package net.obsidianx.chakra.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import net.obsidianx.chakra.types.FlexNodeData

internal fun Modifier.flexboxParentData(block: FlexNodeData.(Density) -> Unit = {}) =
    composed {
        val density: Density = LocalDensity.current
        object : ParentDataModifier {
            override fun Density.modifyParentData(parentData: Any?) =
                ((parentData as? FlexNodeData) ?: FlexNodeData()).apply { block(density) }
        }
    }
