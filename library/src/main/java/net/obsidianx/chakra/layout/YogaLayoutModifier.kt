package net.obsidianx.chakra.layout

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

internal fun Modifier.yogaLayout() = then(layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val width = placeable.width
    val height = placeable.height
    layout(width, height) {
        placeable.place(0, 0)
    }
})