package net.obsidianx.chakra.layout

import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaMeasureMode
import com.facebook.yoga.YogaMeasureOutput
import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.types.FlexNodeData
import kotlin.math.min

fun measureNode(
    node: YogaNode,
    width: Float,
    widthMode: YogaMeasureMode,
    height: Float,
    heightMode: YogaMeasureMode
): Long {
    val placeable = (node.data as? FlexNodeData)?.placeable
        ?: return 0

    val paddingStart = node.getLayoutPadding(YogaEdge.START)
    val paddingTop = node.getLayoutPadding(YogaEdge.TOP)
    val paddingEnd = node.getLayoutPadding(YogaEdge.END)
    val paddingBottom = node.getLayoutPadding(YogaEdge.BOTTOM)

    val intrinsicWidth = placeable.width + paddingStart + paddingEnd
    val intrinsicHeight = placeable.height + paddingTop + paddingBottom

    val measuredWidth = reconcile(widthMode, width, intrinsicWidth)
    val measuredHeight = reconcile(heightMode, height, intrinsicHeight)

    return YogaMeasureOutput.make(measuredWidth, measuredHeight)
}

private fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicSize: Float): Float =
    when (mode) {
        YogaMeasureMode.UNDEFINED -> intrinsicSize
        YogaMeasureMode.EXACTLY -> size.takeIf { it != 0f } ?: intrinsicSize
        YogaMeasureMode.AT_MOST -> min(size, intrinsicSize)
    }
