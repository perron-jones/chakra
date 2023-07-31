package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import com.facebook.yoga.YogaWrap
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexGap
import net.obsidianx.chakra.types.FlexGutter
import net.obsidianx.chakra.types.FlexUnit

fun Modifier.flexDirection(direction: FlexDirection) = flexboxParentData {
    style.flexDirection = direction.yogaValue
}

fun Modifier.flex(value: Float) = flexboxParentData {
    style.flex = value
}

fun Modifier.flexGrow(value: Float) = flexboxParentData {
    style.flexGrow = value
}

fun Modifier.flexShrink(value: Float) = flexboxParentData {
    style.flexShrink = value
}

fun Modifier.flexBasis(dp: Dp) = flexboxParentData {
    style.flexBasis = YogaValue(dp.value, YogaUnit.POINT)
}

fun Modifier.flexBasis(percent: Float) = flexboxParentData {
    style.flexBasis = YogaValue(percent, YogaUnit.PERCENT)
}

fun Modifier.flexBasis(type: FlexUnit) = flexboxParentData {
    style.flexBasis = YogaValue(Float.NaN, type.yogaValue)
}

fun Modifier.flexWrap(wrap: Boolean, reverse: Boolean = false) = flexboxParentData {
    style.flexWrap = if (wrap) {
        if (reverse) {
            YogaWrap.WRAP_REVERSE
        } else {
            YogaWrap.WRAP
        }
    } else {
        YogaWrap.NO_WRAP
    }
}

fun Modifier.flexGap(all: Dp? = null, horizontal: Dp? = null, vertical: Dp? = null): Modifier {
    if (listOf(all, horizontal, vertical).count { it != null } > 1) {
        throw IllegalArgumentException("Only set one kind of gap in Modifier.flexGap")
    }
    return flexboxParentData { density ->
        val gutter = when {
            vertical != null -> FlexGutter.Row
            horizontal != null -> FlexGutter.Column
            else -> FlexGutter.All
        }
        style.gap = FlexGap(gutter, (all?.value ?: 0f) * density.density)
    }
}
