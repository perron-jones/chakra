package net.obsidianx.chakra.debug

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import net.obsidianx.chakra.FlexStyle

internal fun Modifier.inspectableFlexContainer(style: FlexStyle, factory: Modifier.() -> Modifier) =
    inspectable(inspectorInfo = debugInspectorInfo {
        style.run {
            name = "FlexContainer"
            properties["alignContent"] = alignContent.toString()
            properties["alignItems"] = alignItems.toString()
            properties["direction"] = flexDirection.toString()
            properties["justifyContent"] = justifyContent.toString()
            properties["wrap"] = wrap.toString()
        }
    }) { factory() }