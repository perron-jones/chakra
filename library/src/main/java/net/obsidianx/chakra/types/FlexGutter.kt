package net.obsidianx.chakra.types

import com.facebook.yoga.YogaGutter

enum class FlexGutter(val yogaValue: YogaGutter) {
    All(YogaGutter.ALL),
    Column(YogaGutter.COLUMN),
    Row(YogaGutter.ROW),
}
