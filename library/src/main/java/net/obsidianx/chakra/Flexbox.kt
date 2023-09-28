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
import com.facebook.yoga.YogaFlexDirection
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
import net.obsidianx.chakra.layout.horizontalGap
import net.obsidianx.chakra.layout.horizontalPadding
import net.obsidianx.chakra.layout.isSet
import net.obsidianx.chakra.layout.measureNode
import net.obsidianx.chakra.layout.removeAllChildren
import net.obsidianx.chakra.layout.verticalGap
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
        val fitMinContent = layoutFlexData?.fitMinContent == true

        val changeRoot = parentLayoutState?.layoutComplete != false
        val intrinsicPass = parentLayoutState?.remeasure != true || parentLayoutState.layoutComplete
        val remeasurePass =
            parentLayoutState?.remeasure != false || parentLayoutState.layoutComplete

        log("[Measure] Root: ${parentLayoutState == null}; Intrinsic: $intrinsicPass; Remeasure: $remeasurePass; Change root: $changeRoot")

        if (intrinsicPass) {
            log("[Intrinsic] Start (child nodes: ${measurables.size}) (fitMinContent: $fitMinContent) ($constraints)")
            layoutState.layoutComplete = false
            layoutState.originalStyle.apply(node)

            var minContentWidth = 0f
            var minContentHeight = 0f

            measurables.mapIndexed { index, childMeasurable ->
                // get or add a flexbox node
                var childNodeData = childMeasurable.parentData as? FlexNodeData
                val childNode = childNodeData?.layoutNode ?: makeChildNode(index).also { newNode ->
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

                childNodeData.debugTag.takeIf { it.isNotEmpty() }?.let { debugTag ->
                    log("[Intrinsic] Child debug tag: $debugTag [$index][${childNode.address}]")
                }

                val minWidth: Float
                val minHeight: Float

                val nodeType = if (childNodeData.isContainer) "node" else "leaf"

                // apply style prior to measuring so we don't clobber any style updates a child
                // container may set for itself in the fitMinContent mode
                childNodeData.style.apply(childNode)

                log("[Intrinsic] Measuring $nodeType [$index][${childNode.address}]")
                val childPlaceable = childMeasurable.measure(Constraints())

                // configure node
                if (!childNodeData.isContainer) {
                    childNode.setMeasureFunction(::measureNode)

                    // measure intrinsic size
                    minWidth = childPlaceable.measuredWidth.toFloat()
                    minHeight = childPlaceable.measuredHeight.toFloat()

                    if (childNodeData.minWidth != minWidth || childNodeData.minHeight != minHeight) {
                        childNode.dirty()
                    }

                    childNodeData.minWidth = minWidth
                    childNodeData.minHeight = minHeight
                } else {
                    minWidth = max(childNodeData.minWidth, childNode.width.asFloatOrZero)
                    minHeight = max(childNodeData.minHeight, childNode.height.asFloatOrZero)
                }

                var logAction = "Updated"
                if (node.childCount <= index) {
                    node.addChildAt(childNode, index)
                    logAction = "Added"
                }
                log("[Intrinsic] $logAction ${if (childNodeData.isContainer) "node" else "leaf"} [$index][${childNode.address}], size: ($minWidth, $minHeight)")

                minContentWidth = when {
                    fitMinContent || node.flexDirection == YogaFlexDirection.COLUMN -> max(
                        minWidth,
                        minContentWidth
                    )

                    node.flexDirection == YogaFlexDirection.ROW -> minContentWidth + minWidth
                    else -> 0f
                }
                minContentHeight = when {
                    fitMinContent || node.flexDirection == YogaFlexDirection.ROW -> max(
                        minHeight,
                        minContentHeight
                    )

                    node.flexDirection == YogaFlexDirection.COLUMN -> minContentHeight + minHeight
                    else -> 0f
                }
            }

            minContentWidth += node.horizontalPadding + (node.horizontalGap * (node.childCount - 1))
            minContentHeight += node.verticalPadding + (node.verticalGap * (node.childCount - 1))

            layoutFlexData?.let { flexData ->
                flexData.minWidth = minContentWidth
                flexData.minHeight = minContentHeight

                // if this node is in fitMinContent mode assign the min size accordingly
                if (fitMinContent) {
                    minContentWidth.takeIf { it > 0 }?.let { width ->
                        node.setWidth(max(flexData.style.width.asFloatOrZero, width))
                    }
                    minContentHeight.takeIf { it > 0 }?.let { height ->
                        node.setHeight(max(flexData.style.height.asFloatOrZero, height))
                    }

                    log("[Intrinsic] Done; fit min size: ($minContentWidth, $minContentHeight)")
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
            val layoutPadStart = node.getLayoutPadding(YogaEdge.START)
            val layoutPadTop = node.getLayoutPadding(YogaEdge.TOP)
            placeables.fastForEachIndexed { index, placeable ->
                if (node.childCount <= index) {
                    return@fastForEachIndexed
                }
                val childNode = node.getChildAt(index)
                val isContainer = childNode.childCount > 0

                val paddingStart =
                    childNode.getLayoutPadding(YogaEdge.START).takeIf { !isContainer } ?: 0f
                val paddingTop =
                    childNode.getLayoutPadding(YogaEdge.TOP).takeIf { !isContainer } ?: 0f

                val nodeX =
                    (childNode.layoutX + paddingStart).coerceAtLeast(layoutPadStart).roundToInt()
                val nodeY =
                    (childNode.layoutY + paddingTop).coerceAtLeast(layoutPadTop).roundToInt()

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
