package net.obsidianx.chakra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.obsidianx.chakra.debug.debugDump
import net.obsidianx.chakra.debug.debugTag
import net.obsidianx.chakra.modifiers.alignItems
import net.obsidianx.chakra.modifiers.direction
import net.obsidianx.chakra.modifiers.fitMinContent
import net.obsidianx.chakra.modifiers.flex
import net.obsidianx.chakra.modifiers.height
import net.obsidianx.chakra.modifiers.justifyContent
import net.obsidianx.chakra.modifiers.padding
import net.obsidianx.chakra.modifiers.positionType
import net.obsidianx.chakra.modifiers.width
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexPositionType

enum class Page {
    Home,
    Alignment,
    Flex,
    Offsets,
    NestedLayouts,
    Shrink,
    DisconnectedLayout,
    DepthLayout,
    SwapContent,
    UpdateContents,
}

@Composable
fun Test(modifier: Modifier = Modifier) {
    Flexbox(modifier = modifier
        .flex {
            debugTag("in")
            padding(4.dp)
        }
        .border(1.dp, Color.Red)) {
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var page by remember { mutableStateOf(Page.Home) }

            Column {
                if (page != Page.Home) {
                    Button(
                        onClick = { page = Page.Home },
                        modifier = Modifier.padding(all = 8.dp)
                    ) { Text("Back") }
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                ) {
                    Column(modifier = Modifier.padding(all = 8.dp)) {
                        when (page) {
                            Page.Home -> {
                                Page.values().forEach {
                                    if (it != Page.Home) {
                                        Button(onClick = { page = it }) { Text(it.toString()) }
                                    }
                                }
                            }

                            Page.Alignment -> Alignment()
                            Page.Flex -> Flex()
                            Page.Offsets -> Offsets()
                            Page.NestedLayouts -> NestedLayouts()
                            Page.Shrink -> Shrink()
                            Page.DisconnectedLayout -> DisconnectedLayout()
                            Page.DepthLayout -> DepthLayout()
                            Page.SwapContent -> SwapContent()
                            Page.UpdateContents -> UpdateContents()
                        }
                    }
                }
            }
        }
    }
}