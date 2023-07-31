package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.modifiers.flexBorder
import net.obsidianx.chakra.modifiers.flexDirection
import net.obsidianx.chakra.modifiers.flexMargin
import net.obsidianx.chakra.modifiers.flexPadding
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun Heading(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = modifier.flexPadding(vertical = 8.dp)
    )
}

@Composable
fun SubHeading(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = modifier.flexPadding(bottom = 4.dp)
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
            .flexPadding(all = 4.dp)
    )
}

@Composable
fun FlexRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Flexbox(
        modifier = modifier
            .flexDirection(FlexDirection.Row)
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Flexbox(
        modifier = modifier
            .flexDirection(FlexDirection.Column)
            .border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}