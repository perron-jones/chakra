package net.obsidianx.chakra.types

import com.facebook.yoga.YogaWrap

enum class FlexWrap(val yogaValue: YogaWrap) {
    NoWrap(YogaWrap.NO_WRAP),
    Wrap(YogaWrap.WRAP),
    WrapReverse(YogaWrap.WRAP_REVERSE)
}