package net.obsidianx.chakra.types

import com.facebook.yoga.YogaOverflow

enum class FlexOverflow(val yogaValue: YogaOverflow) {
    Visible(YogaOverflow.VISIBLE),
    Hidden(YogaOverflow.HIDDEN),
    Scroll(YogaOverflow.SCROLL)
}
