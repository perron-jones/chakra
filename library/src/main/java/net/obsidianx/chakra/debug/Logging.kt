package net.obsidianx.chakra.debug

import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.layout.isSet
import net.obsidianx.chakra.types.FlexNodeData

enum class DebugDumpFlag {
    Alignment,
    Flexbox,
    Dimensions,
    DimensionsExtras,
    Constraints,
    Layout,
    LayoutExtras,
    Unset;

    companion object {
        val TREE_ONLY = setOf<DebugDumpFlag>()
        val ALL = setOf(
            Alignment,
            Flexbox,
            Dimensions,
            DimensionsExtras,
            Constraints,
            Layout,
            LayoutExtras,
            Unset,
        )
        val ALL_SET = ALL - Unset
    }
}

internal const val DEFAULT_LOG_TAG = "Chakra"
private val EDGES = arrayOf(YogaEdge.TOP, YogaEdge.END, YogaEdge.BOTTOM, YogaEdge.START)

internal val Any?.address: String
    get() = System.identityHashCode(this).toString(16)

private fun getEdges(withUnset: Boolean, getter: (YogaEdge) -> Any): String =
    EDGES.mapNotNull { edge ->
        getter(edge).takeIf { withUnset || (it as? YogaValue)?.isSet ?: ((it as? Float)?.isNaN() != true) }
            ?.let { value -> "${edge.toString().lowercase()}: $value" }
    }.joinToString()

internal fun YogaNode.dump(flags: Set<DebugDumpFlag> = DebugDumpFlag.ALL, depth: Int = 0): String {
    val nodeData = data as? FlexNodeData
    val name = nodeData?.debugTag ?: "Node"

    val children = mutableListOf<String>()
    repeat(childCount) {
        // Allow child nodes to override flags for the subtree
        children += getChildAt(it).dump(nodeData?.debugDumpFlags ?: flags, depth + 2)
    }
    val indent = "".padStart(depth * 2, ' ')
    val isContainer = !isMeasureDefined
    val withUnset = DebugDumpFlag.Unset in flags

    val alignmentContainerConfig = """
        |$indent  Align items: $alignItems
        |$indent  Align content: $alignContent
        |$indent  Justify content: $justifyContent
    """.trimMargin()

    val alignmentNodeConfig = """
        |$indent  Align self: $alignSelf
    """.trimMargin()

    val flexContainerConfig = """
        |$indent  Flex direction: $flexDirection
        |$indent  Flex wrap: $wrap
    """.trimMargin()

    val flexNodeConfig = """
        |$indent  Flex: $flex
        |$indent  Flex shrink: $flexShrink
        |$indent  Flex grow: $flexGrow
        |$indent  Flex basis: $flexBasis
    """.trimMargin()

    val containerChildrenConfig =
        mutableListOf<String>().run {
            if (flags.isNotEmpty()) {
                add("$indent  Children: $childCount")
            }
            addAll(children)
            joinToString("\n")
        }

    val dimensionsConfig = arrayOf(
        "$indent  Width: $width".takeIf { withUnset || width.isSet },
        "$indent  Height: $height".takeIf { withUnset || height.isSet },
    ).filterNotNull().joinToString("\n")

    val dimensionExtrasConfig = mutableListOf<String>().apply {
        getEdges(withUnset, ::getMargin).takeIf { it.isNotEmpty() }?.let { margin ->
            add("$indent  Margin: $margin")
        }
        getEdges(withUnset, ::getPadding).takeIf { it.isNotEmpty() }?.let { padding ->
            add("$indent  Padding: $padding")
        }
        getEdges(withUnset, ::getBorder).takeIf { it.isNotEmpty() }?.let { border ->
            add("$indent  Border: $border")
        }
    }.joinToString("\n")

    val constraintsConfig = arrayOf(
        "$indent  Min width: $minWidth".takeIf { withUnset || minWidth.isSet },
        "$indent  Min height: $minHeight".takeIf { withUnset || minHeight.isSet },
        "$indent  Max width: $maxWidth".takeIf { withUnset || maxWidth.isSet },
        "$indent  Max height: $maxHeight".takeIf { withUnset || maxHeight.isSet },
    ).filterNotNull().joinToString("\n")

    val layoutConfig = """
        |$indent  Layout width: $layoutWidth
        |$indent  Layout height: $layoutHeight
        |$indent  Layout X: $layoutX
        |$indent  Layout Y: $layoutY
    """.trimMargin()

    val layoutExtrasConfig = """
        |$indent  Layout margin: ${getEdges(withUnset, ::getLayoutMargin)}
        |$indent  Layout padding: ${getEdges(withUnset, ::getLayoutPadding)}
        |$indent  Layout border: ${getEdges(withUnset, ::getLayoutBorder)}
    """.trimMargin()

    val config = mutableListOf("${indent}Node '$name' ($address)")
    if (DebugDumpFlag.Layout in flags) {
        config.add(layoutConfig)
    }
    if (DebugDumpFlag.LayoutExtras in flags && layoutExtrasConfig.isNotEmpty()) {
        config.add(layoutExtrasConfig)
    }
    if (DebugDumpFlag.Dimensions in flags && dimensionsConfig.isNotEmpty()) {
        config.add(dimensionsConfig)
    }
    if (DebugDumpFlag.DimensionsExtras in flags && dimensionExtrasConfig.isNotEmpty()) {
        config.add(dimensionExtrasConfig)
    }
    if (DebugDumpFlag.Constraints in flags && constraintsConfig.isNotEmpty()) {
        config.add(constraintsConfig)
    }
    if (DebugDumpFlag.Flexbox in flags) {
        if (isContainer && flexContainerConfig.isNotEmpty()) {
            config.add(flexContainerConfig)
        }
        if (flexNodeConfig.isNotEmpty()) {
            config.add(flexNodeConfig)
        }
    }
    if (DebugDumpFlag.Alignment in flags) {
        if (isContainer && alignmentContainerConfig.isNotEmpty()) {
            config.add(alignmentContainerConfig)
        }
        if (alignmentNodeConfig.isNotEmpty()) {
            config.add(alignmentNodeConfig)
        }
    }
    if (isContainer) {
        config.add(containerChildrenConfig)
    }

    return config.joinToString(separator = "\n")
}
