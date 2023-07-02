package net.obsidianx.chakra.measure

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import com.facebook.yoga.YogaMeasureMode
import com.facebook.yoga.YogaMeasureOutput
import com.facebook.yoga.YogaNode
import kotlin.math.min
import kotlin.math.roundToInt

fun measureNode(
    node: YogaNode,
    width: Float,
    widthMode: YogaMeasureMode,
    height: Float,
    heightMode: YogaMeasureMode
): Long {
    val measurable = node.data as? Measurable ?: return 0

    val maxWidth = width.takeIf { !it.isNaN() }?.roundToInt() ?: Constraints.Infinity
    val maxHeight = height.takeIf { !it.isNaN() }?.roundToInt() ?: Constraints.Infinity

    val intrinsicWidth = measurable.minIntrinsicWidth(maxHeight)
    val intrinsicHeight = measurable.minIntrinsicHeight(maxWidth)

    val measuredWidth = reconcile(widthMode, width, intrinsicWidth.toFloat())
    val measuredHeight = reconcile(heightMode, height, intrinsicHeight.toFloat())

    return YogaMeasureOutput.make(measuredWidth, measuredHeight)
}

private fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicSize: Float): Float =
    when (mode) {
        YogaMeasureMode.UNDEFINED -> intrinsicSize
        YogaMeasureMode.EXACTLY -> size
        YogaMeasureMode.AT_MOST -> min(size, intrinsicSize)
    }