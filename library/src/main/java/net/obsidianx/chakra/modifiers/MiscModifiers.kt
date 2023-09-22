package net.obsidianx.chakra.modifiers

import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.types.FlexDisplay
import net.obsidianx.chakra.types.FlexOverflow
import net.obsidianx.chakra.types.FlexPositionType

fun FlexboxStyleScope.display(display: FlexDisplay) {
    nodeData.style.display = display.yogaValue
}

fun FlexboxStyleScope.overflow(overflow: FlexOverflow) {
    nodeData.style.overflow = overflow.yogaValue
}

fun FlexboxStyleScope.aspectRatio(aspectRatio: Float? = null) {
    nodeData.style.aspectRatio = aspectRatio
}

fun FlexboxStyleScope.positionType(positionType: FlexPositionType) {
    nodeData.style.positionType = positionType.yogaValue
}

fun FlexboxStyleScope.fitMinContent() {
    nodeData.fitMinContent = true
}