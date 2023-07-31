package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.types.FlexEdges

fun Modifier.flexMargin(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) =
    flexboxParentData { density ->
        style.margin = FlexEdges(
            start = YogaValue(start.value * density.density, YogaUnit.POINT),
            top = YogaValue(top.value * density.density, YogaUnit.POINT),
            end = YogaValue(end.value * density.density, YogaUnit.POINT),
            bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexMargin(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    flexboxParentData { density ->
        style.margin = FlexEdges(
            horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
            vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexMargin(all: Dp = 0.dp) =
    flexboxParentData { density ->
        style.margin = FlexEdges(
            all = YogaValue(all.value * density.density, YogaUnit.POINT)
        )
    }

fun Modifier.flexPadding(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) =
    flexboxParentData { density ->
        style.padding = FlexEdges(
            start = YogaValue(start.value * density.density, YogaUnit.POINT),
            top = YogaValue(top.value * density.density, YogaUnit.POINT),
            end = YogaValue(end.value * density.density, YogaUnit.POINT),
            bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexPadding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    flexboxParentData { density ->
        style.padding = FlexEdges(
            horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
            vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexPadding(all: Dp = 0.dp) =
    flexboxParentData { density ->
        style.padding = FlexEdges(
            all = YogaValue(all.value * density.density, YogaUnit.POINT)
        )
    }

fun Modifier.flexBorder(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) =
    flexboxParentData { density ->
        style.border = FlexEdges(
            start = YogaValue(start.value * density.density, YogaUnit.POINT),
            top = YogaValue(top.value * density.density, YogaUnit.POINT),
            end = YogaValue(end.value * density.density, YogaUnit.POINT),
            bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexBorder(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    flexboxParentData { density ->
        style.border = FlexEdges(
            horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
            vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexBorder(all: Dp = 0.dp) =
    flexboxParentData { density ->
        style.border = FlexEdges(
            all = YogaValue(all.value * density.density, YogaUnit.POINT)
        )
    }

fun Modifier.flexPosition(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) =
    flexboxParentData { density ->
        style.position = FlexEdges(
            start = YogaValue(start.value * density.density, YogaUnit.POINT),
            top = YogaValue(top.value * density.density, YogaUnit.POINT),
            end = YogaValue(end.value * density.density, YogaUnit.POINT),
            bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexPosition(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    flexboxParentData { density ->
        style.position = FlexEdges(
            horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
            vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
        )
    }

fun Modifier.flexPosition(all: Dp = 0.dp) =
    flexboxParentData { density ->
        style.position = FlexEdges(
            all = YogaValue(all.value * density.density, YogaUnit.POINT)
        )
    }
