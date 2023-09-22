package net.obsidianx.chakra.modifiers

import androidx.compose.ui.unit.Dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import com.facebook.yoga.YogaWrap
import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexGap
import net.obsidianx.chakra.types.FlexGutter
import net.obsidianx.chakra.types.FlexUnit

fun FlexboxStyleScope.direction(direction: FlexDirection) {
    nodeData.style.flexDirection = direction.yogaValue
}

fun FlexboxStyleScope.flex(value: Float) {
    nodeData.style.flex = value
}

fun FlexboxStyleScope.grow(value: Float) {
    nodeData.style.flexGrow = value
}

fun FlexboxStyleScope.shrink(value: Float) {
    nodeData.style.flexShrink = value
}

fun FlexboxStyleScope.basis(dp: Dp) {
    nodeData.style.flexBasis = YogaValue(dp.value, YogaUnit.POINT)
}

fun FlexboxStyleScope.basis(percent: Float) {
    nodeData.style.flexBasis = YogaValue(percent, YogaUnit.PERCENT)
}

fun FlexboxStyleScope.basis(type: FlexUnit) {
    nodeData.style.flexBasis = YogaValue(Float.NaN, type.yogaValue)
}

fun FlexboxStyleScope.wrap(wrap: Boolean, reverse: Boolean = false) {
    nodeData.style.flexWrap = if (wrap) {
        if (reverse) {
            YogaWrap.WRAP_REVERSE
        } else {
            YogaWrap.WRAP
        }
    } else {
        YogaWrap.NO_WRAP
    }
}

fun FlexboxStyleScope.gap(all: Dp? = null, horizontal: Dp? = null, vertical: Dp? = null) {
    if (listOf(all, horizontal, vertical).count { it != null } > 1) {
        throw IllegalArgumentException("Only set one kind of gap in FlexboxStyleScope.gap")
    }
    val gutter = when {
        vertical != null -> FlexGutter.Row
        horizontal != null -> FlexGutter.Column
        else -> FlexGutter.All
    }
    nodeData.style.gap = FlexGap(gutter, (all?.value ?: 0f) * density.density)
}
