package net.obsidianx.chakra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexValue

@Composable
fun Offsets() {
    Column {
        val grow = FlexStyle(
            flexShrink = 1f,
            flexGrow = 0f,
            flexBasis = FlexValue.Percent(100f)
        )
        FlexColumn(height = FlexValue.Dp(value = 300.dp)) {
            FlexColumn(
                style = FlexStyle(
                    padding = FlexEdges(all = FlexValue.Dp(value = 10.dp)),
                    margin = FlexEdges(bottom = FlexValue.Dp(value = 10.dp))
                )
            ) {

                FlexColumn(
                    style = FlexStyle(
                        margin = FlexEdges(
                            bottom = FlexValue.Dp(value = 10.dp)
                        )
                    )
                ) {
                    OutlinedText(text = "One")
                    OutlinedText(text = "Two")
                }

                FlexColumn {
                    FlexRow(style = FlexStyle(margin = FlexEdges(bottom = FlexValue.Dp(value = 10.dp)))) {
                        OutlinedText(text = "One", style = grow.copy(height= FlexValue.Dp(value = 20.dp)))
                        OutlinedText(
                            text = "Two", style = grow.copy(
                                height= FlexValue.Dp(value = 20.dp),
                                margin = FlexEdges(
                                    start = FlexValue.Dp(
                                        value = 10.dp
                                    )
                                )
                            )
                        )
                    }
                    FlexRow(style = FlexStyle(margin = FlexEdges(bottom = FlexValue.Dp(value = 10.dp)))) {
                        OutlinedText(text = "One", style = grow.copy(height= FlexValue.Dp(value = 20.dp)))
                        OutlinedText(
                            text = "Two", style = grow.copy(
                                height= FlexValue.Dp(value = 20.dp),
                                margin = FlexEdges(
                                    start = FlexValue.Dp(
                                        value = 10.dp
                                    )
                                )
                            )
                        )
                    }
                    FlexRow{
                        OutlinedText(text = "One", style = grow.copy(height= FlexValue.Dp(value = 20.dp)))
                        OutlinedText(
                            text = "Two", style = grow.copy(
                                height= FlexValue.Dp(value = 20.dp),
                                margin = FlexEdges(
                                    start = FlexValue.Dp(
                                        value = 10.dp
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}