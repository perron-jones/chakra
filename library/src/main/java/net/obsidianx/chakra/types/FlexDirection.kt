package net.obsidianx.chakra.types

import com.facebook.yoga.YogaFlexDirection

enum class FlexDirection(val yogaValue: YogaFlexDirection) {
    Row(YogaFlexDirection.ROW),
    RowReverse(YogaFlexDirection.ROW_REVERSE),
    Column(YogaFlexDirection.COLUMN),
    ColumnReverse(YogaFlexDirection.COLUMN_REVERSE)
}

val YogaFlexDirection.isRow
    get() = this == YogaFlexDirection.ROW || this == YogaFlexDirection.ROW_REVERSE

val YogaFlexDirection.isColumn
    get() = this == YogaFlexDirection.COLUMN || this == YogaFlexDirection.COLUMN_REVERSE