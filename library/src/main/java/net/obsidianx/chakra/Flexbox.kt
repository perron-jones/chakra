package net.obsidianx.chakra

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNodeFactory
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.debug.DEFAULT_LOG_TAG
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.debug.dump
import net.obsidianx.chakra.layout.FlexLayoutState
import net.obsidianx.chakra.layout.asFloatOrZero
import net.obsidianx.chakra.layout.deepDirty
import net.obsidianx.chakra.layout.getChildOrNull
import net.obsidianx.chakra.layout.getConstraints
import net.obsidianx.chakra.layout.measureNode
import net.obsidianx.chakra.layout.removeAllChildren
import net.obsidianx.chakra.modifiers.flexboxParentData
import net.obsidianx.chakra.types.FlexNodeData
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

private fun (@Composable FlexboxScope.() -> Unit).withProviders(
    scope: FlexboxScope,
    debugDumpFlags: Set<DebugDumpFlag>? = null,
): @Composable () -> Unit = {
    CompositionLocalProvider(
        LocalDebugDumpFlags provides debugDumpFlags
    ) {
        this(scope)
    }
}

@Composable
fun FlexboxScope.Flexbox(
    modifier: Modifier = Modifier,
    content: @Composable FlexboxScope.() -> Unit
) {
    Flexbox(
        modifier = modifier,
        parentLayoutState = parentLayoutState,
        content = content
    )
}

