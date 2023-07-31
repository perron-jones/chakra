package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import net.obsidianx.chakra.types.FlexDisplay
import net.obsidianx.chakra.types.FlexOverflow
import net.obsidianx.chakra.types.FlexPositionType

fun Modifier.flexDisplay(display: FlexDisplay) = flexboxParentData {
    style.display = display.yogaValue
}

fun Modifier.flexOverflow(overflow: FlexOverflow) = flexboxParentData {
    style.overflow = overflow.yogaValue
}

fun Modifier.flexAspectRatio(aspectRatio: Float? = null) = flexboxParentData {
    style.aspectRatio = aspectRatio
}

fun Modifier.flexPositionType(positionType: FlexPositionType) = flexboxParentData {
    style.positionType = positionType.yogaValue
}

fun Modifier.flexDebugTag(tag: String) = flexboxParentData {
    debugTag = tag
}