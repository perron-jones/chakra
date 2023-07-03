package net.obsidianx.chakra

import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory

data class FlexNodeData(
    val node: YogaNode = YogaNodeFactory.create(),
    val containerRoot: YogaNode? = null,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@FlexNodeData
}
