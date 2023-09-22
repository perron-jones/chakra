package net.obsidianx.chakra.modifiers

import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify

fun FlexboxStyleScope.alignItems(align: FlexAlign) {
    nodeData.style.alignItems = align.yogaValue
}

fun FlexboxStyleScope.alignContent(align: FlexAlign) {
    nodeData.style.alignContent = align.yogaValue
}

fun FlexboxStyleScope.alignSelf(align: FlexAlign) {
    nodeData.style.alignSelf = align.yogaValue
}

fun FlexboxStyleScope.justifyContent(justify: FlexJustify) {
    nodeData.style.justifyContent = justify.yogaValue
}
