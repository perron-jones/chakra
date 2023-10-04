@file:OptIn(ExperimentalComposeUiApi::class)

package net.obsidianx.chakra.modifiers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.Density
import net.obsidianx.chakra.FlexboxStyleScope
import net.obsidianx.chakra.ModifierLocalDebugDumpFlags
import net.obsidianx.chakra.types.FlexNodeData

fun Modifier.flex(block: FlexboxStyleScope.() -> Unit = {}) =
    composed {
        var nodeData by remember { mutableStateOf<FlexNodeData?>(null) }
        modifierLocalConsumer {
            nodeData?.debugDumpFlags = ModifierLocalDebugDumpFlags.current
        }.then(
            object : ParentDataModifier {
                override fun Density.modifyParentData(parentData: Any?): Any? {
                    if (nodeData == null) {
                        nodeData = (parentData as? FlexNodeData) ?: FlexNodeData()
                    }
                    return nodeData?.apply {
                        block(FlexboxStyleScope(this@modifyParentData, this))
                    }
                }
            }
        )
    }

fun Modifier.flexContainer() = flex {
    nodeData.isContainer = true
}