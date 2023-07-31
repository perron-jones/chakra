package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.flexDirection
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexWidth
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun GreenBox(content: @Composable () -> Unit) {
    Flexbox(
        modifier = Modifier
            .flexWidth(100.dp)
            .flexHeight(100.dp)
            .flexDirection(FlexDirection.Row)
            .background(Color.Green)
    ) {
        content()
    }
}

@Composable
fun BlueBox(content: (@Composable () -> Unit)? = null) {
    Flexbox(
        modifier = Modifier
            .flexWidth(40.dp)
            .flexHeight(90.dp)
            .flexDirection(FlexDirection.Row)
            .background(Color.Blue)
            .border(width = 1.dp, color = Color.Yellow)
    ) {
        content?.invoke()
    }
}

@Composable
fun MagentaBox(content: @Composable () -> Unit = {}) {
    Flexbox(
        modifier = Modifier
            .flexWidth(20.dp)
            .flexHeight(80.dp)
            .flexDirection(FlexDirection.Row)
            .background(Color.Magenta)
            .border(width = 1.dp, color = Color.Gray)
    ) { content() }
}

@Composable
fun Shrink() {
    Row {
        Column {
//            repeat(1) { green ->
//                GreenBox {
                    repeat(1) {
                        BlueBox {
                            repeat(3) {
                                MagentaBox {
                                    Text("Hi", softWrap = false, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }
//    }
//}