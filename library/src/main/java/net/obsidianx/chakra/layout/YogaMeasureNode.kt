package net.obsidianx.chakra.layout

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.facebook.yoga.YogaMeasureMode
import com.facebook.yoga.YogaMeasureOutput
import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.types.FlexNodeData

internal fun measureFlexNode(node: YogaNode, width: Float, widthMode: YogaMeasureMode, height: Float, heightMode: YogaMeasureMode): Long {
    fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicMax: Float): Float =
        when (mode) {
            YogaMeasureMode.UNDEFINED -> intrinsicMax
            YogaMeasureMode.EXACTLY -> size.takeIf { it != 0f } ?: intrinsicMax
            YogaMeasureMode.AT_MOST -> intrinsicMax.coerceAtMost(size)
        }

    val nodeData = node.data as FlexNodeData

    val intrinsicMax = nodeData.intrinsicMax.toFloatSize()

    return YogaMeasureOutput.make(
        reconcile(widthMode, width, intrinsicMax.width),
        reconcile(heightMode, height, intrinsicMax.height),
    )
}

private fun IntSize?.toFloatSize(): Size {
    return this?.run {
        Size(width.toFloat(), height.toFloat())
    } ?: Size.Zero
}
