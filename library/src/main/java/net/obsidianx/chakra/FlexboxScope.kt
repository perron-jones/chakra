package net.obsidianx.chakra

import net.obsidianx.chakra.layout.FlexLayoutState

class FlexboxScope(private val layoutState: FlexLayoutState) {
    val parentLayoutState: FlexLayoutState
        get() = layoutState
}