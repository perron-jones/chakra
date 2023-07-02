package net.obsidianx.chakra

import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import net.obsidianx.chakra.measure.measureNode

class FlexLayoutScope {
    fun Modifier.flex(style: FlexStyle = FlexStyle()) =
        inspectable(inspectorInfo = debugInspectorInfo {
            name = "FlexNode"
            style.run {
                properties["flex"] = flex ?: "Undefined"
                properties["flexBasis"] = flexBasis.toString()
                properties["flexGrow"] = flexGrow ?: "Undefined"
                properties["flexShrink"] = flexShrink ?: "Undefined"

                properties["alignSelf"] = alignSelf.toString()
                properties["display"] = display.toString()
                properties["overflow"] = overflow.toString()
                properties["aspectRatio"] = aspectRatio ?: "Undefined"

                properties["width"] = width.toString()
                properties["height"] = height.toString()

                properties["minWidth"] = minWidth.toString()
                properties["minHeight"] = minHeight.toString()

                properties["maxWidth"] = maxWidth.toString()
                properties["maxHeight"] = maxHeight.toString()

                margin.inspectorProps("margin", properties)
                padding.inspectorProps("padding", properties)
                border.inspectorProps("border", properties)

                properties["positionType"] = positionType.toString()
                position.inspectorProps("position", properties)
            }
        }) {
            then(
                composed {
                    val nodeData = remember { FlexNodeData() }
                    style.apply(nodeData.node)
                    nodeData.node.setMeasureFunction(::measureNode)
                    nodeData
                }.layout { measurable, constraints ->
                    val (node) = measurable.parentData as FlexNodeData
                    node.dirty()
                    val placeable = measurable.measure(constraints)
                    Log.d("YogaMeasureNode", "COMPOSE: $constraints")
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
            )
        }
}