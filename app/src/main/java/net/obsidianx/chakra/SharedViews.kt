package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun Heading(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = modifier.flex { padding(vertical = 8.dp) }
    )
}

@Composable
fun SubHeading(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = modifier.flex { padding(bottom = 4.dp) }
    )
}

@Composable
fun OutlinedText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .border(width = 1.dp, color = Color.Blue)
            .flex { padding(all = 4.dp) }
    )
}

@Composable
fun FlexRow(modifier: Modifier = Modifier, content: @Composable FlexboxScope.() -> Unit) {
    Flexbox(
        modifier = modifier
            .flex { direction(FlexDirection.Row) }
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexboxScope.FlexRow(
    modifier: Modifier = Modifier,
    content: @Composable FlexboxScope.() -> Unit
) {
    Flexbox(
        modifier = modifier
            .flex { direction(FlexDirection.Row) }
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexColumn(
    modifier: Modifier = Modifier,
    content: @Composable FlexboxScope.() -> Unit
) {
    Flexbox(
        modifier = modifier
            .flex {
                direction(FlexDirection.Column)
            }
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexboxScope.FlexColumn(
    modifier: Modifier = Modifier,
    content: @Composable FlexboxScope.() -> Unit
) {
    Flexbox(
        modifier = modifier
            .flex {
                direction(FlexDirection.Column)
            }
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}