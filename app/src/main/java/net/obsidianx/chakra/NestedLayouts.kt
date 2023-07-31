package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.flexDirection
import net.obsidianx.chakra.modifiers.flexPadding
import net.obsidianx.chakra.types.FlexDirection

@Composable
fun NestedLayouts() {
    Flexbox(
        modifier = Modifier
            .fillMaxWidth()
            .flexPadding(all = 1.dp)
            .border(width = 1.dp, color = Color.Red)
    ) {
        Text("Column")
        Flexbox(
            modifier = Modifier
                .flexDirection(FlexDirection.Row)
                .flexPadding(all = 1.dp)
                .border(width = 1.dp, color = Color.Green),
        ) {
            Text("Row")
            Flexbox(
                modifier = Modifier
                    .flexPadding(all = 1.dp)
                    .border(width = 1.dp, color = Color.Blue)
            ) {
                Text("Column")
            }
        }
    }
}