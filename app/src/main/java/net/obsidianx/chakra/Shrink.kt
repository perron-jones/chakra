package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.debug.flexDebugDump
import net.obsidianx.chakra.debug.flexDebugTag
import net.obsidianx.chakra.modifiers.flexDirection
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexShrink
import net.obsidianx.chakra.modifiers.flexWidth
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun FlexboxScope.GreenBox(content: @Composable FlexboxScope.() -> Unit) {
    Flexbox(
        modifier = Modifier
            .flexWidth(100.dp)
            .flexHeight(100.dp)
            .flexDirection(FlexDirection.Row)
            .flexDebugTag("GreenBox")
            .background(Color.Green)
    ) {
        content()
    }
}

@Composable
fun FlexboxScope.BlueBox(content: (@Composable FlexboxScope.() -> Unit)? = null) {
    Flexbox(
        modifier = Modifier
            .flexWidth(40.dp)
            .flexHeight(90.dp)
            .flexDirection(FlexDirection.Row)
            .flexShrink(1f)
            .flexDebugTag("BlueBox")
            .background(Color.Blue)
            .border(width = 1.dp, color = Color.Yellow)
    ) {
        content?.invoke(this)
    }
}

@Composable
fun FlexboxScope.MagentaBox(content: @Composable FlexboxScope.() -> Unit = {}) {
    Flexbox(
        modifier = Modifier
            .flexWidth(20.dp)
            .flexHeight(80.dp)
            .flexDirection(FlexDirection.Row)
            .flexShrink(1f)
            .flexDebugTag("MagentaBox")
            .background(Color.Magenta)
            .border(width = 1.dp, color = Color.Gray)
    ) { content() }
}

@Composable
fun Shrink() {
    Row {
        Flexbox(
            Modifier
                .flexDirection(FlexDirection.Column)
                .flexDebugDump(flags = DebugDumpFlag.ALL_SET)
        ) {
            repeat(4) { green ->
                GreenBox {
                    repeat(green) {
                        BlueBox {
                            repeat(green) {
                                MagentaBox {
                                    Text("Hi", softWrap = false, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}