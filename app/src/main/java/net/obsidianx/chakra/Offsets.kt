package net.obsidianx.chakra

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.modifiers.flexBasis
import net.obsidianx.chakra.modifiers.flexBorder
import net.obsidianx.chakra.modifiers.flexDebugTag
import net.obsidianx.chakra.modifiers.flexGap
import net.obsidianx.chakra.modifiers.flexGrow
import net.obsidianx.chakra.modifiers.flexHeight
import net.obsidianx.chakra.modifiers.flexMargin
import net.obsidianx.chakra.modifiers.flexPadding
import net.obsidianx.chakra.modifiers.flexShrink

@Composable
fun Offsets() {
    Column {
        val grow = Modifier
            .flexShrink(1f)
            .flexGrow(0f)
            .flexBasis(percent = 100f)
        FlexColumn(
            modifier = Modifier
                .flexHeight(300.dp)
                .flexDebugTag("outer")
        ) {
            FlexColumn(
                modifier = Modifier
                    .flexPadding(all = 4.dp)
                    .flexBorder(all = 4.dp)
                    .flexMargin(all = 4.dp)
                    .flexDebugTag("layer1")
            ) {

                FlexColumn(
                    modifier = Modifier
                        .flexMargin(bottom = 10.dp)
                        .flexDebugTag("layer2-1")
                        .flexGap(all = 4.dp)
                ) {
                    OutlinedText("One")
                    OutlinedText("Two")
                }

                FlexColumn(modifier = Modifier.flexDebugTag("layer2-2")) {
                    FlexRow(modifier = Modifier.flexMargin(bottom = 10.dp)) {
                        OutlinedText("One", modifier = grow)
                        OutlinedText("Two", modifier = grow.flexMargin(start = 10.dp))
                    }
                    FlexRow(modifier = Modifier.flexMargin(bottom = 10.dp)) {
                        OutlinedText("One", modifier = grow)
                        OutlinedText(
                            "Two",
                            modifier = grow.flexMargin(start = 10.dp)
                        )
                    }
                    FlexRow {
                        OutlinedText("One", modifier = grow)
                        OutlinedText(
                            "Two",
                            modifier = grow.flexMargin(start = 10.dp)
                        )
                    }
                }
            }
        }
    }
}