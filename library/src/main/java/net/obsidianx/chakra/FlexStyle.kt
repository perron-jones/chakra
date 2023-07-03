package net.obsidianx.chakra

import com.facebook.yoga.YogaNode
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexDisplay
import net.obsidianx.chakra.types.FlexEdges
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexOverflow
import net.obsidianx.chakra.types.FlexPositionType
import net.obsidianx.chakra.types.FlexValue
import net.obsidianx.chakra.types.FlexWrap

data class FlexStyle(
    val flexDirection: FlexDirection = FlexDirection.Column,
    val justifyContent: FlexJustify = FlexJustify.Start,
    val alignItems: FlexAlign = FlexAlign.Stretch,
    val alignContent: FlexAlign = FlexAlign.Start,
    val wrap: FlexWrap = FlexWrap.NoWrap,
    val alignSelf: FlexAlign = FlexAlign.Auto,
    val positionType: FlexPositionType = FlexPositionType.Relative,
    val overflow: FlexOverflow = FlexOverflow.Visible,
    val display: FlexDisplay = FlexDisplay.Flex,
    val flex: Float? = 0f,
    val flexGrow: Float? = 0f,
    val flexShrink: Float? = 0f,
    val flexBasis: FlexValue = FlexValue.Auto,
    val margin: FlexEdges = FlexEdges(),
    val padding: FlexEdges = FlexEdges(),
    val border: FlexEdges = FlexEdges(),
    val position: FlexEdges = FlexEdges(),
    val width: FlexValue = FlexValue.Undefined,
    val height: FlexValue = FlexValue.Undefined,
    val minWidth: FlexValue = FlexValue.Undefined,
    val minHeight: FlexValue = FlexValue.Undefined,
    val maxWidth: FlexValue = FlexValue.Undefined,
    val maxHeight: FlexValue = FlexValue.Undefined,
    val aspectRatio: Float? = null,
) {
    fun apply(node: YogaNode) {
        node.alignSelf = alignSelf.yogaValue
        node.positionType = positionType.yogaValue
        node.overflow = overflow.yogaValue
        node.display = display.yogaValue
        node.flexDirection = flexDirection.yogaValue
        node.justifyContent = justifyContent.yogaValue
        node.alignItems = alignItems.yogaValue
        node.alignContent = alignContent.yogaValue
        node.wrap = wrap.yogaValue

        node.flex = flex ?: Float.NaN
        node.flexGrow = flexGrow ?: Float.NaN
        node.flexShrink = flexShrink ?: Float.NaN
        node.aspectRatio = aspectRatio ?: Float.NaN

        flexBasis.apply(node::setFlexBasis, node::setFlexBasisPercent, node::setFlexBasisAuto)
        width.apply(node::setWidth, node::setWidthPercent, node::setWidthAuto)
        height.apply(node::setHeight, node::setHeightPercent, node::setHeightAuto)
        minWidth.apply(node::setMinWidth, node::setMinWidthPercent)
        minHeight.apply(node::setMinHeight, node::setMinHeightPercent)
        maxWidth.apply(node::setMaxWidth, node::setMaxWidthPercent)
        maxHeight.apply(node::setMaxHeight, node::setMaxHeightPercent)

        margin.apply(node::setMargin, node::setMarginPercent)
        padding.apply(node::setPadding, node::setPaddingPercent)
        border.apply(node::setBorder)
        position.apply(node::setPosition, node::setPositionPercent)
    }
}
