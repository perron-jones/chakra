package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify

data class Example(
    val title: String,
    val modifier: Modifier,
    val numChildren: Int = 1,
)

val alignmentExamples = listOf(
    listOf(
        Example("Justify: Start", Modifier.flex { justifyContent(FlexJustify.Start) }),
        Example("Justify: Center", Modifier.flex { justifyContent(FlexJustify.Center) }),
        Example("Justify: End", Modifier.flex { justifyContent(FlexJustify.End) }),
    ),
    listOf(
        Example(
            "Justify:\nSpace Evenly",
            Modifier.flex { justifyContent(FlexJustify.SpaceEvenly) },
            3
        ),
        Example(
            "Justify:\nSpace Around",
            Modifier.flex { justifyContent(FlexJustify.SpaceAround) },
            3
        ),
        Example(
            "Justify:\nSpace Between",
            Modifier.flex { justifyContent(FlexJustify.SpaceBetween) },
            3
        ),
    ),
    listOf(
        Example("Align: Start", Modifier.flex { alignItems(FlexAlign.Start) }),
        Example("Align: Center", Modifier.flex { alignItems(FlexAlign.Center) }),
        Example("Align: End", Modifier.flex { alignItems(FlexAlign.End) }),
    ),
    listOf(
        Example("Align: Stretch", Modifier.flex { alignItems(FlexAlign.Stretch) }),
        Example("Align: Baseline", Modifier.flex { alignItems(FlexAlign.Baseline) }),
    ),
)

@Composable
fun Alignment() {
    Column {
        Heading("Flex Column:")

        alignmentExamples.forEach { row ->
            Row {
                row.forEach { example ->
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        SubHeading(example.title)
                        FlexBox(modifier = example.modifier.flex { direction(FlexDirection.Column) }) {
                            for (i in 0 until example.numChildren) {
                                OutlinedText("Hello, world!")
                            }
                        }
                    }
                }
            }
        }

        Heading("Flex Row:")

        alignmentExamples.forEach { row ->
            Row {
                row.forEach { example ->
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        SubHeading(example.title)
                        FlexBox(modifier = example.modifier.flex { direction(FlexDirection.Row) }) {
                            for (i in 0 until example.numChildren) {
                                if (example.numChildren > 1) {
                                    OutlinedText("Hi")
                                } else {
                                    OutlinedText("Hello, world!")

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlexBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Flexbox(
        modifier = modifier
            .flex {
                width(100.dp)
                height(100.dp)
            }
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}