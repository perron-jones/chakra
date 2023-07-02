package net.obsidianx.chakra.types

import com.facebook.yoga.YogaPositionType

enum class FlexPositionType(val yogaValue: YogaPositionType) {
    Static(YogaPositionType.STATIC),
    Relative(YogaPositionType.RELATIVE),
    Absolute(YogaPositionType.ABSOLUTE)
}