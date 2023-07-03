package net.obsidianx.chakra

import androidx.compose.ui.Modifier
import net.obsidianx.chakra.layout.flexInternal

class FlexLayoutScope {
    fun Modifier.flex(style: FlexStyle = FlexStyle()) = then(flexInternal(style))
}