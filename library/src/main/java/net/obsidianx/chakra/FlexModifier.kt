package net.obsidianx.chakra

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaOverflow
import com.facebook.yoga.YogaPositionType
import com.facebook.yoga.YogaValue
import com.facebook.yoga.YogaWrap
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.debug.address
import net.obsidianx.chakra.layout.times
import net.obsidianx.chakra.layout.yogaValue
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexDisplay
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexGap
import net.obsidianx.chakra.types.FlexGutter
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexNodeData
import net.obsidianx.chakra.types.FlexOverflow
import net.obsidianx.chakra.types.FlexPositionType
import net.obsidianx.chakra.types.FlexUnit
import net.obsidianx.chakra.types.NodeState

internal interface FlexParentData : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): FlexNodeData
}

internal class FlexModifier(
    private val flexStyleScope: FlexStyleScope,
) : FlexParentData {
    override fun Density.modifyParentData(parentData: Any?): FlexNodeData {
        val nodeData = (parentData as? FlexNodeData) ?: FlexNodeData()
        if (nodeData.nodeState == null) {
            nodeData.nodeState = NodeState()
        }
        flexStyleScope.depthLayout?.let { nodeData.depthLayout = it }
        flexStyleScope.debugTag?.let { nodeData.debugTag = it }
        flexStyleScope.debugLogTag?.let { nodeData.debugLogTag = it }
        flexStyleScope.debugDumpFlags?.let { nodeData.debugDumpFlags = it }

        val styles = flexStyleScope.styles
        val style = nodeData.style
        (styles["flexDirection"] as? YogaFlexDirection)?.let { style.flexDirection = it }
        if (styles.containsKey("flex")) {
            (styles["flex"] as? Float).let { style.flex = it }
        }
        if (styles.containsKey("flexGrow")) {
            (styles["flexGrow"] as? Float).let { style.flexGrow = it }
        }
        if (styles.containsKey("flexShrink")) {
            (styles["flexShrink"] as? Float).let { style.flexShrink = it }
        }
        (styles["flexBasis"] as? YogaValue)?.let { style.flexBasis = it * density }
        (styles["flexWrap"] as? YogaWrap)?.let { style.flexWrap = it }
        (styles["alignItems"] as? YogaAlign)?.let { style.alignItems = it }
        (styles["alignContent"] as? YogaAlign)?.let { style.alignContent = it }
        (styles["alignSelf"] as? YogaAlign)?.let { style.alignSelf = it }
        (styles["justifyContent"] as? YogaJustify)?.let { style.justifyContent = it }
        (styles["display"] as? YogaDisplay)?.let { style.display = it }
        (styles["overflow"] as? YogaOverflow)?.let { style.overflow = it }
        (styles["positionType"] as? YogaPositionType)?.let { style.positionType = it }
        if (styles.containsKey("aspectRatio")) {
            (styles["aspectRatio"] as? Float).let { style.aspectRatio = it }
        }
        (styles["margin"] as? FlexEdges)?.let { style.margin = it * density }
        (styles["padding"] as? FlexEdges)?.let { style.padding = it * density }
        (styles["border"] as? FlexEdges)?.let { style.border = it * density }
        (styles["position"] as? FlexEdges)?.let { style.position = it * density }
        (styles["gap"] as? FlexGap)?.let { style.gap = it * density }
        (styles["width"] as? YogaValue)?.let { style.width = it * density }
        (styles["height"] as? YogaValue)?.let { style.height = it * density }
        (styles["minWidth"] as? YogaValue)?.let { style.minWidth = it * density }
        (styles["minHeight"] as? YogaValue)?.let { style.minHeight = it * density }
        (styles["maxWidth"] as? YogaValue)?.let { style.maxWidth = it * density }
        (styles["maxHeight"] as? YogaValue)?.let { style.maxHeight = it * density }

        return nodeData
    }
}

