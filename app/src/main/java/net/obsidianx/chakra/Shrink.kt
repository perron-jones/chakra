package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun GreenBox(content: @Composable () -> Unit = {}) {
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
fun BlueBox(content: @Composable () -> Unit = {}) {
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
            .border(width = 1.dp, color = Color.Yellow),
        content = content,
    )
}

@Composable
fun MagentaBox(content: @Composable () -> Unit = {}) {
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
        repeat(3) { green ->
            GreenBox {
                repeat(green+1) {
                    BlueBox {
                        repeat(green+1) {
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