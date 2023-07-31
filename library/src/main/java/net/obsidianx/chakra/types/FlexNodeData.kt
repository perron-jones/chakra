package net.obsidianx.chakra.types

import androidx.compose.ui.layout.Placeable
import com.facebook.yoga.YogaNode

data class FlexNodeData(
    val style: FlexboxStyle = FlexboxStyle(),
    var debugTag: String = "",
    var layoutNode: YogaNode? = null,
    var placeable: Placeable? = null
)
