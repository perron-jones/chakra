package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify

fun Modifier.flexAlignItems(align: FlexAlign) = flexboxParentData {
    style.alignItems = align.yogaValue
}

fun Modifier.flexAlignContent(align: FlexAlign) = flexboxParentData {
    style.alignContent = align.yogaValue
}

fun Modifier.flexAlignSelf(align: FlexAlign) = flexboxParentData {
    style.alignSelf = align.yogaValue
}

fun Modifier.flexJustifyContent(justify: FlexJustify) = flexboxParentData {
    style.justifyContent = justify.yogaValue
}
