package net.obsidianx.chakra.types

import com.facebook.yoga.YogaAlign

enum class FlexAlign(val yogaValue: YogaAlign) {
    Auto(YogaAlign.AUTO),
    Start(YogaAlign.FLEX_START),
    Center(YogaAlign.CENTER),
    End(YogaAlign.FLEX_END),
    Stretch(YogaAlign.STRETCH),
    Baseline(YogaAlign.BASELINE),
    SpaceBetween(YogaAlign.SPACE_BETWEEN),
    SpaceAround(YogaAlign.SPACE_AROUND)
}
