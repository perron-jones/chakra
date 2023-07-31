package net.obsidianx.chakra

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.flexBasis
import net.obsidianx.chakra.modifiers.flexGrow
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexShrink

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
            Modifier
                .flexGrow(0f)
                .flexShrink(1f)
                .flexBasis(percent = 100f).let { modifier ->
                    OutlinedText("One", modifier)
                    OutlinedText("Two", modifier)
                    OutlinedText("Three", modifier)
                }
        }

        SubHeading("Only set single to: grow: 1")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Two", modifier = Modifier.flexGrow(1f))
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Basis: 25%", modifier = Modifier.flexBasis(percent = 25f))
            OutlinedText("Basis: 50%", modifier = Modifier.flexBasis(percent = 50f))
        }

        SubHeading("Basis + Split remaining space")
        FlexRow {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                modifier = Modifier
                    .flexBasis(percent = 25f)
                    .flexGrow(1f)
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                modifier = Modifier
                    .flexBasis(50f)
                    .flexGrow(1f)
            )
        }

        Heading("Vertical")

        SubHeading("Default style")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            OutlinedText("One")
            OutlinedText("Two")
            OutlinedText("Three")
        }

        SubHeading("All equal sizes")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            Modifier.flexGrow(0f).flexShrink(1f).flexBasis(percent = 100f).let { modifier ->
                OutlinedText("One", modifier)
                OutlinedText("Two", modifier)
                OutlinedText("Three", modifier)
            }
        }

        SubHeading("Only set single to: grow: 1")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            OutlinedText("One")
            OutlinedText("Two", modifier = Modifier.flexGrow(1f))
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            OutlinedText("One")
            OutlinedText("Basis: 25%", modifier = Modifier.flexBasis(25f))
            OutlinedText("Basis: 50%", modifier = Modifier.flexBasis(50f))
        }

        SubHeading("Basis + Split remaining space")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                modifier = Modifier
                    .flexGrow(1f)
                    .flexBasis(percent = 25f)
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                modifier = Modifier
                    .flexGrow(1f)
                    .flexBasis(percent = 50f)
            )
        }

        SubHeading("Nested layouts")
        FlexColumn(modifier = Modifier.flexHeight(200.dp)) {
            FlexColumn {
                OutlinedText("One")
            }

            FlexColumn {
                OutlinedText("Two")
            }
        }
    }
}
