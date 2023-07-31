package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.types.FlexUnit

fun Modifier.flexWidth(dp: Dp) = flexboxParentData { density ->
    style.width = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexWidth(percent: Float) = flexboxParentData {
    style.width = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexWidth(type: FlexUnit) = flexboxParentData {
    style.width = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexHeight(dp: Dp) = flexboxParentData { density ->
    style.height = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexHeight(percent: Float) = flexboxParentData {
    style.height = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexHeight(type: FlexUnit) = flexboxParentData {
    style.height = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexMinWidth(dp: Dp) = flexboxParentData { density ->
    style.minWidth = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexMinWidth(percent: Float) = flexboxParentData {
    style.minWidth = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexMinWidth(type: FlexUnit) = flexboxParentData {
    style.minWidth = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexMinHeight(dp: Dp) = flexboxParentData { density ->
    style.minHeight = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexMinHeight(percent: Float) = flexboxParentData {
    style.minHeight = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexMinHeight(type: FlexUnit) = flexboxParentData {
    style.minHeight = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexMaxWidth(dp: Dp) = flexboxParentData { density ->
    style.maxWidth = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexMaxWidth(percent: Float) = flexboxParentData {
    style.maxWidth = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexMaxWidth(type: FlexUnit) = flexboxParentData {
    style.maxWidth = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexMaxHeight(dp: Dp) = flexboxParentData { density ->
    style.maxHeight = YogaValue(dp.value * density.density, YogaUnit.POINT)
}

fun Modifier.flexMaxHeight(percent: Float) = flexboxParentData {
    style.maxHeight = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexMaxHeight(type: FlexUnit) = flexboxParentData {
    style.maxHeight = YogaValue(Float.NaN, type.yogaValue)
}
