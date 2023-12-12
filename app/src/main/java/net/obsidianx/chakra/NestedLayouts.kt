package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun NestedLayouts() {
    Flexbox(
        modifier = Modifier
            .fillMaxWidth()
            .flex { padding(all = 1.dp) }
            .border(width = 1.dp, color = Color.Red)
    ) {
        Text("Column")
        Text("Column")
        Flexbox(
            modifier = Modifier
                .flex {
                    direction(FlexDirection.Row)
                    padding(all = 1.dp)
                }
                .border(width = 1.dp, color = Color.Green),
        ) {
            Text("Row")
            Text("Row")
            Flexbox(
                modifier = Modifier
                    .flex { padding(all = 1.dp) }
                    .border(width = 1.dp, color = Color.Blue)
            ) {
                Text("Column")
                Text("Column")
            }
        }
    }
}