package net.obsidianx.chakra.layout

import androidx.compose.ui.unit.Constraints
import com.facebook.yoga.YogaConstants
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue

internal val YogaValue.isSet
    get() = this.unit == YogaUnit.PERCENT || this.unit == YogaUnit.POINT

internal val YogaFlexDirection.isRow
    get() = this == YogaFlexDirection.ROW || this == YogaFlexDirection.ROW_REVERSE

internal val YogaFlexDirection.isColumn
    get() = this == YogaFlexDirection.COLUMN || this == YogaFlexDirection.COLUMN_REVERSE

internal val YogaValue.constraintValue: Int?
    get() = when (unit) {
        YogaUnit.POINT -> value.toInt()
        else -> null
    }

internal fun YogaNode.getConstraints(from: Constraints, parentNode: YogaNode? = null): FloatArray {
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

internal fun YogaNode.hasDirtyDescendants(): Boolean {
    repeat(childCount) {
        val child = getChildAt(it)
        return if (child.childCount > 0 && child.hasDirtyDescendants()) {
            true
        } else {
            child.isDirty
        }
    }
    return false
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
