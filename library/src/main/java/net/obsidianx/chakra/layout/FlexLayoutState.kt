package net.obsidianx.chakra.layout

import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.types.FlexboxStyle

data class FlexLayoutState(
    var parent: YogaNode? = null,
    var childIndex: Int = 0,
    var remeasure: Boolean = false,
    var layoutComplete: Boolean = false,
    var childNode: YogaNode? = null,
    var selfNode: YogaNode? = null,
    var originalStyle: FlexboxStyle = FlexboxStyle(),
)
