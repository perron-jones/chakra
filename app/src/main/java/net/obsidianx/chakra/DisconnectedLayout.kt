package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.flexDebugDump
import net.obsidianx.chakra.debug.flexDebugTag
import net.obsidianx.chakra.modifiers.flexAlignItems
import net.obsidianx.chakra.modifiers.flexFitMinContent
import net.obsidianx.chakra.modifiers.flexGap
import net.obsidianx.chakra.modifiers.flexJustifyContent
import net.obsidianx.chakra.modifiers.flexPositionType
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

@Composable
fun DisconnectedLayout() {
    FlexColumn(modifier = Modifier
        .flexGap(16.dp)
        .flexDebugTag("root")
        .flexDebugDump()) {
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


        FlexRow(
            modifier = Modifier
                .flexAlignItems(FlexAlign.Center)
                .flexJustifyContent(FlexJustify.Center)
                .flexDebugTag("zstack")
                .flexFitMinContent()
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(100.dp)
                    .flexPositionType(FlexPositionType.Absolute)
                    .flexDebugTag("tall")
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(5.dp)
                    .flexPositionType(FlexPositionType.Absolute)
                    .flexDebugTag("wide")
                    .background(Color.Blue)
            )
//            Text("Centered", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
//            Text("Text", modifier = Modifier.flexPositionType(FlexPositionType.Absolute))
        }
    }
}