package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.flexAlignItems
import net.obsidianx.chakra.modifiers.flexFitMinContent
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexJustifyContent
import net.obsidianx.chakra.modifiers.flexPositionType
import net.obsidianx.chakra.modifiers.flexWidth
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

@Composable
fun DepthLayout() {
    Flexbox(
        modifier = Modifier
            .flexWidth(300.dp)
            .flexHeight(300.dp)
            .flexAlignItems(FlexAlign.Center)
            .flexJustifyContent(FlexJustify.Center)
            .flexFitMinContent()
            .border(1.dp, Color.Red)
    ) {
        Text(
            "Hello",
            modifier = Modifier
                .border(1.dp, Color.Blue)
                .flexPositionType(FlexPositionType.Absolute)
        )
        Text(
            "World",
            modifier = Modifier
                .border(1.dp, Color.Blue)
                .flexPositionType(FlexPositionType.Absolute)
        )
    }
}