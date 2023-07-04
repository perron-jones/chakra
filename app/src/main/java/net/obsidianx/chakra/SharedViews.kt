package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.types.FlexDirection
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
        text, modifier = Modifier
            .border(width = 1.dp, color = Color.Blue)
            .flex(style)
    )
}

@Composable
fun FlexRow(
    style: FlexStyle = FlexStyle(),
    tag: String? = null,
    content: @Composable FlexLayoutScope.() -> Unit
) {
    FlexLayout(
        style = style.copy(flexDirection = FlexDirection.Row),
        tag = tag,
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f))
    ) {
        content()
    }
}

@Composable
fun FlexColumn(
    height: FlexValue = FlexValue.Undefined,
    style: FlexStyle = FlexStyle(),
    tag: String? = null,
    content: @Composable FlexLayoutScope.() -> Unit
) {
    FlexLayout(
        style = style.copy(flexDirection = FlexDirection.Column, height = height),
        modifier = Modifier.border(width = 1.dp, color = Color.Red.copy(alpha = 0.5f)),
        tag = tag,
    ) {
        content()
    }
}