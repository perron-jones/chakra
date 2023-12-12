package net.obsidianx.chakra

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.node.InteroperableComposeUiNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaNodeFactory
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.debug.log
import net.obsidianx.chakra.layout.YogaMeasurePolicy
import net.obsidianx.chakra.types.FlexNodeData

@SuppressLint("RememberReturnType")
@Composable
fun Flexbox(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    val context by rememberUpdatedState(newValue = LocalContext.current)
    val density = LocalDensity.current
    remember { SoLoader.init(context, false) }

    // bring our own node to the tree (or start a new one)
    val node = remember { YogaNodeFactory.create() }
    // execute modifier, resolving composed modifiers
    val mod = Modifier.flexContainer(node).then(modifier)
    // extract this node's info from the incoming modifier
    val nodeData = remember(modifier) {
        mod.foldOut(FlexNodeData()) { el, nodeData ->
            nodeData.also {
                (el as? FlexParentData)?.apply {
                    density.modifyParentData(it)
                }
            }
        }
    }.also { data ->
        node.data = data
        data.style.apply(node)
    }

    node.log { "New container (node: [${node.address}]; data: [${nodeData.address}]; state: [${nodeData.nodeState.address}])" }

    // layout child views
    Layout(
        content = content,
        modifier = mod,
        measurePolicy = YogaMeasurePolicy(node, nodeData),
    )
}