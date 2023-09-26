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
import net.obsidianx.chakra.modifiers.alignItems
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.types.FlexAlign

@Composable
fun UpdateContents() {
    var incr by remember { mutableStateOf(0) }

    Column {
        Button(onClick = { incr++ }) {
            Text("Increment")
        }
        Flexbox(
            modifier = Modifier
                .flex { padding(1.dp) }
                .border(1.dp, Color.Red)
        ) {
            Flexbox(
                modifier = Modifier
                    .flex { padding(1.dp) }
                    .border(1.dp, Color.Blue)
            ) {
                Flexbox(
                    modifier = Modifier
                        .flex { padding(1.dp) }
                        .border(1.dp, Color.Green)
                ) {
                    Flexbox(
                        modifier = Modifier
                            .flex { padding(1.dp) }
                            .border(1.dp, Color.Magenta)
                    ) {
                        Flexbox(modifier = Modifier
                            .flex {
                                padding(4.dp)
                                alignItems(FlexAlign.Start)
                            }
                            .border(1.dp, Color.Cyan)) {
                            Text("$incr", modifier = Modifier.border(1.dp, Color.Black))
                        }
                    }
                }
            }
        }
    }
}