class FlexStyleScope {
    internal val styles = mutableMapOf<String, Any?>()
    internal var depthLayout: Boolean? = null
    internal var debugTag: String? = null
    internal var debugLogTag: String? = null
    internal var debugDumpFlags: Set<DebugDumpFlag>? = null

    fun direction(direction: FlexDirection) {
        styles["flexDirection"] = direction.yogaValue
    }

    fun flex(value: Float?) {
        styles["flex"] = value
    }

    fun grow(value: Float?) {
        styles["flexGrow"] = value
    }

    fun shrink(value: Float?) {
        styles["flexShrink"] = value
    }

    fun basis(dp: Dp) {
        styles["flexBasis"] = dp.yogaValue
    }

    fun basis(percent: Float) {
        styles["flexBasis"] = percent.yogaValue
    }

    fun basis(type: FlexUnit) {
        styles["flexBasis"] = type.toYogaValue
    }

    fun wrap(wrap: Boolean, reverse: Boolean = false) {
        val yogaWrap = if (wrap) {
            if (reverse) {
                YogaWrap.WRAP_REVERSE
            } else {
                YogaWrap.WRAP
            }
        } else {
            YogaWrap.NO_WRAP
        }
        styles["flexWrap"] = yogaWrap
    }

    fun alignItems(flexAlign: FlexAlign) {
        styles["alignItems"] = flexAlign.yogaValue
    }

    fun alignContent(flexAlign: FlexAlign) {
        styles["alignContent"] = flexAlign.yogaValue
    }

    fun alignSelf(flexAlign: FlexAlign) {
        styles["alignSelf"] = flexAlign.yogaValue
    }

    fun justifyContent(flexJustify: FlexJustify) {
        styles["justifyContent"] = flexJustify.yogaValue
    }

    fun display(flexDisplay: FlexDisplay) {
        styles["display"] = flexDisplay.yogaValue
    }

    fun overflow(flexOverflow: FlexOverflow) {
        styles["overflow"] = flexOverflow.yogaValue
    }

    fun positionType(flexPositionType: FlexPositionType) {
        styles["positionType"] = flexPositionType.yogaValue
    }

    fun aspectRatio(value: Float?) {
        styles["aspectRatio"] = value
    }

    fun margin(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
        styles["margin"] = FlexEdges(
            start = start.yogaValue,
            top = top.yogaValue,
            end = end.yogaValue,
            bottom = bottom.yogaValue,
        )
    }

    fun margin(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
        styles["margin"] = FlexEdges(
            horizontal = horizontal.yogaValue,
            vertical = vertical.yogaValue,
        )
    }

    fun margin(all: Dp = 0.dp) {
        styles["margin"] = FlexEdges(all = all.yogaValue)
    }

    fun padding(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
        styles["padding"] = FlexEdges(
            start = start.yogaValue,
            top = top.yogaValue,
            end = end.yogaValue,
            bottom = bottom.yogaValue,
        )
    }

