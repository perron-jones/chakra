package net.obsidianx.chakra

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.basis
import net.obsidianx.chakra.modifiers.border
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.gap
import net.obsidianx.chakra.modifiers.grow
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.margin
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.modifiers.shrink

@Composable
fun Offsets() {
    Column {
        val grow = Modifier.flex {
            shrink(0f)
            grow(1f)
            basis(percent = 0f)
        }
        FlexColumn(
            modifier = Modifier.flex {
                height(300.dp)
                debugTag("outer")
            }
        ) {
            FlexColumn(
                modifier = Modifier.flex {
                    padding(all = 4.dp)
                    border(all = 4.dp)
                    margin(all = 4.dp)
                    debugTag("layer1")
                }
            ) {

                FlexColumn(
                    modifier = Modifier.flex {
                        margin(bottom = 10.dp)
                        gap(all = 4.dp)
                        debugTag("layer2-1")
                    }
                ) {
                    OutlinedText("One")
                    OutlinedText("Two")
                }

                FlexColumn(modifier = Modifier.flex { debugTag("layer2-2") }) {
                    FlexRow(modifier = Modifier.flex { margin(bottom = 10.dp) }) {
                        OutlinedText("One", modifier = grow)
                        OutlinedText("Two", modifier = grow.flex { margin(start = 10.dp) })
                    }
                    FlexRow(modifier = Modifier.flex { margin(bottom = 10.dp) }) {
                        OutlinedText("One", modifier = grow)
                        OutlinedText(
                            "Two",
                            modifier = grow.flex { margin(start = 10.dp) }
                        )
                    }
                    FlexRow {
                        OutlinedText("One", modifier = grow)
                        OutlinedText(
                            "Two",
                            modifier = grow.flex { margin(start = 10.dp) }
                        )
                    }
                }
            }
        }
    }
}
