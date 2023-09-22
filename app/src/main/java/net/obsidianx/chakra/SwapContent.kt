package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.alignItems
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.fitMinContent
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.justifyContent
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify

@Composable
fun SwapContent() {
    Column {
        var showOne by remember { mutableStateOf(false) }
        Button(onClick = { showOne = !showOne }) {
            Text("Toggle")
        }

        Flexbox(
            modifier = Modifier
                .border(1.dp, Color.Magenta)
                .flex {
                    fitMinContent()
                    debugTag("outer")
                }) {
            if (showOne) {
                Flexbox(modifier = Modifier
                    .border(1.dp, Color.Red)
                    .flex {
                        direction(FlexDirection.Row)
                        alignItems(FlexAlign.End)
                        justifyContent(FlexJustify.End)
                        width(100.dp)
                        height(100.dp)
                        debugTag("inner-red")
                    }) {
                    Text("One")
                    Text("Two")
                }
            } else {
                Flexbox(modifier = Modifier
                    .flex { debugTag("inner-blue") }
                    .border(1.dp, Color.Blue)) {
                    Text("One")
                    Text("Two")
                }
            }
        }
    }
}