@Composable
fun Flexbox(
    modifier: Modifier = Modifier,
    parentLayoutState: FlexLayoutState? = null,
    content: @Composable FlexboxScope.() -> Unit
) {
    val context = LocalContext.current
    val treeDebugFlags = LocalDebugDumpFlags.current
    // Passed down to child views when doing subcompose passes
    val myLayoutState = FlexLayoutState()
    val mod = modifier.flexboxParentData()
    SoLoader.init(context, false)

    val scope = FlexboxScope(myLayoutState)

    // Test opposites to ensure both are true on a root node
    val intrinsicPass = parentLayoutState?.remeasure != true || parentLayoutState.layoutComplete
    val remeasurePass = parentLayoutState?.remeasure != false || parentLayoutState.layoutComplete

    SubcomposeLayout(modifier = mod) { constraints ->
        // Get this view's yoga node or make a new one
        val containerNode =
            parentLayoutState?.parent?.getChildOrNull(parentLayoutState.childIndex)?.apply {
                // ensure there's no measure function if this is a reused node
                setMeasureFunction(null)
            }
                ?: YogaNodeFactory.create()
        myLayoutState.parent = containerNode

        // Extract flex rules from modifier
        val tempMeasurable = subcompose("parent") { Spacer(modifier = mod) }.first()
        val containerNodeData = (tempMeasurable.parentData as? FlexNodeData)
            ?: throw IllegalStateException("FlexNodeData not present in modifier")
        containerNode.data = containerNodeData

        // Apply flex rules to node
        containerNodeData.style.apply(containerNode)

        if (intrinsicPass) {
            // Add to parent layout, if there is one
            if (containerNode.owner == null && parentLayoutState?.parent != null) {
                if (parentLayoutState.childIndex <= (parentLayoutState.parent?.childCount ?: -1)) {
                    parentLayoutState.parent?.addChildAt(
                        containerNode,
                        parentLayoutState.childIndex
                    )
                } else {
                    Log.w(
                        "Chakra",
                        "[${containerNode.address}] Orphaned child view, tried to add to ${parentLayoutState.parent}"
                    )
                    return@SubcomposeLayout layout(0, 0) {}
                }
            }

            // Remove all child nodes; they'll be re-added during subcompose
            containerNode.removeAllChildren()

            var minContentWidth = 0
            var minContentHeight = 0

            // Gather child view intrinsic sizes
            val intrinsicChildren =
                subcompose(
                    "intrinsic",
                    content.withProviders(
                        scope,
                        containerNodeData.debugDumpFlags ?: treeDebugFlags
                    )
                )
                    .mapIndexed { index, child ->
                        myLayoutState.childIndex = index
                        child.measure(Constraints()).also { childPlaceable ->
                            val childNodeData =
                                (childPlaceable.parentData as? FlexNodeData) ?: FlexNodeData()

                            minContentWidth = max(childPlaceable.measuredWidth, minContentWidth)
                            minContentHeight = max(childPlaceable.measuredHeight, minContentHeight)

                            (containerNode.getChildOrNull(index)
                                ?: YogaNodeFactory.create().also { node ->
                                    // add to parent if this is a new node
                                    containerNode.addChildAt(node, index)
                                }).apply {
                                if (childCount == 0) {
                                    setMeasureFunction(::measureNode)
                                }
                                childNodeData.placeable = childPlaceable
                                data = childNodeData
                                if (childNodeData.fitMinContent) {
                                    // save the previously measured minimum size if we have one
                                    childNodeData.style.minWidth = layoutWidth.takeIf { it > 0 }
                                        ?.let { YogaValue(it, YogaUnit.POINT) }
                                        ?: childNodeData.style.minWidth
                                    childNodeData.style.minHeight = layoutHeight.takeIf { it > 0 }
                                        ?.let { YogaValue(it, YogaUnit.POINT) }
                                        ?: childNodeData.style.minHeight
                                }
                                childNodeData.style.apply(this)
                            }
                        }
                    }

            while (containerNode.childCount > intrinsicChildren.size) {
                containerNode.removeChildAt(containerNode.childCount - 1)
            }

            containerNode.getConstraints(
                from = constraints,
                parentNode = parentLayoutState?.parent,
            ).let { yogaConstraints ->
                val width = if (containerNodeData.fitMinContent) {
                    minContentWidth + containerNodeData.style.padding.getHorizontal()
                } else {
                    yogaConstraints[0]
                }
                val height = if (containerNodeData.fitMinContent) {
                    minContentHeight + containerNodeData.style.padding.getVertical()
                } else {
                    yogaConstraints[1]
                }

                if (containerNodeData.fitMinContent) {
                    containerNode.setMinWidth(max(containerNode.minWidth.asFloatOrZero, width))
                    containerNode.setMinHeight(max(containerNode.minHeight.asFloatOrZero, height))
                }
                containerNode.calculateLayout(width, height)
            }

            if (!remeasurePass) {
                return@SubcomposeLayout layout(
                    containerNode.layoutWidth.toInt(),
                    containerNode.layoutHeight.toInt()
                ) {}
            }
        }


        // Determine the measured dimensions
        val measuredWidth =
            containerNode.layoutWidth.roundToInt().takeIf { it != Constraints.Infinity }
                ?: constraints.maxWidth
        val measuredHeight =
            containerNode.layoutHeight.roundToInt().takeIf { it != Constraints.Infinity }
                ?: constraints.maxHeight
        val minWidth = max(containerNode.minWidth.asFloatOrZero, measuredWidth.toFloat())
        val minHeight = max(containerNode.minHeight.asFloatOrZero, measuredHeight.toFloat())


        containerNode.apply {
            (data as? FlexNodeData)?.style?.apply(this)
            setMinWidth(minWidth)
            setMinHeight(minHeight)
            setMaxWidth(measuredWidth.toFloat())
            setMaxHeight(measuredHeight.toFloat())
            getConstraints(
                from = constraints,
                parentNode = parentLayoutState?.parent
            ).let { yogaConstraints ->
                calculateLayout(yogaConstraints[0], yogaConstraints[1])
            }
        }

        myLayoutState.remeasure = true

        // Reset flex layout to re-layout with calculated constraints
        containerNode.apply {
            deepDirty()
            calculateLayout(measuredWidth.toFloat(), measuredHeight.toFloat())
        }

        // Assign dimensions calculated in flex to views
        val childViews = subcompose(
            "remeasure",
            content.withProviders(
                scope,
                containerNodeData.debugDumpFlags ?: treeDebugFlags
            )
        )
        val measuredChildren = childViews.mapIndexed { index, child ->
            if (containerNode.childCount <= index) {
                return@mapIndexed child.measure(Constraints())
            }
            myLayoutState.childIndex = index
            val childNode = containerNode.getChildAt(index)
            val isContainer = childNode.childCount > 0

            val paddingStart = ceil(childNode.getLayoutPadding(YogaEdge.START))
            val paddingTop = ceil(childNode.getLayoutPadding(YogaEdge.TOP))
            val paddingEnd = ceil(childNode.getLayoutPadding(YogaEdge.END))
            val paddingBottom = ceil(childNode.getLayoutPadding(YogaEdge.BOTTOM))

            val verticalPadding =
                (paddingTop + paddingBottom).takeIf { !isContainer } ?: 0f
            val horizontalPadding =
                (paddingStart + paddingEnd).takeIf { !isContainer } ?: 0f

            val childWidth =
                (childNode.layoutWidth - horizontalPadding)
                    .roundToInt()
                    .coerceAtLeast(0)
            val childHeight =
                (childNode.layoutHeight - verticalPadding)
                    .roundToInt()
                    .coerceAtLeast(0)

            val childConstraints = Constraints(
                minWidth = childWidth,
                minHeight = childHeight,
                maxWidth = childWidth,
                maxHeight = childHeight
            )

            child.measure(childConstraints)
        }

        containerNode.calculateLayout(measuredWidth.toFloat(), measuredHeight.toFloat())

        // Place child views in layout
        return@SubcomposeLayout layout(
            measuredWidth,
            measuredHeight
        ) {
            measuredChildren.fastForEachIndexed { index, childPlaceable ->
                if (containerNode.childCount <= index) {
                    return@fastForEachIndexed
                }
                val childNode = containerNode.getChildAt(index)
                val isContainer = childNode.childCount > 0

                // Padding was removed in the measuring phase, so offset the view by the padding
                // amount to ensure the content is in the right place
                val paddingStart =
                    childNode.getLayoutPadding(YogaEdge.START).takeIf { !isContainer } ?: 0f
                val paddingTop =
                    childNode.getLayoutPadding(YogaEdge.TOP).takeIf { !isContainer } ?: 0f

                val nodeX = (childNode.layoutX + paddingStart).roundToInt()
                val nodeY = (childNode.layoutY + paddingTop).roundToInt()

                childPlaceable.place(nodeX, nodeY)
            }
        }.also {
            myLayoutState.remeasure = false
            myLayoutState.layoutComplete = true

            // Only dump the layout if we're the top-most node with flexDebugDump()
            val debugFlags = containerNodeData.debugDumpFlags ?: treeDebugFlags
            if ((treeDebugFlags == null || parentLayoutState?.layoutComplete == true) && debugFlags != null) {
                debugFlags.let { flags ->
                    containerNode.dump(flags = flags).split("\n")
                        .fastForEach {
                            Log.d(
                                containerNodeData.debugLogTag ?: DEFAULT_LOG_TAG,
                                it
                            )
                        }
                }
            }
        }
    }
}
