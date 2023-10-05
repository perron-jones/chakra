package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.alignItems
import net.obsidianx.chakra.modifiers.border
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.fitMinContent
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.grow
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.justifyContent
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.modifiers.positionType
import net.obsidianx.chakra.modifiers.shrink
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

@Composable
fun DepthLayout() {
    Flexbox(
        modifier = Modifier
            .flex {
                debugTag("outer")
                debugDump()
                width(300.dp)
                height(300.dp)
                alignItems(FlexAlign.Center)
                justifyContent(FlexJustify.Center)
            }
            .border(1.dp, Color.Red)
    ) {
        Flexbox(modifier = Modifier
            .flex {
                fitMinContent()
                debugTag("depth-layer1")
                alignItems(FlexAlign.Center)
                justifyContent(FlexJustify.Center)
                padding(16.dp)
            }
            .border(1.dp, Color.Magenta)
            .clipToBounds()
        ) {
            Flexbox(
                modifier = Modifier
                    .flex {
                        fitMinContent()
                        debugTag("depth-layer2")
                        alignItems(FlexAlign.Center)
                        justifyContent(FlexJustify.Center)
                        padding(16.dp)
                        positionType(FlexPositionType.Absolute)
                    }
                    .border(1.dp, Color.Green)
            ) {
                Text(
                    "Hello, World!",
                    modifier = Modifier
                        .border(1.dp, Color.Blue)
                        .flex {
                            debugTag("text")
                            positionType(FlexPositionType.Absolute)
                        }
                )
            }
        }
    }

    Spacer(modifier = Modifier.size(10.dp))

    Flexbox(modifier = Modifier
        .flex {
            direction(FlexDirection.Column)
            height(50.dp)
            alignItems(FlexAlign.Stretch)
        }
        .border(1.dp, Color.Red)) {
        Flexbox(modifier = Modifier
            .flex {
                fitMinContent()
                justifyContent(FlexJustify.Center)
                alignItems(FlexAlign.Center)
            }
            .border(1.dp, Color.Blue)) {
            Text(
                modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                text = "Hello"
            )
            Text(
                modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                text = "World"
            )
        }
    }

    Spacer(modifier = Modifier.size(10.dp))

    Flexbox(modifier = Modifier
        .flex {
            direction(FlexDirection.Column)
            height(50.dp)
            alignItems(FlexAlign.Start)
        }
        .border(1.dp, Color.Red)) {
        Flexbox(modifier = Modifier
            .flex {
                fitMinContent()
                justifyContent(FlexJustify.Center)
                alignItems(FlexAlign.Center)
            }
            .border(1.dp, Color.Blue)) {
            Text(
                modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                text = "Hello"
            )
            Text(
                modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                text = "World"
            )
        }
    }

    Spacer(modifier = Modifier.size(10.dp))

    Flexbox(modifier = Modifier
        .flex {
            direction(FlexDirection.Column)
            height(50.dp)
        }
        .border(1.dp, Color.Red)) {
        Flexbox(modifier = Modifier
            .flex {
                fitMinContent()
                justifyContent(FlexJustify.Center)
                alignItems(FlexAlign.Center)
            }
            .border(1.dp, Color.Blue)) {
            Flexbox(modifier = Modifier
                .flex {
                    fitMinContent()
                    positionType(FlexPositionType.Absolute)
                    width(50f)
                    height(50f)
                }
                .border(1.dp, Color.Green)) {
                Text(
                    modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                    text = "Hello"
                )
                Text(
                    modifier = Modifier.flex { positionType(FlexPositionType.Absolute) },
                    text = "World"
                )
            }
        }
    }
}