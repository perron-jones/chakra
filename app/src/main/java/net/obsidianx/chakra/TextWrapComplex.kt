package net.obsidianx.chakra

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.fitMinContent
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.overflow
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexOverflow

private const val FILLER_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
        "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ultrices dui sapien eget mi " +
        "proin sed libero. Suspendisse potenti nullam ac tortor vitae. Volutpat lacus laoreet non " +
        "curabitur gravida arcu ac tortor dignissim. Eu non diam phasellus vestibulum lorem."


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TextWrapComplex() {
    var visible: Boolean by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                visible = false
                GlobalScope.launch(Dispatchers.IO) {
                    delay(500)
                    withContext(Dispatchers.Main) {
                        visible = true
                    }
                }
            }
        ) {
            Text(text = "Refresh")
        }
        if (visible) {
            Content()
        }
    }
    
}

@Composable
private fun Content() {
    Flexbox( // <blocks> aka root
        modifier = Modifier
            .flex {
                direction(FlexDirection.Column)
                height(320.dp)
                debugTag("<blocks>")
                debugDump(flags = DebugDumpFlag.ALL_SET)
            }
            .border(1.dp, Color.Green)
    ) {
        Text(
            text = FILLER_TEXT,
            modifier = Modifier
                .flex { debugTag("<text>") },
            softWrap = true
        )
        TextSpacer()
        Divider(
            modifier = Modifier
                .flex{
                    debugTag("<divider>")
                }
        )
        TextSpacer()
        HeightUnboundedWrap(flexboxScope = this)
    }
}

@Composable
private fun TextSpacer() {
    Spacer(
        modifier = Modifier
            .flex {
                debugTag("<spacer>")
                width(8.dp)
                height(8.dp)
            }
            .width(8.dp)
            .height(8.dp),
    )
}

@Composable
private fun HeightUnboundedWrap(flexboxScope: FlexboxScope) {
    WrapText(
        label = "Height: unbounded; Wrap: enabled",
        tagSuffix = "HeightUnboundedWrap",
        overflow = TextOverflow.Clip,
        wrap = true,
        flexboxScope
    )
}

@Composable
private fun WrapText(
    label: String,
    tagSuffix:String,
    overflow: TextOverflow,
    wrap: Boolean,
    flexboxScope: FlexboxScope) {
    flexboxScope.Flexbox(
        modifier = Modifier
            .flex {
                direction(FlexDirection.Column)
                overflow(FlexOverflow.Hidden)
                debugTag("<v-stack ($tagSuffix)>")
                fitMinContent(false)
            }
    ) {
        Text(
            text = label,
            modifier = Modifier
                .flex {
                    debugTag("<text>")
                },
            fontWeight = FontWeight.Bold
        )
        this.Flexbox(
            modifier = Modifier
                .flex {
                    direction(FlexDirection.Column)
                    debugTag("<v-stack>")
                }
        ) {
            Text(
                text = FILLER_TEXT,
                modifier = Modifier
                    .flex { debugTag("<text ($tagSuffix)>") },
                overflow = overflow,
                softWrap = wrap
            )
        }
    }
}