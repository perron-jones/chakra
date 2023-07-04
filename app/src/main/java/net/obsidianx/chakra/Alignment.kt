package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexValue

data class Example(
    val title: String,
    val style: FlexStyle,
    val numChildren: Int = 1,
)

val alignmentExamples = listOf(
    listOf(
        Example("Justify: Start", FlexStyle(justifyContent = FlexJustify.Start)),
        Example("Justify: Center", FlexStyle(justifyContent = FlexJustify.Center)),
        Example("Justify: End", FlexStyle(justifyContent = FlexJustify.End)),
    ),
    listOf(
        Example("Justify:\nSpace Evenly", FlexStyle(justifyContent = FlexJustify.SpaceEvenly), 3),
        Example("Justify:\nSpace Around", FlexStyle(justifyContent = FlexJustify.SpaceAround), 3),
        Example("Justify:\nSpace Between", FlexStyle(justifyContent = FlexJustify.SpaceBetween), 3),
    ),
    listOf(
        Example("Align: Start", FlexStyle(alignItems = FlexAlign.Start)),
        Example("Align: Center", FlexStyle(alignItems = FlexAlign.Center)),
        Example("Align: End", FlexStyle(alignItems = FlexAlign.End)),
    ),
    listOf(
        Example("Align: Stretch", FlexStyle(alignItems = FlexAlign.Stretch)),
        Example("Align: Baseline", FlexStyle(alignItems = FlexAlign.Baseline)),
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
                        FlexBox(style = example.style.copy(flexDirection = FlexDirection.Column)) {
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
                        FlexBox(style = example.style.copy(flexDirection = FlexDirection.Row)) {
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
    style: FlexStyle = FlexStyle(),
    tag: String? = null,
    content: @Composable FlexLayoutScope.() -> Unit
) {
    FlexLayout(
        style = style.copy(
            width = FlexValue.Dp(value = 100.dp),
            height = FlexValue.Dp(value = 100.dp),
        ),
        tag = tag,
        modifier = modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}