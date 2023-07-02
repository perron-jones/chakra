package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexValue

@Composable
fun SampleApp() {
    Column {
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
                            flexGrow = 1f
                        )
                    )
                    .border(width = 1.dp, color = Color.Blue)
            )
            Text(
                text = "World",
                modifier = Modifier
                    .flex(
                        FlexStyle(
                        )
                    )
                    .border(width = 1.dp, color = Color.Blue)
            )
            FlexLayout(
                modifier = Modifier
                    .flex(
                        FlexStyle(
                            width = FlexValue.Dp(value = 100.dp),
                            height = FlexValue.Dp(value = 100.dp)
                        )
                    )
                    .border(width = 1.dp, color = Color.Red),
                style = FlexStyle(
                    alignItems = FlexAlign.Center,
                    justifyContent = FlexJustify.Center
                )
            ) {
                Text(text = "Foo", modifier = Modifier.flex())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSampleApp() {
    SampleApp()
}