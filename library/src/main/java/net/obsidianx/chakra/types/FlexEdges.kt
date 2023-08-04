package net.obsidianx.chakra.types

import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.layout.isSet

data class FlexEdges(
    val left: YogaValue = YOGA_UNDEFINED,
    val top: YogaValue = YOGA_UNDEFINED,
    val right: YogaValue = YOGA_UNDEFINED,
    val bottom: YogaValue = YOGA_UNDEFINED,
    val start: YogaValue = YOGA_UNDEFINED,
    val end: YogaValue = YOGA_UNDEFINED,
    val horizontal: YogaValue = YOGA_UNDEFINED,
    val vertical: YogaValue = YOGA_UNDEFINED,
    val all: YogaValue = YOGA_UNDEFINED,
) {
    private val typeMap = mapOf(
        YogaEdge.LEFT to left,
        YogaEdge.TOP to top,
        YogaEdge.RIGHT to right,
        YogaEdge.BOTTOM to bottom,
        YogaEdge.START to start,
        YogaEdge.END to end,
        YogaEdge.HORIZONTAL to horizontal,
        YogaEdge.VERTICAL to vertical,
        YogaEdge.ALL to all,
    )

    fun apply(
        setPoint: (YogaEdge, Float) -> Unit,
        setPercent: ((YogaEdge, Float) -> Unit)? = null,
        setAuto: ((YogaEdge) -> Unit)? = null,
    ) {
        val fallback: (YogaEdge) -> Unit = { edge -> setPoint.invoke(edge, Float.NaN) }

        typeMap.entries.forEach { edge ->
            when (edge.value.unit) {
                YogaUnit.POINT -> setPoint(edge.key, edge.value.value)
                YogaUnit.PERCENT -> setPercent?.invoke(edge.key, edge.value.value)
                    ?: fallback(edge.key)

                YogaUnit.AUTO -> setAuto?.invoke(edge.key)
                else -> fallback(edge.key)
            }
        }
    }

    fun getHorizontal(): Float {
        val horiz = horizontal.takeIf { it.isSet }?.value ?: all.takeIf { it.isSet }?.value ?: 0f
        val startEdge = start.takeIf { it.isSet }?.value ?: horiz
        val endEdge = end.takeIf { it.isSet }?.value ?: horiz
        return startEdge + endEdge
    }

    fun getVertical(): Float {
        val vert = vertical.takeIf { it.isSet }?.value ?: all.takeIf { it.isSet }?.value ?: 0f
        val topEdge = top.takeIf { it.isSet }?.value ?: vert
        val bottomEdge = bottom.takeIf { it.isSet }?.value ?: vert
        return topEdge + bottomEdge
    }
}
