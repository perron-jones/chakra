package net.obsidianx.chakra.types

import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.debug.DebugDumpFlag

data class FlexNodeData(
    var style: FlexboxStyle = FlexboxStyle(),
    var debugTag: String = "",
    var debugDumpFlags: Set<DebugDumpFlag>? = null,
    var debugLogTag: String? = null,
    var layoutNode: YogaNode? = null,
    var fitMinContent: Boolean = false,
    var isContainer: Boolean = false,
    var minWidth: Float = 0f,
    var minHeight: Float = 0f,
    var maxWidth: Float = Float.MAX_VALUE,
    var maxHeight: Float = Float.MAX_VALUE,
)
