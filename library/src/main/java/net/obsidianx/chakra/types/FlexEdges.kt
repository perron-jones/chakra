package net.obsidianx.chakra.types

import androidx.compose.ui.platform.ValueElementSequence
import com.facebook.yoga.YogaEdge

data class FlexEdges(
    val left: FlexValue = FlexValue.Undefined,
    val top: FlexValue = FlexValue.Undefined,
    val right: FlexValue = FlexValue.Undefined,
    val bottom: FlexValue = FlexValue.Undefined,
    val start: FlexValue = FlexValue.Undefined,
    val end: FlexValue = FlexValue.Undefined,
    val horizontal: FlexValue = FlexValue.Undefined,
    val vertical: FlexValue = FlexValue.Undefined,
    val all: FlexValue = FlexValue.Undefined,
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
        setValue: (edge: YogaEdge, value: Float) -> Unit,
        setPercent: ((edge: YogaEdge, value: Float) -> Unit)? = null,
        setAuto: ((edge: YogaEdge) -> Unit)? = null
    ) {
        typeMap.entries.forEach { edge ->
            when (edge.value) {
                is FlexValue.Value -> setValue(
                    edge.key,
                    (edge.value as FlexValue.Value).value
                )

                is FlexValue.Percent -> setPercent?.invoke(
                    edge.key,
                    (edge.value as FlexValue.Percent).value
                )

                is FlexValue.Auto -> setAuto?.invoke(edge.key) ?: setValue.invoke(
                    edge.key,
                    Float.NaN
                )
            }
        }
    }

    fun inspectorProps(type: String, properties: ValueElementSequence) {
        var hasValue = false
        typeMap.entries.forEach { edge ->
            if (edge.value !is FlexValue.Undefined) {
                val edgeName = edge.key.toString()
                    .lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
                properties["$type$edgeName"] = edge.value.toString()
                hasValue = true
            }
        }
        if (!hasValue) {
            properties[type] = "Undefined"
        }
    }
}