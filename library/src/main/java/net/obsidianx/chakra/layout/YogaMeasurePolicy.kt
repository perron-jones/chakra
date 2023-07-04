package net.obsidianx.chakra.layout

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEachIndexed
import com.facebook.yoga.YogaConstants
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import net.obsidianx.chakra.FlexNodeData
import kotlin.math.roundToInt

class YogaMeasurePolicy(private val containerNode: YogaNode) :
    MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        syncToYoga(measurables)

        val maxWidth = constraints.maxWidth
            .takeIf { constraints.hasBoundedWidth }
            ?.toFloat()
            ?: YogaConstants.UNDEFINED
        val maxHeight = constraints.maxHeight
            .takeIf { constraints.hasBoundedHeight }
            ?.toFloat()
            ?: YogaConstants.UNDEFINED

        if (containerNode.isDirty) {
            containerNode.calculateLayout(maxWidth, maxHeight)
        }

        val measuredWidth =
            containerNode.layoutWidth.roundToInt().takeIf { it != Constraints.Infinity }
                ?: constraints.maxWidth
        val measuredHeight =
            containerNode.layoutHeight.roundToInt().takeIf { it != Constraints.Infinity }
                ?: constraints.maxHeight

        // Apply the calculated size to the container
        return layout(measuredWidth, measuredHeight) {
            placeChildren()
        }
    }

    private fun syncToYoga(measurables: List<Measurable>) {
        // Fetch the cached node from a .flex() modifier, or create a temporary one for this pass
        val yogaNodes = measurables.mapIndexed { index, measurable ->
            // get the node attached to the measurable
            (measurable.parentData as? FlexNodeData)?.node
                ?: if (containerNode.childCount > index) {
                    // or attempt to find it in the yoga layout
                    containerNode.getChildAt(index).takeIf { it.data == measurable }
                } else {
                    null
                    // otherwise create a new one
                } ?: YogaNodeFactory.create().also {
                    it.setMeasureFunction(::measureNode)
                    it.data = measurable
                }
        }

        containerNode.run {
            // Remove excess nodes if there are fewer this pass than the previous pass
            while (childCount > yogaNodes.size) {
                removeChildAt(childCount - 1)
            }

            yogaNodes.fastForEachIndexed { index, node ->
                if (index < childCount) {
                    // Position in the container has changed
                    if (getChildAt(index) != node) {
                        removeChildAt(index)
                        addChildAt(node, index)
                    }
                } else {
                    // New node in container
                    addChildAt(node, index)
                }
                if (node.data is YogaNode) {
                    (node.data as YogaNode).data = measurables[index]
                } else {
                    node.data = measurables[index]
                }
            }
        }
    }

    private fun Placeable.PlacementScope.placeChildren() {
        for (index in 0 until containerNode.childCount) {
            val node = containerNode.getChildAt(index)
            val measurable = if (node.data is YogaNode) {
                (node.data as? YogaNode)?.let {
                    for (i in 0 until it.childCount) {
                        it.getChildAt(i).dirty()
                    }
                    it.calculateLayout(node.layoutWidth, node.layoutHeight)
                    it.data as? Measurable
                }
            } else {
                node.data as? Measurable
            } ?: continue

            val paddingStart = node.getLayoutPadding(YogaEdge.START)
            val paddingTop = node.getLayoutPadding(YogaEdge.TOP)
            val paddingEnd = node.getLayoutPadding(YogaEdge.END)
            val paddingBottom = node.getLayoutPadding(YogaEdge.BOTTOM)

            val childWidth = (node.layoutWidth - paddingStart - paddingEnd).roundToInt()
            val childHeight = (node.layoutHeight - paddingTop - paddingBottom).roundToInt()

            val childConstraints = Constraints(
                minWidth = childWidth,
                minHeight = childHeight,
                maxWidth = childWidth,
                maxHeight = childHeight
            )

            val nodeX = (node.layoutX + paddingStart).roundToInt()
            val nodeY = (node.layoutY + paddingTop).roundToInt()

            measurable.measure(childConstraints).place(nodeX, nodeY)
        }
    }
}