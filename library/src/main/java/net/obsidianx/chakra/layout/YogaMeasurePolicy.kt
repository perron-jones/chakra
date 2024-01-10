package net.obsidianx.chakra.layout

import android.util.Log
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import com.facebook.yoga.YogaPositionType
import net.obsidianx.chakra.debug.DEFAULT_LOG_TAG
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.debug.dump
import net.obsidianx.chakra.debug.log
import net.obsidianx.chakra.types.FlexNodeData
import net.obsidianx.chakra.types.isChild
import net.obsidianx.chakra.types.isContainer
import kotlin.math.max
import kotlin.math.roundToInt

internal class YogaMeasurePolicy(private val node: YogaNode, private val nodeData: FlexNodeData) : MeasurePolicy {
    private var lastWidth: Int = -1
    private var lastHeight: Int = -1
    private var lastMeasurement: Size = Size.Unspecified

    private val childLayout: Boolean
        get() = (node.data as? FlexNodeData)?.isChild == true

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        (node.data as FlexNodeData).nodeState?.constraints = constraints
        node.log { "[Sync] my constraints: $constraints" }

        // Step 1: Ensure the yoga UI tree is prepped for this container
        syncNodes(measurables)

        // Step 2: Measure constraints and run yoga calculation from this node downward
        val size = measureYoga(measurables, constraints)

        // Step 3: Apply yoga's measurements to compose views
        val placeables = mutableListOf<Placeable>()
        measurables.fastForEachIndexed { index, measurable ->
            node.log { "[Measure] Applying calculated sizes" }
            val childNode = node.getChildAt(index)
            val childNodeData = childNode.data as FlexNodeData

            val horizontalPadding = childNode.layoutHorizontalPadding.takeUnless { childNodeData.isContainer } ?: 0f
            val verticalPadding = childNode.layoutVerticalPadding.takeUnless { childNodeData.isContainer } ?: 0f

            val childWidth = (childNode.layoutWidth - horizontalPadding)
                .roundToInt()
                .coerceAtLeast(0)
            val childHeight = (childNode.layoutHeight - verticalPadding)
                .roundToInt()
                .coerceAtLeast(0)

            node.log { "[Measure] Measured child [$index][${childNode.address}] size: (w: $childWidth, h: $childHeight)" }

            if (childNodeData.isContainer) {
                childNode.markLayoutSeen()
            }

            placeables.add(measurable.measure(Constraints.fixed(childWidth, childHeight)))
        }

