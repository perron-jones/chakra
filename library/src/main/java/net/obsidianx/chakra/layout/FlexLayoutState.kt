package net.obsidianx.chakra.layout

import com.facebook.yoga.YogaNode

data class FlexLayoutState(
    var parent: YogaNode? = null,
    var childIndex: Int = 0,
    var remeasure: Boolean = false,
    var layoutComplete: Boolean = false,
)
