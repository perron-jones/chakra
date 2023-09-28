package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.shrink
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun FlexboxScope.GreenBox(content: @Composable FlexboxScope.() -> Unit) {
    Flexbox(
        modifier = Modifier
            .flex {
                width(100.dp)
                height(100.dp)
                direction(FlexDirection.Row)
                debugTag("GreenBox")
                debugDump()
            }
            .background(Color.Green)
    ) {
        content()
    }
}

@Composable
fun FlexboxScope.BlueBox(content: (@Composable FlexboxScope.() -> Unit)? = null) {
    Flexbox(
        modifier = Modifier
            .flex {
                width(40.dp)
                height(90.dp)
                direction(FlexDirection.Row)
                shrink(1f)
                debugTag("BlueBox")
            }
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
            .flex {
                width(20.dp)
                height(80.dp)
                direction(FlexDirection.Row)
                shrink(1f)
                debugTag("MagentaBox")
            }
            .background(Color.Magenta)
            .border(width = 1.dp, color = Color.Gray)
    ) { content() }
}

@Composable
fun Shrink() {
    Flexbox(
        Modifier.flex {
            direction(FlexDirection.Column)
            debugDump(flags = DebugDumpFlag.ALL_SET)
        }
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