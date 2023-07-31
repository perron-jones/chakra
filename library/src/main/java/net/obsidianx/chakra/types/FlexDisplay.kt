package net.obsidianx.chakra.types

import com.facebook.yoga.YogaDisplay

enum class FlexDisplay(val yogaValue: YogaDisplay) {
    Flex(YogaDisplay.FLEX),
    None(YogaDisplay.NONE)
}
