package net.obsidianx.chakra.modifiers

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.types.FlexEdges

fun FlexboxStyleScope.margin(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
    nodeData.style.margin = FlexEdges(
        start = YogaValue(start.value * density.density, YogaUnit.POINT),
        top = YogaValue(top.value * density.density, YogaUnit.POINT),
        end = YogaValue(end.value * density.density, YogaUnit.POINT),
        bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.margin(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
    nodeData.style.margin = FlexEdges(
        horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
        vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.margin(all: Dp = 0.dp) {
    nodeData.style.margin = FlexEdges(
        all = YogaValue(all.value * density.density, YogaUnit.POINT)
    )
}

fun FlexboxStyleScope.padding(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
    nodeData.style.padding = FlexEdges(
        start = YogaValue(start.value * density.density, YogaUnit.POINT),
        top = YogaValue(top.value * density.density, YogaUnit.POINT),
        end = YogaValue(end.value * density.density, YogaUnit.POINT),
        bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.padding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
    nodeData.style.padding = FlexEdges(
        horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
        vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.padding(all: Dp = 0.dp) {
    nodeData.style.padding = FlexEdges(
        all = YogaValue(all.value * density.density, YogaUnit.POINT)
    )
}

fun FlexboxStyleScope.border(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
    nodeData.style.border = FlexEdges(
        start = YogaValue(start.value * density.density, YogaUnit.POINT),
        top = YogaValue(top.value * density.density, YogaUnit.POINT),
        end = YogaValue(end.value * density.density, YogaUnit.POINT),
        bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.border(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
    nodeData.style.border = FlexEdges(
        horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
        vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.border(all: Dp = 0.dp) {
    nodeData.style.border = FlexEdges(
        all = YogaValue(all.value * density.density, YogaUnit.POINT)
    )
}

fun FlexboxStyleScope.position(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
) {
    nodeData.style.position = FlexEdges(
        start = YogaValue(start.value * density.density, YogaUnit.POINT),
        top = YogaValue(top.value * density.density, YogaUnit.POINT),
        end = YogaValue(end.value * density.density, YogaUnit.POINT),
        bottom = YogaValue(bottom.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.position(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
    nodeData.style.position = FlexEdges(
        horizontal = YogaValue(horizontal.value * density.density, YogaUnit.POINT),
        vertical = YogaValue(vertical.value * density.density, YogaUnit.POINT),
    )
}

fun FlexboxStyleScope.position(all: Dp = 0.dp) {
    nodeData.style.position = FlexEdges(
        all = YogaValue(all.value * density.density, YogaUnit.POINT)
    )
}
