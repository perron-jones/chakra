package net.obsidianx.chakra

import androidx.compose.ui.unit.Density
import net.obsidianx.chakra.layout.FlexLayoutState
import net.obsidianx.chakra.types.FlexNodeData
import net.obsidianx.chakra.types.FlexboxStyle

class FlexboxStyleScope(val density: Density, val nodeData: FlexNodeData)

class FlexboxScope(private val layoutState: FlexLayoutState) {
    val parentLayoutState: FlexLayoutState
        get() = layoutState
}