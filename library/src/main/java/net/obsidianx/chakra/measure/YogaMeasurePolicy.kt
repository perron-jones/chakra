package net.obsidianx.chakra.measure

import android.util.Log
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
import net.obsidianx.chakra.FlexNodeData
import kotlin.math.roundToInt

class YogaMeasurePolicy(private val containerNode: YogaNode) : MeasurePolicy {
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

        Log.d("YogaMeasureContainer", "FLEX: Measuring container: $constraints")

        containerNode.calculateLayout(maxWidth, maxHeight)

        val measuredWidth = containerNode.layoutWidth.roundToInt()
        val measuredHeight = containerNode.layoutHeight.roundToInt()

        Log.d("YogaMeasureContainer", "FLEX: Measured container: ($measuredWidth, $measuredHeight)")

        // Apply the calculated size to the container
        return layout(measuredWidth, measuredHeight) {
            placeChildren()
        }
    }

    private fun syncToYoga(measurables: List<Measurable>) {
        // Only handle views that have the .flex() modifier
        val composeViews = measurables.filter { it.parentData is FlexNodeData }
        // Cast the parentData to FlexNodeData and cache the results for use later
        val yogaNodes = composeViews.map { (it.parentData as FlexNodeData).node }

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
                node.data = composeViews[index]
            }
        }
    }

    private fun Placeable.PlacementScope.placeChildren() {
        for (index in 0 until containerNode.childCount) {
            val node = containerNode.getChildAt(index)
            val measurable = node.data as? Measurable ?: continue

            val paddingStart = node.getLayoutPadding(YogaEdge.START)
            val paddingEnd = node.getLayoutPadding(YogaEdge.END)
            val paddingTop = node.getLayoutPadding(YogaEdge.TOP)
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

            Log.d("YogaMeasureContainer", "FLEX: Place[$index]: ($nodeX, $nodeY)")

            measurable.measure(childConstraints).place(nodeX, nodeY)
        }
    }
}