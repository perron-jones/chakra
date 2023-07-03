package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexValue

@Composable
fun SampleApp() {
    FlexLayout(
        style = FlexStyle(
            flexDirection = FlexDirection.Row,
            justifyContent = FlexJustify.Start,
            alignItems = FlexAlign.Start,
        ),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Red.copy(alpha = 0.5f)
            )
            .fillMaxWidth()
    ) {
        Text(
            text = "Hello",
            modifier = Modifier
                .flex(
                    FlexStyle(
                        margin = FlexEdges(all = FlexValue.Dp(value = 8.dp)),
                    )
                )
                .border(width = 1.dp, color = Color.Blue)
        )
        Text(
            text = "World",
            modifier = Modifier
                .flex(
                    FlexStyle(
                        flexGrow = 0f,
                        flexShrink = 1f,
                        flexBasis = FlexValue.Percent(50f)
                    )
                )
                .border(width = 1.dp, color = Color.Blue)
        )
    }
}
