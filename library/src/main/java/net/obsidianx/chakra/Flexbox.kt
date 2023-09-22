package net.obsidianx.chakra

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MultiMeasureLayout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNodeFactory
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.debug.DEFAULT_LOG_TAG
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.debug.dump
import net.obsidianx.chakra.layout.FlexLayoutState
import net.obsidianx.chakra.layout.asFloatOrZero
import net.obsidianx.chakra.layout.deepDirty
import net.obsidianx.chakra.layout.getConstraints
import net.obsidianx.chakra.layout.measureNode
import net.obsidianx.chakra.layout.removeAllChildren
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
        // Log.d("XXX", "[${layoutNode.address}]$msg")
    }

    MultiMeasureLayout(
        modifier = modifier.layout { measurable, constraints ->
            // extract rules from own modifier
            if (layoutFlexData == null) {
                layoutFlexData = (measurable.parentData as? FlexNodeData)?.also {
                    layoutState.originalStyle = it.style.copy()
                }
            }
            if (layoutNode == null) {
                layoutState.selfNode = (layoutFlexData?.layoutNode ?: parentLayoutState?.childNode
                ?: YogaNodeFactory.create())
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
            log("[Content]")
            content(scope)
        }
    ) { measurables, constraints ->
        val node = layoutNode ?: return@MultiMeasureLayout layout(0, 0) {}

        val intrinsicPass = parentLayoutState?.remeasure != true || parentLayoutState.layoutComplete
        val remeasurePass =
            parentLayoutState?.remeasure != false || parentLayoutState.layoutComplete

        log("[Measure] Root: ${parentLayoutState == null}; Intrinsic: $intrinsicPass; Remeasure: $remeasurePass")

        lateinit var placeables: List<Placeable>
        if (intrinsicPass) {
            log("[Intrinsic] Start (child nodes: ${measurables.size}) ($constraints)")
            layoutState.layoutComplete = false
            node.removeAllChildren()

            layoutState.originalStyle.apply(node)

            var minContentWidth = 0
            var minContentHeight = 0

            placeables = measurables.fastMap { childMeasurable ->
                // get or add a flexbox node
                val childNodeData = childMeasurable.parentData as? FlexNodeData
                val childNode =
                    childNodeData?.layoutNode ?: YogaNodeFactory.create().also { newNode ->
                        childNodeData?.let { it.layoutNode = newNode }
                    }
                layoutState.childNode = childNode
                childNode.data = childNodeData

                childNodeData?.style?.apply(childNode)

                log("[Intrinsic] Node: [${childNode.address}]; Data: [${childNodeData.address}]; width: ${childNode.width}; height: ${childNode.height}")

                // measure intrinsic size
                childMeasurable.measure(Constraints()).also { childPlaceable ->
                    // assign empty node data if not present from parentData
                    if (childNode.data == null) {
                        childNode.data = FlexNodeData().apply {
                            style.apply(childNode)
                        }
                    }

                    // assign placeable for measure function
                    (childNode.data as FlexNodeData).placeable = childPlaceable

                    // add to parent
                    log("[Intrinsic] Add [${node.childCount}][${childNode.address}], size: (${childPlaceable.measuredWidth}, ${childPlaceable.measuredHeight})")
                    node.addChildAt(childNode, node.childCount)

                    // configure node
                    if (childNodeData?.isContainer != true) {
                        childNode.setMeasureFunction(::measureNode)
                    }

                    minContentWidth = max(childPlaceable.measuredWidth, minContentWidth)
                    minContentHeight = max(childPlaceable.measuredHeight, minContentHeight)

                    childNodeData?.let { nodeData ->
                        if (nodeData.fitMinContent) {
                            // save the previously measured minimum size if we have one
                            nodeData.style.width = childNode.layoutWidth.takeIf { it > 0 }
                                ?.let { YogaValue(it, YogaUnit.POINT) }
                                ?: nodeData.style.width
                            nodeData.style.height = childNode.layoutHeight.takeIf { it > 0 }
                                ?.let { YogaValue(it, YogaUnit.POINT) }
                                ?: nodeData.style.height
                        }
                        nodeData.style.apply(childNode)
                    }
                }
            }.also {
                // calculate flexbox for this container, which also copies compose measurements via ::measureNode()
                node.getConstraints(constraints).let { yogaConstraints ->
                    layoutNode?.let { node ->
                        val fitMinContent = layoutFlexData?.fitMinContent == true
                        val width = layoutFlexData?.let { nodeData ->
                            (minContentWidth + nodeData.style.padding.getHorizontal()).takeIf { fitMinContent }
                        } ?: yogaConstraints[0]
                        val height = layoutFlexData?.let { nodeData ->
                            (minContentHeight + nodeData.style.padding.getVertical()).takeIf { fitMinContent }
                        } ?: yogaConstraints[1]
                        if (fitMinContent) {
                            node.setWidth(max(node.width.asFloatOrZero, width))
                            node.setHeight(max(node.height.asFloatOrZero, height))
                        }
                        log("[Intrinsic] Calculate layout ($width, $height) node size: (${node.width}, ${node.height}); min size: (${node.minWidth}, ${node.minHeight}); max size: (${node.maxWidth}, ${node.maxHeight})")
                        node.calculateLayout(width, height)
                        log("[Intrinsic] Container size: (${node.layoutWidth}, ${node.layoutHeight}); fitMin: $fitMinContent; minSize: ($minContentWidth, $minContentHeight)")
                    }
                }

            }
            log("[Intrinsic] Done")
            layoutState.remeasure = true
        }
        if (remeasurePass) {
            log("[Remeasure] Start ($constraints)")
            val measuredWidth =
                node.layoutWidth.roundToInt().takeIf { it != Constraints.Infinity }
                    ?: constraints.maxWidth
            val measuredHeight =
                node.layoutHeight.roundToInt().takeIf { it != Constraints.Infinity }
                    ?: constraints.maxHeight
            val minWidth = max(node.minWidth.asFloatOrZero, measuredWidth.toFloat())
            val minHeight = max(node.minHeight.asFloatOrZero, measuredHeight.toFloat())

            // apply sizes to this node
            node.apply {
                (data as? FlexNodeData)?.style?.apply(this)
                setMinWidth(minWidth)
                setMinHeight(minHeight)
                setMaxWidth(measuredWidth.toFloat())
                setMaxHeight(measuredHeight.toFloat())
                getConstraints(constraints).let {
                    // invalidate child sizes to determine final sizes relative to final size of container
                    calculateLayout(it[0], it[1])
                }
            }

            node.apply {
                deepDirty()
                calculateLayout(measuredWidth.toFloat(), measuredHeight.toFloat())
            }

            // now that flexbox has the final size for child nodes, force compose to match
            placeables = measurables.mapIndexed { index, measurable ->
                if (node.childCount <= index) {
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
                measurable.measure(childConstraints)
            }.also {
                // final sync between compose and flexbox
                node.calculateLayout(measuredWidth.toFloat(), measuredHeight.toFloat())
                log("[Remeasure] Final container size: (${node.layoutWidth}, ${node.layoutHeight})")
            }
        }

        layout(node.layoutWidth.toInt(), node.layoutHeight.toInt()) {
            if (!remeasurePass) return@layout

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

                log("[Place] Child[$index][${childNode.address}] -> [${node.layoutWidth}, ${node.layoutHeight}]@($nodeX, $nodeY)")
                placeable.place(nodeX, nodeY)
            }

            layoutState.remeasure = false
            layoutState.layoutComplete = true
            log("[Remeasure] Done")

            if (parentLayoutState?.layoutComplete != false) {
                layoutFlexData?.debugDumpFlags?.let { dumpFlags ->
                    layoutNode?.dump(flags = dumpFlags)?.split('\n')?.fastForEach { log ->
                        Log.d(layoutFlexData?.debugLogTag ?: DEFAULT_LOG_TAG, log)
                    }
                }
            }
        }
    }
}
