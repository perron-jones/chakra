package net.obsidianx.chakra

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify

private const val t2 = "The goal of the game is to hold the flag for the longest total duration. All players have" +
        " 10 capture attempts, but after each successful claim, a hidden 5-20 second cooldown is" +
        " triggered. During this cooldown, all capture attempts will fail but still deduct an" +
        " attempt. After {AUTO_DROP_INTERVAL_MINUTES} minutes in one hands the flag will be dropped" +
        " automatically and you will need to capture it again."

private const val t3 = "Outwit your opponents by mastering the timing of captures and cooldowns."

@Composable
fun CaptureFlagWrapIssue() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    Flexbox( // R
        modifier = Modifier
            .flex {
                direction(FlexDirection.Column)
                height(320.dp)
                debugTag("<root>")
            }) {
        Flexbox( // Z1
            modifier = Modifier.flex {
                direction(FlexDirection.Column)
                depthLayout(true)
                height(100f)
                width(100f)
                debugTag("<z1>")
            }
        ) {
            val v1Padding: Dp = remember {
                16.dp
            }
            Flexbox( // V1
                modifier = Modifier.flex {
                    direction(FlexDirection.Column)
                    gap(16.dp)
                    justifyContent(FlexJustify.Center)
                    alignItems(FlexAlign.Center)
                    width(100f)
                    height(100f)
                    padding(
                        top = v1Padding,
                        start = v1Padding,
                        bottom = v1Padding,
                        end = v1Padding,
                    )
                    debugTag("<v1>")
                }
            ) {
                Flexbox( // H1
                    modifier = Modifier
                        .flex {
                            direction(FlexDirection.Row)
                            width(100f)
                            debugTag("<h1>")
                        }
                ) {
                    Text( // T1
                        text = "How to play",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .flex {
                                debugTag("t3")
                                grow(1f)
                                shrink(1f)
                            }
                    )
                    Spacer( // S
                        modifier = Modifier
                            .flex {
                                debugTag("<spacer>")
                                width(16.dp)
                                height(16.dp)
                            }
                            .size(16.dp)
                    )
                    Flexbox( // H2
                        modifier = Modifier
                            .flex {
                                debugTag("<h2>")
                                direction(FlexDirection.Row)
                                height(40.dp)
                                width(40.dp)
                                justifyContent(FlexJustify.Center)
                                alignItems(FlexAlign.Center)
                            }
                    ) {
                        Icon( // Ic
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .flex {
                                    debugTag("<Ic>")
                                })
                    }

                }
                Flexbox( // V2
                    modifier = Modifier
                        .flex {
                            debugTag("<v2>")
                            direction(FlexDirection.Column)
                            gap(16.dp)
                            width(100f)
                        }
                ) {
                    Text( // T2
                        text = t2,
                        modifier = Modifier
                            .flex {
                                debugTag("t2")
                                width(100f)
                            }
                    )
                    Text( // T3
                        text = t3,
                        modifier = Modifier
                            .flex {
                                debugTag("t3")
                                width(100f)
                            }
                    )
                }
            }
        }
    }
}