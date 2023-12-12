package net.obsidianx.chakra.types

import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue

enum class FlexUnit(val yogaValue: YogaUnit) {
    Auto(YogaUnit.AUTO),
    Undefined(YogaUnit.UNDEFINED);

    val toYogaValue: YogaValue
        get() = YogaValue(0f, yogaValue)
}
