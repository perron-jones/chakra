package net.obsidianx.chakra.types

import com.facebook.yoga.YogaJustify

enum class FlexJustify(val yogaValue: YogaJustify) {
    Start(YogaJustify.FLEX_START),
    Center(YogaJustify.CENTER),
    End(YogaJustify.FLEX_END),
    SpaceBetween(YogaJustify.SPACE_BETWEEN),
    SpaceAround(YogaJustify.SPACE_AROUND),
    SpaceEvenly(YogaJustify.SPACE_EVENLY)
}
