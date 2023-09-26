package net.obsidianx.chakra

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MultiMeasureLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.debug.DEFAULT_LOG_TAG
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.debug.dump
import net.obsidianx.chakra.layout.FlexLayoutState
import net.obsidianx.chakra.layout.asFloatOrZero
import net.obsidianx.chakra.layout.getConstraints
import net.obsidianx.chakra.layout.horizontalPadding
import net.obsidianx.chakra.layout.measureNode
import net.obsidianx.chakra.layout.removeAllChildren
import net.obsidianx.chakra.layout.verticalPadding
import net.obsidianx.chakra.modifiers.flexContainer
import net.obsidianx.chakra.types.FlexNodeData
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun FlexboxScope.Flexbox(
    modifier: Modifier = Modifier,
    content: @Composable FlexboxScope.() -> Unit = {}
) {
    Flexbox(
        modifier = modifier,
        parentLayoutState = parentLayoutState
    ) { content() }
}

@SuppressLint("RememberReturnType")
@Composable
fun Flexbox(
    modifier: Modifier = Modifier,
    parentLayoutState: FlexLayoutState? = null,
    content: @Composable FlexboxScope.() -> Unit = {}
) {
    val context by rememberUpdatedState(LocalContext.current)
    remember { SoLoader.init(context, false) }


    val layoutState = remember { FlexLayoutState() }
    val scope = FlexboxScope(layoutState)

    var layoutNode = layoutState.selfNode
    var layoutFlexData: FlexNodeData? = null

    val log: (String) -> Unit = { msg ->
        if (Chakra.debugLogging) {
            Log.d("Chakra", "[${layoutNode.address}]$msg")
        }
    }

    val makeNode = {
        YogaNodeFactory.create().also {
            log("[Layout] Creating a new node: [${it.address}]")
        }
    }
    val makeChildNode: (Int) -> YogaNode = { index ->
        layoutNode?.let {
            if (index < it.childCount) it.getChildAt(index).also { childNode ->
                log("[Layout] Reusing child node: [${childNode.address}]")
            } else null
        } ?: makeNode()
    }

    // Multimeasure allows the layout to build the Yoga node tree recursively
    @Suppress("DEPRECATION")
    MultiMeasureLayout(
        modifier = modifier
            .flexContainer()
            .layout { measurable, constraints ->
                // extract rules from own modifier
                if (layoutFlexData == null) {
                    layoutFlexData = (measurable.parentData as? FlexNodeData)?.also {
                        layoutState.originalStyle = it.style.copy()
                    }
                }
                if (layoutNode == null) {
                    layoutState.selfNode = (layoutFlexData?.layoutNode?.also {
                        log("[Layout] Reusing container node: [${it.address}]")
                    } ?: parentLayoutState?.childNode?.also {
                        log("[Layout] Using node from parent: [${it.address}]")
                    } ?: makeNode())
                    layoutNode = layoutState.selfNode
                }
                if (layoutFlexData == null) {
                    layoutFlexData = (layoutNode?.data as? FlexNodeData ?: FlexNodeData()).also {
                        it.style = layoutState.originalStyle.copy()
                    }
                }
                layoutFlexData?.isContainer = true
                layoutFlexData?.style?.let { origStyle ->
                    layoutState.originalStyle = origStyle.copy()
                }
                log("[Layout] Measure")
                val placeable = measurable.measure(constraints)
                layout(placeable.measuredWidth, placeable.measuredHeight) {
                    log("[Layout] Place [${layoutNode?.layoutWidth}, ${layoutNode?.layoutHeight}]@(${layoutNode?.layoutX}, ${layoutNode?.layoutY})")
                    placeable.placeRelative(0, 0)
                }
            },
        content = {
            log("[Content] Parent layout state: ${parentLayoutState.address}")
            content(scope)
            layoutNode?.removeAllChildren()
        }
    ) layout@{ measurables, constraints ->
        val node = layoutNode ?: return@layout layout(0, 0) {}

        val changeRoot = parentLayoutState?.layoutComplete != false
        val intrinsicPass = parentLayoutState?.remeasure != true || parentLayoutState.layoutComplete
        val remeasurePass =
            parentLayoutState?.remeasure != false || parentLayoutState.layoutComplete

        log("[Measure] Root: ${parentLayoutState == null}; Intrinsic: $intrinsicPass; Remeasure: $remeasurePass; Change root: $changeRoot")

        if (intrinsicPass) {
            log("[Intrinsic] Start (child nodes: ${measurables.size}) ($constraints)")
            layoutState.layoutComplete = false
            layoutState.originalStyle.apply(node)

            val horizontalPadding = node.horizontalPadding().toInt()
            val verticalPadding = node.verticalPadding().toInt()

            var minContentWidth = horizontalPadding
            var minContentHeight = verticalPadding

            log("[Intrinsic] Container padding: ($horizontalPadding, $verticalPadding)")

            measurables.mapIndexed { index, childMeasurable ->
                // get or add a flexbox node
                var childNodeData = (childMeasurable.parentData as? FlexNodeData)?.also {
                    log("[Intrinsic] Found flex node data on child [${childMeasurable.parentData.address}]")
                }
                val childNode =
                    childNodeData?.layoutNode ?: makeChildNode(index).also { newNode ->
                        childNodeData?.let {
                            it.layoutNode = newNode
                            newNode.data = it
                        }
                    }
                layoutState.childNode = childNode

                // assign empty node data if not present from parentData
                childNodeData = childNodeData ?: FlexNodeData().apply {
                    log("[Intrinsic] Creating new FlexNodeData for [${childNode.address}]")
                    childNode.data = this
                }

                val minWidth: Int
                val minHeight: Int

                var logAction = "Updated"
                var logTrailer = "[container]"

                log("[Intrinsic] Measuring child[$index]")
                val childPlaceable = childMeasurable.measure(Constraints())

                // configure node
                if (!childNodeData.isContainer) {
                    childNode.setMeasureFunction(::measureNode)

                    // measure intrinsic size
                    minWidth = childPlaceable.measuredWidth
                    minHeight = childPlaceable.measuredHeight

                    if (childNodeData.minWidth != minWidth || childNodeData.minHeight != minHeight) {
                        childNode.dirty()
                    }

                    childNodeData.minWidth = minWidth
                    childNodeData.minHeight = minHeight

                    logTrailer = "size: ($minWidth, $minHeight)"
                } else {
                    minWidth = childNode.horizontalPadding().toInt()
                    minHeight = childNode.verticalPadding().toInt()
                }

                if (node.childCount <= index) {
                    node.addChildAt(childNode, index)
                    logAction = "Added"
                }
                log("[Intrinsic] $logAction [$index][${childNode.address}], $logTrailer")
                childNodeData.style.apply(childNode)

                minContentWidth = max(minWidth + horizontalPadding, minContentWidth)
                minContentHeight = max(minHeight + verticalPadding, minContentHeight)
            }

            layoutFlexData?.let { flexData ->
                // if this node is in fitMinContent mode assign the min size accordingly
                if (flexData.fitMinContent) {
                    minContentWidth.takeIf { it > 0 }?.toFloat()
                        ?.let { width ->
                            flexData.style.minWidth =
                                YogaValue(max(node.minWidth.asFloatOrZero, width), YogaUnit.POINT)
                        }
                    minContentHeight.takeIf { it > 0 }?.toFloat()
                        ?.let { height ->
                            flexData.style.minHeight =
                                YogaValue(max(node.minHeight.asFloatOrZero, height), YogaUnit.POINT)
                        }

                    flexData.style.apply(node)

                    log("[Intrinsic] Done; fit min size: ($minContentWidth, $minContentHeight)${if (horizontalPadding > 0 || verticalPadding > 0) "; includes padding: ($horizontalPadding, $verticalPadding)" else ""}")
                } else {
                    log("[Intrinsic] Done")
                }
            }

            layoutState.remeasure = true

            if (!remeasurePass) {
                return@layout layout(0, 0) {}
            }
        }

        log("[Remeasure] Start ($constraints)")

        if (changeRoot) {
            // if we're at the top of the updated subtree, recalculate the whole subtree once
            node.apply {
                getConstraints(constraints).let {
                    // ensure we calculate the whole tree from this point
//                    deepDirty()
                    layoutNode?.calculateLayout(it[0], it[1])
                    log("[Remeasure] Final container size: (${node.layoutWidth}, ${node.layoutHeight}); constraints: (${it[0]}, ${it[1]})")
                }
            }
        }

        // now that flexbox has the final size for child nodes, synchronize with compose
        val placeables = measurables.mapIndexed { index, measurable ->
            if (node.childCount <= index) {
                log("[Remeasure] Skipping child[$index]; not in container")
                return@mapIndexed measurable.measure(Constraints())
            }
            val childNode = node.getChildAt(index)
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
            log("[Remeasure] Child[$index] final size: ($childWidth, $childHeight)")
            measurable.measure(childConstraints)
        }

        layout(node.layoutWidth.toInt(), node.layoutHeight.toInt()) {
            placeables.fastForEachIndexed { index, placeable ->
                if (node.childCount <= index) {
                    return@fastForEachIndexed
                }
                val childNode = node.getChildAt(index)
                val isContainer = childNode.childCount > 0

                // Padding was removed in the measuring phase, so offset the view by the padding
                // amount to ensure the content is in the right place
                val paddingStart =
                    childNode.getLayoutPadding(YogaEdge.START).takeIf { !isContainer } ?: 0f
                val paddingTop =
                    childNode.getLayoutPadding(YogaEdge.TOP).takeIf { !isContainer } ?: 0f

                val nodeX = (childNode.layoutX + paddingStart).roundToInt()
                val nodeY = (childNode.layoutY + paddingTop).roundToInt()

                log("[Place] Child[$index][${childNode.address}] -> [${childNode.layoutWidth}, ${childNode.layoutHeight}]@($nodeX, $nodeY)")
                placeable.place(nodeX, nodeY)
            }

            layoutState.remeasure = false
            layoutState.layoutComplete = true
            log("[Remeasure] Done")

            if (changeRoot) {
                layoutFlexData?.debugDumpFlags?.let { dumpFlags ->
                    layoutNode?.dump(flags = dumpFlags)?.split('\n')?.fastForEach { log ->
                        Log.d(layoutFlexData?.debugLogTag ?: DEFAULT_LOG_TAG, log)
                    }
                }
            }
        }
    }
}
