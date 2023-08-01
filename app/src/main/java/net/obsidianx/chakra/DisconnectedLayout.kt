package net.obsidianx.chakra

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.flexDebugDump
import net.obsidianx.chakra.modifiers.flexAlignItems
import net.obsidianx.chakra.modifiers.flexFitMinContent
import net.obsidianx.chakra.modifiers.flexGap
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexJustifyContent
import net.obsidianx.chakra.modifiers.flexPositionType
import net.obsidianx.chakra.modifiers.flexWidth
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

@Composable
fun DisconnectedLayout() {
    FlexColumn(modifier = Modifier.flexGap(16.dp)) {
        FlexRow(modifier = Modifier.flexDebugDump()) {
            Text("One")
            Box {
                net.obsidianx.chakra.FlexRow(
                    modifier = Modifier
                        .flexFitMinContent()
                        .flexDebugDump()
                ) {
                    Text("Two", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
                    Text("Three", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
                }
            }
        }

        Box(
            modifier = Modifier
                .flexWidth(100.dp)
                .flexHeight(100.dp)
        ) {
            FlexRow(
                modifier = Modifier
                    .flexAlignItems(FlexAlign.Center)
                    .flexJustifyContent(FlexJustify.Center)
                    .flexFitMinContent()
            ) {
                Text("Centered", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
                Text("Text", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
            }
        }
    }
}