    fun padding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
        styles["padding"] = FlexEdges(
            horizontal = horizontal.yogaValue,
            vertical = vertical.yogaValue,
        )
    }

    fun padding(all: Dp = 0.dp) {
        styles["padding"] = FlexEdges(all = all.yogaValue)
    }

    fun border(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
        styles["border"] = FlexEdges(
            start = start.yogaValue,
            top = top.yogaValue,
            end = end.yogaValue,
            bottom = bottom.yogaValue,
        )
    }

    fun border(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
        styles["border"] = FlexEdges(
            horizontal = horizontal.yogaValue,
            vertical = vertical.yogaValue,
        )
    }

    fun border(all: Dp = 0.dp) {
        styles["border"] = FlexEdges(all = all.yogaValue)
    }

    fun position(start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp) {
        styles["position"] = FlexEdges(
            start = start.yogaValue,
            top = top.yogaValue,
            end = end.yogaValue,
            bottom = bottom.yogaValue,
        )
    }

    fun position(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) {
        styles["position"] = FlexEdges(
            horizontal = horizontal.yogaValue,
            vertical = vertical.yogaValue,
        )
    }

    fun position(all: Dp = 0.dp) {
        styles["position"] = FlexEdges(all = all.yogaValue)
    }

    fun gap(all: Dp? = null, horizontal: Dp? = null, vertical: Dp? = null) {
        if (listOf(all, horizontal, vertical).count { it != null } > 1) {
            throw IllegalArgumentException("Only set one kind of gap in FlexboxStyleScope.gap")
        }
        val gutter: FlexGutter
        val value: Float
        when {
            vertical != null -> {
                gutter = FlexGutter.Row
                value = vertical.value
            }

            horizontal != null -> {
                gutter = FlexGutter.Column
                value = horizontal.value
            }

            else -> {
                gutter = FlexGutter.All
                value = all?.value ?: 0f
            }
        }
        styles["gap"] = FlexGap(gutter = gutter, amount = value)
    }

    fun width(dp: Dp) {
        styles["width"] = dp.yogaValue
    }

    fun width(percent: Float) {
        styles["width"] = percent.yogaValue
    }

    fun width(type: FlexUnit) {
        styles["width"] = type.toYogaValue
    }

    fun height(dp: Dp) {
        styles["height"] = dp.yogaValue
    }

    fun height(percent: Float) {
        styles["height"] = percent.yogaValue
    }

    fun height(type: FlexUnit) {
        styles["height"] = type.toYogaValue
    }

    fun minWidth(dp: Dp) {
        styles["minWidth"] = dp.yogaValue
    }

    fun minWidth(percent: Float) {
        styles["minWidth"] = percent.yogaValue
    }

    fun minWidth(type: FlexUnit) {
        styles["minWidth"] = type.toYogaValue
    }

    fun minHeight(dp: Dp) {
        styles["minHeight"] = dp.yogaValue
    }

    fun minHeight(percent: Float) {
        styles["minHeight"] = percent.yogaValue
    }

    fun minHeight(type: FlexUnit) {
        styles["minHeight"] = type.toYogaValue
    }

    fun maxWidth(dp: Dp) {
        styles["maxWidth"] = dp.yogaValue
    }

    fun maxWidth(percent: Float) {
        styles["maxWidth"] = percent.yogaValue
    }

    fun maxWidth(type: FlexUnit) {
        styles["maxWidth"] = type.toYogaValue
    }

    fun maxHeight(dp: Dp) {
        styles["maxHeight"] = dp.yogaValue
    }

    fun maxHeight(percent: Float) {
        styles["maxHeight"] = percent.yogaValue
    }

    fun maxHeight(type: FlexUnit) {
        styles["maxHeight"] = type.toYogaValue
    }

    fun depthLayout(enabled: Boolean = true) {
        depthLayout = enabled
    }

    fun debugTag(tag: String) {
        debugTag = tag
    }

    fun debugDump(logTag: String? = null, flags: Set<DebugDumpFlag> = DebugDumpFlag.ALL) {
        debugLogTag = logTag
        debugDumpFlags = flags
    }
}

fun Modifier.flex(block: FlexStyleScope.() -> Unit): Modifier = inspectable({
    name = "flex"
    val scope = FlexStyleScope().apply { block() }
    for (entry in scope.styles.entries) {
        properties[entry.key] = entry.value
    }
}) {
    val scope = FlexStyleScope().apply { block() }
    FlexModifier(scope)
}

internal fun Modifier.flexContainer(node: YogaNode) = then(object : FlexParentData {
    override fun Density.modifyParentData(parentData: Any?): FlexNodeData =
        ((parentData as? FlexNodeData) ?: FlexNodeData()).apply {
            (nodeState ?: NodeState()).apply {
                isContainer = true
                this.node = node
                nodeState = this
            }
        }
})
