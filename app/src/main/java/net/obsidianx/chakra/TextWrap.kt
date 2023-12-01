package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexUnit

private const val fillerText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
        "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ultrices dui sapien eget mi " +
        "proin sed libero. Suspendisse potenti nullam ac tortor vitae. Volutpat lacus laoreet non " +
        "curabitur gravida arcu ac tortor dignissim. Eu non diam phasellus vestibulum lorem."

@Composable
fun TextWrap() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Heading(text = "Text Wrap")
        //examples.find { it.id == TextWrapID.FLEX_HEIGHT_UNSET_TEXT_HEIGHT_UNSET }?.let { example ->
        examples.forEach { example ->
            SubHeading(text = example.description)
            FlexBox(
                modifier = example.flexBoxFlexModifier
                    .border(2.dp, Color.Green)
            ) {
                Text(
                    text = fillerText,
                    modifier = example.textFlexModifier
                        .border(1.dp, Color.Gray)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

private val examples = listOf(
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_UNSET_TEXT_HEIGHT_UNSET,
        description = "FlexBox Height not set. Text: No specification",
        flexBoxFlexModifier = Modifier
            .flex {
                direction(FlexDirection.Column)
                width(100f)
                debugDump()
            },
        textFlexModifier = Modifier
            .flex {
                debugTag("<text>")
            }
    ),
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_AUTO_TEXT_HEIGHT_UNSET,
        description = "FlexBox Height auto. Text: No specification",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                height(FlexUnit.Auto)
                debugDump()
            },
        textFlexModifier = Modifier
            .flex {
                debugTag("<text>")
            }
    ),
    TextWrapExample( // renders as expected
        id = TextWrapID.FLEX_HEIGHT_UNSET_TEXT_HEIGHT_100,
        description = "FlexBox Height not set. Text: height 100%",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                debugDump()
            },
        textFlexModifier = Modifier.flex {
            debugTag("<text>")
            height(100f)
        }
    ),
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_AUTO_TEXT_HEIGHT_100,
        description = "FlexBox Height auto. Text: height 100%",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                height(FlexUnit.Auto)
                debugDump()
            },
        textFlexModifier = Modifier.flex {
            debugTag("<text>")
            height(100f)
        }
    ),
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_UNSET_TEXT_HEIGHT_100_PADDING,
        description = "FlexBox Height not set. Text: height 100% padding",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                padding(10.dp)
                debugDump()
            },
        textFlexModifier = Modifier.flex {
            debugTag("<text>")
            height(100f)
        }
    ),
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_UNDEFINED_TEXT_HEIGHT_100,
        description = "FlexBox Height Undefined. Text: height 100%",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                height(FlexUnit.Undefined)
                debugDump()
            },
        textFlexModifier = Modifier.flex {
            debugTag("<text>")
            height(100f)
        }
    ),
    TextWrapExample(
        id = TextWrapID.FLEX_HEIGHT_UNSET_TEXT_HEIGHT_AUTO,
        description = "FlexBox Height not set. Text: height auto",
        flexBoxFlexModifier = Modifier
            .flex {
                debugTag("<column>")
                direction(FlexDirection.Column)
                width(100f)
                padding(10.dp)
                debugDump()
            },
        textFlexModifier = Modifier.flex {
            debugTag("<text>")
            height(FlexUnit.Auto)
        }
    ),
)

private data class TextWrapExample(
    val id: TextWrapID,
    val description: String,
    val flexBoxFlexModifier: Modifier,
    val textFlexModifier: Modifier
)

enum class TextWrapID {
    FLEX_HEIGHT_UNSET_TEXT_HEIGHT_UNSET,
    FLEX_HEIGHT_AUTO_TEXT_HEIGHT_UNSET,
    FLEX_HEIGHT_UNSET_TEXT_HEIGHT_100,
    FLEX_HEIGHT_AUTO_TEXT_HEIGHT_100,
    FLEX_HEIGHT_UNSET_TEXT_HEIGHT_100_PADDING,
    FLEX_HEIGHT_UNDEFINED_TEXT_HEIGHT_100,
    FLEX_HEIGHT_UNSET_TEXT_HEIGHT_AUTO
}