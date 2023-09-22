package net.obsidianx.chakra

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.basis
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.grow
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.shrink

@Composable
fun Flex() {
    Column {
        Heading("Flex Shrink/Grow/Basis")

        SubHeading("Default style")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Two")
            OutlinedText("Three")
        }

        SubHeading("All equal sizes (Grow: 0; Shrink: 1; Basis: 100%)")
        FlexRow {
            Modifier.flex {
                grow(0f)
                shrink(1f)
                basis(percent = 100f)
            }.let { modifier ->
                OutlinedText("One", modifier)
                OutlinedText("Two", modifier)
                OutlinedText("Three", modifier)
            }
        }

        SubHeading("Only set single to: grow: 1")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Two", modifier = Modifier.flex { grow(1f) })
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Basis: 25%", modifier = Modifier.flex { basis(percent = 25f) })
            OutlinedText("Basis: 50%", modifier = Modifier.flex { basis(percent = 50f) })
        }

        SubHeading("Basis + Split remaining space")
        FlexRow {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                modifier = Modifier.flex {
                    basis(percent = 25f)
                    grow(1f)
                }
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                modifier = Modifier.flex {
                    basis(50f)
                    grow(1f)
                }
            )
        }

        Heading("Vertical")

        SubHeading("Default style")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            OutlinedText("One")
            OutlinedText("Two")
            OutlinedText("Three")
        }

        SubHeading("All equal sizes")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            Modifier.flex {
                grow(0f)
                shrink(1f)
                basis(percent = 100f)
            }.let { modifier ->
                OutlinedText("One", modifier)
                OutlinedText("Two", modifier)
                OutlinedText("Three", modifier)
            }
        }

        SubHeading("Only set single to: grow: 1")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            OutlinedText("One")
            OutlinedText("Two", modifier = Modifier.flex { grow(1f) })
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            OutlinedText("One")
            OutlinedText("Basis: 25%", modifier = Modifier.flex { basis(25f) })
            OutlinedText("Basis: 50%", modifier = Modifier.flex { basis(50f) })
        }

        SubHeading("Basis + Split remaining space")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                modifier = Modifier.flex {
                    grow(1f)
                    basis(percent = 25f)
                }
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                modifier = Modifier.flex {
                    grow(1f)
                    basis(percent = 50f)
                }
            )
        }

        SubHeading("Nested layouts")
        FlexColumn(modifier = Modifier.flex { height(200.dp) }) {
            FlexColumn {
                OutlinedText("One")
            }

            FlexColumn {
                OutlinedText("Two")
            }
        }
    }
}
