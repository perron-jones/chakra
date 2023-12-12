package net.obsidianx.chakra.layout

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import com.facebook.yoga.YogaConstants
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaGutter
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexGap
import net.obsidianx.chakra.types.FlexNodeData
import net.obsidianx.chakra.types.isContainer
import kotlin.math.ceil

internal val YogaValue.isSet
    get() = unit == YogaUnit.PERCENT || unit == YogaUnit.POINT

internal val YogaValue.asFloatOrZero: Float
    get() = value.takeIf { unit == YogaUnit.POINT } ?: 0f

internal val YogaValue.asFloatOrNan: Float
    get() = value.takeIf { unit == YogaUnit.POINT } ?: Float.NaN

internal val YogaFlexDirection.isRow
    get() = this == YogaFlexDirection.ROW || this == YogaFlexDirection.ROW_REVERSE

internal val YogaFlexDirection.isColumn
    get() = this == YogaFlexDirection.COLUMN || this == YogaFlexDirection.COLUMN_REVERSE

fun YogaNode.getConstraints(from: Constraints, parentNode: YogaNode? = null): FloatArray {
    val maxWidth = from.maxWidth
        .takeIf {
            from.hasBoundedWidth &&
                    (flexGrow > 0f || flexBasis.isSet || parentNode?.flexDirection?.isRow != true)
        }
        ?.toFloat()
        ?: YogaConstants.UNDEFINED
    val maxHeight = from.maxHeight
        .takeIf {
            from.hasBoundedHeight &&
                    (flexGrow > 0f || flexBasis.isSet || parentNode?.flexDirection?.isColumn != true)
        }
        ?.toFloat()
        ?: YogaConstants.UNDEFINED
    return floatArrayOf(maxWidth, maxHeight)
}

internal fun YogaNode.getChildOrNull(index: Int): YogaNode? {
    return if (childCount > index) {
        getChildAt(index)
    } else {
        null
    }
}

internal fun YogaNode.deepDirty() {
    repeat(childCount) {
        val child = getChildAt(it)
        if (child.isMeasureDefined) {
            child.dirty()
        } else if (child.childCount > 0) {
            child.deepDirty()
        }
    }
}

internal fun YogaNode.removeAllChildren() {
    repeat(childCount) {
        removeChildAt(0).apply {
            if (childCount > 0) {
                removeAllChildren()
            }
        }
    }
}

internal val YogaNode.horizontalPadding: Float
    get() = getPadding(YogaEdge.ALL).takeIf { it.isSet }?.asFloatOrZero?.let { it * 2 }
        ?: getPadding(YogaEdge.HORIZONTAL).takeIf { it.isSet }?.asFloatOrZero?.let { it * 2 }
        ?: (getPadding(YogaEdge.START).asFloatOrZero + getPadding(YogaEdge.END).asFloatOrZero)

internal val YogaNode.verticalPadding: Float
    get() = getPadding(YogaEdge.ALL).takeIf { it.isSet }?.asFloatOrZero?.let { it * 2 }
        ?: getPadding(YogaEdge.VERTICAL).takeIf { it.isSet }?.asFloatOrZero?.let { it * 2 }
        ?: (getPadding(YogaEdge.TOP).asFloatOrZero + getPadding(YogaEdge.BOTTOM).asFloatOrZero)

val YogaNode.layoutHorizontalPadding: Float
    get() = ceil(getLayoutPadding(YogaEdge.START) + getLayoutPadding(YogaEdge.END))

val YogaNode.layoutVerticalPadding: Float
    get() = ceil(getLayoutPadding(YogaEdge.TOP) + getLayoutPadding(YogaEdge.BOTTOM))

internal val YogaNode.horizontalGap: Float
    get() = getGap(YogaGutter.ALL).takeUnless { it.isNaN() }
        ?: getGap(YogaGutter.COLUMN).takeUnless { it.isNaN() } ?: 0f

internal val YogaNode.verticalGap: Float
    get() = getGap(YogaGutter.ALL).takeUnless { it.isNaN() }
        ?: getGap(YogaGutter.ROW).takeUnless { it.isNaN() } ?: 0f

internal val YogaNode.isContainer: Boolean
    get() = (data as? FlexNodeData).isContainer

internal val Dp.yogaValue: YogaValue
    get() = YogaValue(value, YogaUnit.POINT)

internal val Float.yogaValue: YogaValue
    get() = YogaValue(this, YogaUnit.PERCENT)

internal operator fun YogaValue.times(operand: Float): YogaValue =
    if (unit == YogaUnit.POINT) {
        YogaValue(value * operand, unit)
    } else {
        this
    }

internal operator fun FlexEdges.times(operand: Float): FlexEdges =
    FlexEdges(
        left = left * operand,
        top = top * operand,
        right = right * operand,
        bottom = bottom * operand,
        start = start * operand,
        end = end * operand,
        horizontal = horizontal * operand,
        vertical = vertical * operand,
        all = all * operand,
    )

internal operator fun FlexGap.times(operand: Float): FlexGap =
    copy(
        amount = amount * operand,
    )

internal val YogaNode.childDepthLayouts: List<YogaNode>
    get() {
        val nodes = mutableListOf<YogaNode>()
        if (childCount == 0) {
            return nodes
        }
        if ((data as? FlexNodeData)?.depthLayout == true) {
            nodes += this
        }
        repeat(childCount) { index ->
            nodes += getChildAt(index).childDepthLayouts
        }
        return nodes
    }
