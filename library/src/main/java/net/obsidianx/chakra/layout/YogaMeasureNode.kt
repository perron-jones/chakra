package net.obsidianx.chakra.layout

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import com.facebook.yoga.YogaMeasureMode
import com.facebook.yoga.YogaMeasureOutput
import com.facebook.yoga.YogaNode
import kotlin.math.min

fun measureNode(
    node: YogaNode,
    width: Float,
    widthMode: YogaMeasureMode,
    height: Float,
    heightMode: YogaMeasureMode
): Long {
    val measurable = (node.data as? YogaNode)?.data as? Measurable
        ?: node.data as? Measurable
        ?: return 0

    val maxWidth = width.takeIf { !it.isNaN() } ?: Constraints.Infinity
    val maxHeight = height.takeIf { !it.isNaN() } ?: Constraints.Infinity

    val intrinsicWidth = measurable.maxIntrinsicWidth(maxHeight.toInt())
    val intrinsicHeight = measurable.maxIntrinsicHeight(maxWidth.toInt())

    val measuredWidth = reconcile(widthMode, width, intrinsicWidth.toFloat())
    val measuredHeight = reconcile(heightMode, height, intrinsicHeight.toFloat())

    return YogaMeasureOutput.make(measuredWidth, measuredHeight)
}

private fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicSize: Float): Float =
    when (mode) {
        YogaMeasureMode.UNDEFINED -> intrinsicSize
        YogaMeasureMode.EXACTLY -> size.takeIf { it != 0f } ?: intrinsicSize
        YogaMeasureMode.AT_MOST -> min(size, intrinsicSize)
    }