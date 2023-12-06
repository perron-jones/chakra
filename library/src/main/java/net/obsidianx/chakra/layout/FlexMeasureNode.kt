package net.obsidianx.chakra.layout

import android.util.Log
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaMeasureMode
import com.facebook.yoga.YogaMeasureOutput
import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.Chakra
import net.obsidianx.chakra.types.FlexNodeData
import kotlin.math.min

fun measureNode(
    node: YogaNode,
    width: Float,
    widthMode: YogaMeasureMode,
    height: Float,
    heightMode: YogaMeasureMode
): Long {
    val nodeData = node.data as? FlexNodeData ?: return 0

    val paddingStart = node.getPadding(YogaEdge.START).asFloatOrZero
    val paddingTop = node.getPadding(YogaEdge.TOP).asFloatOrZero
    val paddingEnd = node.getPadding(YogaEdge.END).asFloatOrZero
    val paddingBottom = node.getPadding(YogaEdge.BOTTOM).asFloatOrZero

    val intrinsicMinWidth = nodeData.minWidth + paddingStart + paddingEnd
    val intrinsicMinHeight = nodeData.minHeight + paddingTop + paddingBottom
    val intrinsicMaxWidth = nodeData.maxWidth + paddingStart + paddingEnd
    val intrinsicMaxHeight = nodeData.maxHeight + paddingTop + paddingBottom

    val measuredWidth = if (Chakra.isTextWrappingEnabled) {
        reconcile(widthMode, width, intrinsicMinWidth, intrinsicMaxWidth)
    } else {
        reconcile(widthMode, width, intrinsicMinWidth)
    }
    val measuredHeight = if (Chakra.isTextWrappingEnabled) {
        reconcile(heightMode, height, intrinsicMinHeight, intrinsicMaxHeight)
    } else {
        reconcile(heightMode, height, intrinsicMinHeight)
    }
    val tag = (node.data as? FlexNodeData)?.debugTag ?: "??"
    log(
        "tag: $tag " +
                "provided: ($width, $height) " +
                "intrinsic min: ($intrinsicMinWidth, $intrinsicMinHeight) " +
                "intrinsic max: ($intrinsicMaxWidth, $intrinsicMaxHeight) " +
                "measured: ($measuredWidth, $measuredHeight) " +
                "width mode: $widthMode " +
                "height mode $heightMode " +
                "is Height Nan: ${height.isNaN()} " +
                "is Height Infinite: ${height.isInfinite()}"
    )

    return YogaMeasureOutput.make(measuredWidth, measuredHeight)
}

private fun log(msg: String) {
    if (Chakra.debugLogging) {
        Log.d("Chakra", "[measureNode] $msg")
    }
}

private fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicSize: Float): Float =
    when (mode) {
        YogaMeasureMode.UNDEFINED -> intrinsicSize
        YogaMeasureMode.EXACTLY -> size.takeIf { it != 0f } ?: intrinsicSize
        YogaMeasureMode.AT_MOST -> min(size, intrinsicSize)
    }

private fun reconcile(mode: YogaMeasureMode, size: Float, intrinsicMinSize: Float, intrinsicMaxSize: Float): Float =
    when (mode) {
        YogaMeasureMode.UNDEFINED -> if(intrinsicMaxSize.isFinite()) intrinsicMaxSize else intrinsicMinSize
        YogaMeasureMode.EXACTLY -> size.takeIf { it != 0f } ?: intrinsicMinSize
        YogaMeasureMode.AT_MOST -> size.coerceAtMost(intrinsicMaxSize)
    }
