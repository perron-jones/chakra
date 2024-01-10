package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reddit.devvit.ui.block_kit.v1beta.Attributes
import com.reddit.devvit.ui.block_kit.v1beta.Attributes.BlockSize
import com.reddit.devvit.ui.block_kit.v1beta.BlockOuterClass.BlockConfig
import com.reddit.devvit.ui.block_kit.v1beta.Enums
import net.obsidianx.chakra.types.FlexDirection

private const val t2 = "The goal of the game is to hold the flag for the longest total duration. All players have" +
        " 10 capture attempts, but after each successful claim, a hidden 5-20 second cooldown is" +
        " triggered. During this cooldown, all capture attempts will fail but still deduct an" +
        " attempt. After {AUTO_DROP_INTERVAL_MINUTES} minutes in one hands the flag will be dropped" +
        " automatically and you will need to capture it again."

private const val t3 = "Outwit your opponents by mastering the timing of captures and cooldowns."

@Composable
fun CaptureFlagFlexFormatting() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .border(1.dp, Color.Blue)
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    Flexbox( // R
        modifier = Modifier
            .border(1.dp, Color.Green)
            .flex {
                direction(FlexDirection.Column)
                height(320.dp)
                debugTag("<root>")
            }) {
        // <zstack height="100%" width="100%">
        val z1Size = remember {
            BlockSize.newBuilder()
                .setWidth(100f)
                .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                .setHeight(100f)
                .setHeightUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                .build()
        }
        val z1Config = remember {
            BlockConfig.Stack.newBuilder()
                .setDirection(Enums.BlockStackDirection.STACK_DEPTH)
                .build()
        }
        Flexbox( // Z1
            modifier = Modifier
                .flexBlockStyle(
                    blockSize = z1Size,
                    blockSizes = null,
                )
                .flexStackStyle(z1Size, z1Config, false)
        ) {
            // <vstack width="100%" height="100%" alignment="center" padding="medium" gap="medium">
            val v1Size = remember {
                BlockSize.newBuilder()
                    .setWidth(100f)
                    .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                    .setHeight(100f)
                    .setHeightUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                    .build()
            }
            val v1Config = remember {
                BlockConfig.Stack.newBuilder()
                    .setDirection(Enums.BlockStackDirection.STACK_VERTICAL)
                    .setAlignment(
                        Attributes.BlockAlignment.newBuilder()
                            .setHorizontal(Enums.BlockHorizontalAlignment.ALIGN_CENTER)
                    )
                    .setGap(Enums.BlockGap.GAP_MEDIUM)
                    .setPadding(Enums.BlockPadding.PADDING_MEDIUM)
                    .build()
            }
            Flexbox( // V1
                modifier = Modifier
                    .flexStackChildStyle(
                        stackConfig = z1Config,
                        childSize = v1Size,
                        childSizes = null,
                        index = 0,
                    )
                    .flexStackStyle(v1Size, v1Config, false)
            ) {
                // <hstack width="100%" alignment="middle">
                val h1Size = remember {
                    BlockSize.newBuilder()
                        .setWidth(100f)
                        .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                        .build()
                }
                val h1Config = remember {
                    BlockConfig.Stack.newBuilder()
                        .setDirection(Enums.BlockStackDirection.STACK_HORIZONTAL)
                        .setAlignment(
                            Attributes.BlockAlignment.newBuilder()
                                .setVertical(Enums.BlockVerticalAlignment.ALIGN_MIDDLE)
                        )
                        .build()
                }
                Flexbox( // H1
                    modifier = Modifier
                        .border(1.dp, Color.Magenta)
                        .flexStackChildStyle(
                            stackConfig = v1Config,
                            childSize = h1Size,
                            childSizes = null,
                            index = 0,
                        )
                        .flexStackStyle(h1Size, h1Config, false)
                ) {
                    // <text color="white" weight="bold" size="xlarge" overflow="ellipsis" grow>
                    val t1Size = remember {
                        BlockSize.newBuilder()
                            .setGrow(true)
                            .build()
                    }
                    Text( // T1
                        text = "How to play",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .flexStackChildStyle(
                                stackConfig = h1Config,
                                childSize = t1Size,
                                childSizes = null,
                                index = 0,
                            )
                    )
                    // <spacer size="medium" />
                    val sSize = remember {
                        BlockSize.newBuilder().build()
                    }
                    Spacer( // S
                        modifier = Modifier
                            .flexStackChildStyle(
                                stackConfig = h1Config,
                                childSize = sSize,
                                childSizes = null,
                                index = 1,
                            )
                            .flex {
                                debugTag("<spacer>")
                                width(16.dp)
                                height(16.dp)
                            }
                            .width(16.dp)
                            .height(16.dp)
                    )
                    /*
                    <hstack
                      backgroundColor="rgba(255, 255, 255, 0.1)"
                      cornerRadius="full"
                      height="40px"
                      width="40px"
                      alignment="center middle"
                      onPress={onPress}
                    >
                     */
                    val h2Size = remember {
                        BlockSize.newBuilder()
                            .setHeight(40f)
                            .setHeightUnit(Enums.BlockSizeUnit.SIZE_UNIT_PIXELS)
                            .setWidth(40f)
                            .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PIXELS)
                            .build()
                    }
                    val h2Config = remember {
                        BlockConfig.Stack.newBuilder()
                            .setDirection(Enums.BlockStackDirection.STACK_HORIZONTAL)
                            .setCornerRadius(Enums.BlockRadius.RADIUS_FULL)
                            .setAlignment(
                                Attributes.BlockAlignment.newBuilder()
                                    .setHorizontal(Enums.BlockHorizontalAlignment.ALIGN_CENTER)
                                    .setVertical(Enums.BlockVerticalAlignment.ALIGN_MIDDLE)
                                    .build()
                            )
                            .build()
                    }
                    Flexbox( // H2
                        modifier = Modifier
                            .flexStackChildStyle(
                                stackConfig = h1Config,
                                childSize = h2Size,
                                childSizes = null,
                                index = 2,
                            )
                            .flexStackStyle(h2Size, h2Config, false)
                    ) {
                        Icon( // Ic
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .flexStackChildStyle(
                                    stackConfig = h2Config,
                                    childSize = h2Size,
                                    childSizes = null,
                                    index = 0,
                                )
                                .flex {
                                    debugTag("<Ic>")
                                })
                    }
                }
                // <vstack gap="medium" width="100%">
                val v2Size = remember {
                    BlockSize.newBuilder()
                        .setWidth(100f)
                        .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                        .build()
                }
                val v2Config = remember {
                    BlockConfig.Stack.newBuilder()
                        .setDirection(Enums.BlockStackDirection.STACK_VERTICAL)
                        .setGap(Enums.BlockGap.GAP_MEDIUM)
                        .build()
                }
                Flexbox( // V2
                    modifier = Modifier
                        .border(1.dp, Color.Red)
                        .flexStackChildStyle(
                            stackConfig = v1Config,
                            childSize = v2Size,
                            childSizes = null,
                            index = 1,
                        )
                        .flexStackStyle(v2Size, v2Config, false)
                ) {
                    // <text color="white" wrap width="100%">
                    val t2Size = remember {
                        BlockSize.newBuilder()
                            .setWidth(100f)
                            .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                            .build()
                    }
                    Text( // T2
                        text = t2,
                        softWrap = true,
                        modifier = Modifier
                            .flexStackChildStyle(
                                stackConfig = v2Config,
                                childSize = t2Size,
                                childSizes = null,
                                index = 0,
                            )
                            .flex {
                                debugTag("t2")
                            }
                    )
                    // <text color="white" wrap width="100%">
                    val t3Size = remember {
                        BlockSize.newBuilder()
                            .setWidth(100f)
                            .setWidthUnit(Enums.BlockSizeUnit.SIZE_UNIT_PERCENT)
                            .build()
                    }
                    Text( // T3
                        text = t3,
                        softWrap = true,
                        modifier = Modifier
                            .flexStackChildStyle(
                                stackConfig = v2Config,
                                childSize = t3Size,
                                childSizes = null,
                                index = 1,
                            )
                            .flex {
                                debugTag("t3")
                            }
                    )
                }
            }
        }
    }
}