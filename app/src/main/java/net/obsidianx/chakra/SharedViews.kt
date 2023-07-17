package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexValue

@Composable
fun Heading(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SubHeading(text: String) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun FlexLayoutScope.OutlinedText(text: String, style: FlexStyle = FlexStyle()) {
    Text(
        text,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .border(width = 1.dp, color = Color.Blue)
            .flex(style.copy(padding = FlexEdges(all = FlexValue.Dp(value = 4.dp))))
    )
}

@Composable
fun FlexRow(
    style: FlexStyle = FlexStyle(),
    content: @Composable FlexLayoutScope.() -> Unit
) {
    FlexLayout(
        style = style.copy(flexDirection = FlexDirection.Row),
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexColumn(
    height: FlexValue = FlexValue.Undefined,
    style: FlexStyle = FlexStyle(),
    content: @Composable FlexLayoutScope.() -> Unit
) {
    FlexLayout(
        style = style.copy(flexDirection = FlexDirection.Column, height = height),
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f)),
    ) {
        content()
    }
}