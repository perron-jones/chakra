package net.obsidianx.chakra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Page {
    Home,
    Alignment,
    Flex,
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
                                Button(onClick = { page = Page.Alignment }) { Text("Alignment") }
                                Button(onClick = { page = Page.Flex }) { Text("Flex") }
                            }

                            Page.Alignment -> Alignment()
                            Page.Flex -> Flex()
                        }
                    }
                }
            }
        }
    }
}