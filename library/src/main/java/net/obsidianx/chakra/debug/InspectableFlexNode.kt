package net.obsidianx.chakra.debug

import androidx.compose.ui.platform.debugInspectorInfo
import net.obsidianx.chakra.FlexStyle

internal fun flexNodeInspectorInfo(style: FlexStyle) =
    debugInspectorInfo {
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
    }