package net.obsidianx.chakra.modifiers

import androidx.compose.ui.unit.Dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.types.FlexUnit

fun FlexboxStyleScope.width(dp: Dp) {
    nodeData.style.width = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.width(percent: Float) {
    nodeData.style.width = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.width(type: FlexUnit) {
    nodeData.style.width = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.height(dp: Dp) {
    nodeData.style.height = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.height(percent: Float) {
    nodeData.style.height = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.height(type: FlexUnit) {
    nodeData.style.height = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.minWidth(dp: Dp) {
    nodeData.style.minWidth = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.minWidth(percent: Float) {
    nodeData.style.minWidth = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.minWidth(type: FlexUnit) {
    nodeData.style.minWidth = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.minHeight(dp: Dp) {
    nodeData.style.minHeight = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.minHeight(percent: Float) {
    nodeData.style.minHeight = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.minHeight(type: FlexUnit) {
    nodeData.style.minHeight = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.maxWidth(dp: Dp) {
    nodeData.style.maxWidth = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.maxWidth(percent: Float) {
    nodeData.style.maxWidth = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.maxWidth(type: FlexUnit) {
    nodeData.style.maxWidth = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.maxHeight(dp: Dp) {
    nodeData.style.maxHeight = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun FlexboxStyleScope.maxHeight(percent: Float) {
    nodeData.style.maxHeight = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.maxHeight(type: FlexUnit) {
    nodeData.style.maxHeight = YogaValue(Float.NaN, type.yogaValue)
}
