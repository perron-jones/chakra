package net.obsidianx.chakra.types

import androidx.compose.ui.layout.Placeable
import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.debug.DebugDumpFlag

data class FlexNodeData(
    var style: FlexboxStyle = FlexboxStyle(),
    var debugTag: String = "",
    var debugDumpFlags: Set<DebugDumpFlag>? = null,
    var debugLogTag: String? = null,
    var layoutNode: YogaNode? = null,
    var placeable: Placeable? = null,
    var fitMinContent: Boolean = false,
    var isContainer: Boolean = false,
)
