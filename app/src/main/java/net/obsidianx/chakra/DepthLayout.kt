package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.alignItems
import net.obsidianx.chakra.modifiers.fitMinContent
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.justifyContent
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.modifiers.positionType
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

@Composable
fun DepthLayout() {
    Flexbox(
        modifier = Modifier
            .flex {
                fitMinContent()
                debugTag("outer")
                debugDump()
                width(300.dp)
                height(300.dp)
                alignItems(FlexAlign.Center)
                justifyContent(FlexJustify.Center)
            }
            .border(1.dp, Color.Red)
    ) {
        Flexbox(
            modifier = Modifier
                .flex {
                    fitMinContent()
                    debugTag("inner")
                    alignItems(FlexAlign.Center)
                    justifyContent(FlexJustify.Center)
                    padding(16.dp)
                    positionType(FlexPositionType.Absolute)
                }
                .border(1.dp, Color.Green)
        ) {
            Text(
                "Hello",
                modifier = Modifier
                    .border(1.dp, Color.Blue)
                    .flex {
                        debugTag("text")
                        positionType(FlexPositionType.Absolute)
                    }
            )
        }
    }
}