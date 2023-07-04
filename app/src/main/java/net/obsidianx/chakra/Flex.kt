package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexValue

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
        FlexStyle(
            flexGrow = 0f,
            flexShrink = 1f,
            flexBasis = FlexValue.Percent(100f)
        ).let { style ->
            FlexRow {
                OutlinedText("One", style)
                OutlinedText("Two", style)
                OutlinedText("Three", style)
            }
        }

        SubHeading("Only set single to: grow: 1")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Two", FlexStyle(flexGrow = 1f))
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexRow {
            OutlinedText("One")
            OutlinedText("Basis: 25%", FlexStyle(flexBasis = FlexValue.Percent(25f)))
            OutlinedText("Basis: 50%", FlexStyle(flexBasis = FlexValue.Percent(50f)))
        }

        SubHeading("Basis + Split remaining space")
        FlexRow {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                FlexStyle(flexBasis = FlexValue.Percent(25f), flexGrow = 1f)
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                FlexStyle(flexBasis = FlexValue.Percent(50f), flexGrow = 1f)
            )
        }

        Heading("Vertical")

        SubHeading("Default style")
        FlexColumn {
            OutlinedText("One")
            OutlinedText("Two")
            OutlinedText("Three")
        }

        SubHeading("All equal sizes")
        FlexStyle(
            flexGrow = 0f,
            flexShrink = 1f,
            flexBasis = FlexValue.Percent(100f)
        ).let { style ->
            FlexColumn {
                OutlinedText("One", style)
                OutlinedText("Two", style)
                OutlinedText("Three", style)
            }
        }

        SubHeading("Only set single to: grow: 1")
        FlexColumn {
            OutlinedText("One")
            OutlinedText("Two", FlexStyle(flexGrow = 1f))
            OutlinedText("Three")
        }

        SubHeading("Basis")
        FlexColumn {
            OutlinedText("One")
            OutlinedText("Basis: 25%", FlexStyle(flexBasis = FlexValue.Percent(25f)))
            OutlinedText("Basis: 50%", FlexStyle(flexBasis = FlexValue.Percent(50f)))
        }

        SubHeading("Basis + Split remaining space")
        FlexColumn {
            OutlinedText("One")
            OutlinedText(
                "Basis: 25%; Grow: 1",
                FlexStyle(flexBasis = FlexValue.Percent(25f), flexGrow = 1f)
            )
            OutlinedText(
                "Basis: 50%; Grow: 1",
                FlexStyle(flexBasis = FlexValue.Percent(50f), flexGrow = 1f)
            )
        }
    }
}

@Composable
fun FlexRow(content: @Composable FlexLayoutScope.() -> Unit) {
    FlexLayout(
        style = FlexStyle(flexDirection = FlexDirection.Row),
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexColumn(content: @Composable FlexLayoutScope.() -> Unit) {
    FlexLayout(
        style = FlexStyle(flexDirection = FlexDirection.Column, height = FlexValue.Dp(200.dp)),
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}