        node.log { "[Measure] Size: (w: ${size.width}, h: ${size.height})" }
        // Step 4: Place views
        return layout(size.width.toInt(), size.height.toInt()) {
            val paddingStart = node.getLayoutPadding(YogaEdge.START)
            val paddingTop = node.getLayoutPadding(YogaEdge.TOP)

            placeables.fastForEachIndexed { index, placeable ->
                val childNode = node.getChildAt(index)
                val childNodeData = childNode.data as FlexNodeData

                val childPaddingStart = childNode.getLayoutPadding(YogaEdge.START).takeUnless { childNodeData.isContainer } ?: 0f
                val childPaddingTop = childNode.getLayoutPadding(YogaEdge.TOP).takeUnless { childNodeData.isContainer } ?: 0f

                val x = (childNode.layoutX + childPaddingStart).coerceAtLeast(paddingStart).roundToInt()
                val y = (childNode.layoutY + childPaddingTop).coerceAtLeast(paddingTop).roundToInt()

                node.log { "[Place] Child[$index][${childNode.address}] => (x: $x, y: $y)" }
                placeable.placeRelative(x, y)
            }

            if (!childLayout) {
                nodeData.debugDumpFlags?.let { dumpFlags ->
                    node.dump(flags = dumpFlags).split('\n').fastForEach { log ->
                        Log.d(nodeData.debugLogTag ?: DEFAULT_LOG_TAG, log)
                    }
                }
            }
            nodeData.nodeState?.child = false
            nodeData.nodeState?.synced = false

            node.log { "[Place] Complete" }
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int {
        return maxIntrinsicWidth(measurables, height)
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
        return maxIntrinsicHeight(measurables, width)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurables: List<IntrinsicMeasurable>, height: Int): Int {
        syncNodes(measurables)
        return measureYoga(measurables, Constraints(maxHeight = height), true).width.toInt()
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
        syncNodes(measurables)
        return measureYoga(measurables, Constraints(maxWidth = width), true).height.toInt()
    }

    /**
     * Ensures the yoga node tree is aligned with the compose tree
     */
    private fun syncNodes(measurables: List<IntrinsicMeasurable>) {
        val nodePool = mutableListOf<YogaNode>()

        if (childLayout) {
            (node.owner?.data as FlexNodeData).nodeState?.constraints?.let { constraints ->
                (node.data as FlexNodeData).nodeState?.constraints = constraints
                node.log { "[Sync] my constraints: $constraints" }
            }
        }

        if (nodeData.nodeState?.synced != true) {
            lastWidth = -1
            lastHeight = -1
            lastMeasurement = Size.Unspecified
            // remove existing nodes
            while (node.childCount > 0) {
                node.removeChildAt(node.childCount - 1).also { childNode ->
                    if (!childNode.isContainer) {
                        childNode.reset()
                        nodePool += childNode
                    }
                }
            }
            // add nodes for each child, using
            while (node.childCount < measurables.size) {
                val childNodeData = measurables[node.childCount].parentData as? FlexNodeData
                val childNode =
                    // use the container's node
                    childNodeData?.let { childNodeData.nodeState?.node }
                    // or use one of the previously removed leaf nodes
                        ?: nodePool.removeLastOrNull()
                        // or create a new one
                        ?: YogaNodeFactory.create()

                childNode.apply {
                    owner?.apply {
                        removeChildAt(indexOf(childNode))
                    }
                    data = childNodeData?.also {
                        childNodeData.nodeState?.child = true
                    }
                }
                node.log { "[Sync] Added child [${node.childCount}][${childNode.address}]" }
                node.addChildAt(childNode, node.childCount)
            }
            nodeData.nodeState?.synced = true
        }
    }

    /**
     * Runs calculateLayout after gathering intrinsic measurements for child nodes
     */
    private fun measureYoga(measurables: List<IntrinsicMeasurable>, constraints: Constraints = Constraints(), intrinsic: Boolean = false): Size {
        var constraintWidth = constraints.maxWidth
        var constraintHeight = constraints.maxHeight
        if (intrinsic && childLayout) {
            // get full constraints from parent instead of just doing one dimension
            (node.owner?.data as? FlexNodeData)?.nodeState?.constraints?.let { parentConstraints ->
                node.log { "[Measure] Parent constraints: $parentConstraints" }
                constraintWidth = parentConstraints.maxWidth
                constraintHeight = parentConstraints.maxHeight
            }
            if (lastMeasurement.isSpecified) {
                return lastMeasurement
            }
        }

        val isFixedContainer = constraints.hasFixedWidth && constraints.hasFixedHeight && childLayout
        if (isFixedContainer) {
            node.log { "[Measure] Parent container assigned size: (w: ${constraints.maxWidth}, h: ${constraints.maxHeight})" }
        } else {
            node.log { "[Measure] Measuring intrinsic sizes of ${node.childCount} child nodes (maxWidth: $constraintWidth; maxHeight: $constraintHeight)" }
        }
        var dirty = false

        // Measure intrinsics
        measurables.fastForEachIndexed { index, measurable ->
            val childNode = node.getChildAt(index)
            val parentDataNodeData = measurable.parentData as? FlexNodeData
            val childNodeData = childNode.data as? FlexNodeData
                ?: (parentDataNodeData ?: FlexNodeData()).also { newNodeData ->
                    childNode.data = newNodeData
                }

            childNodeData.style.apply(childNode)

            if (nodeData.depthLayout) {
                childNode.positionType = YogaPositionType.ABSOLUTE
            }

            if (isFixedContainer) {
                return@fastForEachIndexed
            }

            node.log { "[Measure] Measuring child[$index][${childNode.address}] with Constraints (w: $constraintWidth, h: $constraintHeight)" }

            val maxWidth = measurable.maxIntrinsicWidth(constraintHeight)
            val maxHeight = measurable.maxIntrinsicHeight(constraintWidth)

            depthLayout(childNode, childNodeData)

            dirty = dirty ||
                    maxWidth != childNodeData.intrinsicMax?.width ||
                    maxHeight != childNodeData.intrinsicMax?.height

            val oldIntrinsics = childNodeData.intrinsicMax
            childNodeData.intrinsicMax = IntSize(maxWidth, maxHeight)

            if (!childNodeData.isContainer) {
                node.log { "[Measure] Leaf node: [$index][${childNode.address}]" }
                childNode.setMeasureFunction(::measureFlexNode)
                if (dirty) {
                    childNode.dirty()
                }
                node.log { "[Measure] Size of leaf node [$index][${childNode.address}] (w: $maxWidth, h: $maxHeight)${if (dirty) " (changed from (w: ${oldIntrinsics?.width ?: "N/P"}, h: ${oldIntrinsics?.height ?: "N/P"})" else ""}" }
            } else {
                node.log { "[Measure] Size of container node [$index][${childNode.address}] (w: $maxWidth, h: $maxHeight)" }
            }
        }

        // Measurement is being provided by parent, don't continue with calculateLayout()
        if (isFixedContainer) {
            return Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
        }

        // Get this container's size if it's a depth layout
        val depthSize = depthLayout(node, nodeData)

        return node.run {
            // Only run calculateLayout() at the root of the (re)measure tree
            if (!intrinsic && !childLayout) {
                calculateLayout(constraints)
                val depthLayouts = childDepthLayouts
                if (depthLayouts.isNotEmpty()) {
                    log { "[Measure] Depth layout, second pass" }
                    depthLayouts.forEach {
                        depthLayout(it, it.data as FlexNodeData)
                    }
                    calculateLayout(constraints)
                }
            } else if (nodeData.depthLayout) {
                return@run depthSize
            }
            Size(layoutWidth, layoutHeight)
        }.also {
            lastWidth = constraintWidth
            lastHeight = constraintHeight
            lastMeasurement = it
        }
    }

    private fun calculateLayout(constraints: Constraints) {
        node.apply {
            getConstraints(constraints, owner).let {
                log { "[Measure] calculateLayout" }
                calculateLayout(it[0], it[1])
            }
        }
    }

    private fun depthLayout(node: YogaNode, nodeData: FlexNodeData): Size {
        // special mode that allows for stackable layouts
        if (nodeData.depthLayout) {
            var contentWidth = 0
            var contentHeight = 0
            repeat(node.childCount) { index ->
                node.getChildAt(index).apply {
                    var nodeWidth = 0
                    var nodeHeight = 0
                    (data as? FlexNodeData)?.intrinsicMax?.let { size ->
                        nodeWidth = size.width
                        nodeHeight = size.height
                    }
                    if (nodeWidth > 0 || nodeHeight > 0) {
                        contentWidth = max(contentWidth, nodeWidth)
                        contentHeight = max(contentHeight, nodeHeight)
                    } else {
                        contentWidth = max(contentWidth, layoutWidth.toInt())
                        contentHeight = max(contentHeight, layoutHeight.toInt())
                    }
                }
            }

            val minWidth = node.horizontalPadding
            val minHeight = node.verticalPadding

            val measuredWidth = minWidth + contentWidth
            val measuredHeight = minHeight + contentHeight

            val crossStretch = node.owner?.alignItems == YogaAlign.STRETCH && node.positionType == YogaPositionType.RELATIVE
            val inColumn = node.owner?.flexDirection?.isColumn == true
            val inRow = node.owner?.flexDirection?.isRow == true

            // don't override width if set from the style
            if (!nodeData.style.width.isSet) {
                // if width is a percentage or if we're supposed to stretch in a column, set minWidth but don't override the style's minWidth
                if (crossStretch && inColumn && !nodeData.style.minWidth.isSet) {
                    node.setMinWidth(measuredWidth)
                    node.log { "[Measure][Depth] Set min width: $measuredWidth" }
                } else if (!crossStretch || inRow) {
                    node.setWidth(measuredWidth)
                    node.log { "[Measure][Depth] Set width: $measuredWidth" }
                }
            } else {
                node.log { "[Measure][Depth] Skipped width; user-defined width (intrinsic width: $measuredWidth)" }
            }

            // don't override height if set from the style
            if (!nodeData.style.height.isSet) {
                if (crossStretch && inRow && !nodeData.style.minHeight.isSet) {
                    node.setMinHeight(measuredHeight)
                    node.log { "[Measure][Depth] Set min height: $measuredHeight" }
                } else if (!crossStretch || inColumn) {
                    node.setHeight(measuredHeight)
                    node.log { "[Measure][Depth] Set height: $measuredHeight" }
                }
            } else {
                node.log { "[Measure][Depth] Skipped height; user-defined height (intrinsic height: $measuredHeight)" }
            }

            return Size(measuredWidth, measuredHeight)
        }
        return Size.Unspecified
    }
}