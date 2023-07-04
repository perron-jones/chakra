package net.obsidianx.chakra.layout

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import net.obsidianx.chakra.FlexNodeData
import net.obsidianx.chakra.FlexStyle
import net.obsidianx.chakra.debug.flexNodeInspectorInfo

internal fun Modifier.flexInternal(
    style: FlexStyle = FlexStyle(),
    containerNode: YogaNode? = null,
) = composed(inspectorInfo = flexNodeInspectorInfo(style)) {
    val flexNode = remember {
        YogaNodeFactory.create().also { node ->
            node.setMeasureFunction(::measureNode)
            node.data = containerNode
        }
    }

    style.apply(flexNode)
    FlexNodeData(node = flexNode)
}
