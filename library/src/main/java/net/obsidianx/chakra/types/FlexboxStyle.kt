package net.obsidianx.chakra.types

import com.facebook.yoga.YogaAlign
import com.facebook.yoga.YogaDisplay
import com.facebook.yoga.YogaFlexDirection
import com.facebook.yoga.YogaJustify
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaOverflow
import com.facebook.yoga.YogaPositionType
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue
import com.facebook.yoga.YogaWrap

internal val YOGA_AUTO = YogaValue(Float.NaN, YogaUnit.AUTO)
internal val YOGA_UNDEFINED = YogaValue(Float.NaN, YogaUnit.UNDEFINED)

data class FlexboxStyle(
    var flexDirection: YogaFlexDirection = YogaFlexDirection.COLUMN,
    var flex: Float? = 0f,
    var flexGrow: Float? = 0f,
    var flexShrink: Float? = 1f,
    var flexBasis: YogaValue = YOGA_AUTO,
    var flexWrap: YogaWrap = YogaWrap.NO_WRAP,
    var alignItems: YogaAlign = YogaAlign.STRETCH,
    var alignContent: YogaAlign = YogaAlign.FLEX_START,
    var alignSelf: YogaAlign = YogaAlign.AUTO,
    var justifyContent: YogaJustify = YogaJustify.FLEX_START,
    var display: YogaDisplay = YogaDisplay.FLEX,
    var overflow: YogaOverflow = YogaOverflow.VISIBLE,
    var positionType: YogaPositionType = YogaPositionType.RELATIVE,
    var aspectRatio: Float? = null,
    var margin: FlexEdges = FlexEdges(),
    var padding: FlexEdges = FlexEdges(),
    var border: FlexEdges = FlexEdges(),
    var position: FlexEdges = FlexEdges(),
    var gap: FlexGap = FlexGap(),
    var width: YogaValue = YOGA_AUTO,
    var height: YogaValue = YOGA_AUTO,
    var minWidth: YogaValue = YOGA_UNDEFINED,
    var minHeight: YogaValue = YOGA_UNDEFINED,
    var maxWidth: YogaValue = YOGA_UNDEFINED,
    var maxHeight: YogaValue = YOGA_UNDEFINED,
) {
    fun apply(node: YogaNode) {
        node.flexDirection = flexDirection
        node.flex = flex ?: Float.NaN
        node.flexGrow = flexGrow ?: Float.NaN
        node.flexShrink = flexShrink ?: Float.NaN
        node.wrap = flexWrap
        flexBasis.apply(
            node::setFlexBasis,
            node::setFlexBasisPercent,
            node::setFlexBasisAuto
        )

        node.alignItems = alignItems
        node.alignContent = alignContent
        node.alignSelf = alignSelf
        node.justifyContent = justifyContent

        node.display = display
        node.overflow = overflow
        node.positionType = positionType
        node.aspectRatio = aspectRatio ?: Float.NaN
        node.setGap(gap.gutter.yogaValue, gap.amount)

        margin.apply(node::setMargin, node::setMarginPercent, node::setMarginAuto)
        padding.apply(node::setPadding, node::setPaddingPercent)
        border.apply(node::setBorder)
        position.apply(node::setPosition, node::setPositionPercent)

        width.apply(node::setWidth, node::setWidthPercent, node::setWidthAuto)
        height.apply(node::setHeight, node::setHeightPercent, node::setHeightAuto)
        minWidth.apply(node::setMinWidth, node::setMinWidthPercent)
        minHeight.apply(node::setMinHeight, node::setMinHeightPercent)
        maxWidth.apply(node::setMaxWidth, node::setMaxWidthPercent)
        maxHeight.apply(node::setMaxHeight, node::setMaxHeightPercent)
    }
}

private fun YogaValue.apply(
    setPoint: (Float) -> Unit,
    setPercent: (Float) -> Unit,
    setAuto: (() -> Unit)? = null,
    clear: (() -> Unit)? = null
) {
    when (unit) {
        YogaUnit.POINT -> setPoint(value)
        YogaUnit.PERCENT -> setPercent(value)
        YogaUnit.AUTO -> setAuto?.invoke() ?: setPoint(Float.NaN)
        else -> (clear ?: setAuto)?.invoke() ?: setPoint(Float.NaN)
    }
}
