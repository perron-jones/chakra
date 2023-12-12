package net.obsidianx.chakra.types

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.debug.DebugDumpFlag

internal data class NodeState(
    var node: YogaNode? = null,
    var isContainer: Boolean = false,
    var child: Boolean = false,
    var synced: Boolean = false,
    var constraints: Constraints = Constraints(),
)

internal data class FlexNodeData(
    var style: FlexboxStyle = FlexboxStyle(),
    var debugTag: String = "",
    var debugDumpFlags: Set<DebugDumpFlag>? = null,
    var debugLogTag: String? = null,
    var intrinsicMax: IntSize? = null,
    var nodeState: NodeState? = null,
    var depthLayout: Boolean = false,
)

internal var FlexNodeData?.isContainer: Boolean
    get() = this?.nodeState?.isContainer == true
    set(value) {
        this?.let {
            (nodeState ?: NodeState().also { nodeState = it }).isContainer = value
        }
    }

internal val FlexNodeData.isChild: Boolean
    get() = this.nodeState?.child == true