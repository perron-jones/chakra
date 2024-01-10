package net.obsidianx.chakra

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.reddit.devplatform.composables.blocks.beta.block.BlockFormattingUtil
import com.reddit.devplatform.composables.conditional
import com.reddit.devvit.ui.block_kit.v1beta.Attributes.BlockColor
import com.reddit.devvit.ui.block_kit.v1beta.Attributes.BlockSize
import com.reddit.devvit.ui.block_kit.v1beta.Attributes.BlockSizes
import com.reddit.devvit.ui.block_kit.v1beta.BlockOuterClass.BlockConfig
import com.reddit.devvit.ui.block_kit.v1beta.BlockSizesKt.DimensionKt.value as dimensionValue
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockGap
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockGap.GAP_LARGE
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockGap.GAP_MEDIUM
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockGap.GAP_NONE
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockGap.GAP_SMALL
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockHorizontalAlignment
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockHorizontalAlignment.ALIGN_CENTER
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockHorizontalAlignment.ALIGN_END
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockHorizontalAlignment.ALIGN_START
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockSizeUnit
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockStackDirection
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockStackDirection.STACK_DEPTH
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockStackDirection.STACK_HORIZONTAL
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockStackDirection.STACK_VERTICAL
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockVerticalAlignment
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockVerticalAlignment.ALIGN_BOTTOM
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockVerticalAlignment.ALIGN_MIDDLE
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockVerticalAlignment.ALIGN_TOP
import com.reddit.devvit.ui.block_kit.v1beta.backgroundColorsOrNull
import com.reddit.devvit.ui.block_kit.v1beta.colorsOrNull
import com.reddit.devvit.ui.block_kit.v1beta.heightOrNull
import com.reddit.devvit.ui.block_kit.v1beta.maxOrNull
import com.reddit.devvit.ui.block_kit.v1beta.minOrNull
import com.reddit.devvit.ui.block_kit.v1beta.valueOrNull
import com.reddit.devvit.ui.block_kit.v1beta.widthOrNull
import kotlin.math.min
import net.obsidianx.chakra.types.FlexAlign
import net.obsidianx.chakra.types.FlexDirection
import net.obsidianx.chakra.types.FlexJustify
import net.obsidianx.chakra.types.FlexOverflow
import net.obsidianx.chakra.types.FlexUnit

/**
 * Universal block modifiers, configured from the Block.size field
 *
 * Rules:
 * https://docs.google.com/document/d/1N2R-il-SB7eP-2l2-CP-Tn4EME_Im_y0k4le8H8WJwE
 */
internal fun Modifier.flexBlockStyle(
  blockSize: BlockSize?,
  blockSizes: BlockSizes?,
  allowGrow: Boolean = true,
): Modifier {
  val grow = (blockSizes?.grow ?: blockSize?.grow) == true && allowGrow
  val width = blockSizes?.widthOrNull
  val height = blockSizes?.heightOrNull
  val widthFallback = blockSize?.takeIf { it.hasWidth() }?.width?.let {
    dimensionValue {
      value = it
      unit = blockSize.widthUnit ?: BlockSizeUnit.SIZE_UNIT_PERCENT
    }
  }
  val heightFallback = blockSize?.takeIf { it.hasHeight() }?.height?.let {
    dimensionValue {
      value = it
      unit = blockSize.heightUnit ?: BlockSizeUnit.SIZE_UNIT_PERCENT
    }
  }
  return flex {
    if (grow) {
      grow(1f)
      shrink(1f)
    } else {
      grow(0f)
      shrink(0f)
    }

    val widthValue = (width?.valueOrNull ?: widthFallback)
    widthValue.applyTo(::width, ::width, ::width)
    width?.minOrNull.applyTo(::minWidth, ::minWidth, ::minWidth)
    (width?.maxOrNull ?: widthValue).applyTo(::maxWidth, ::maxWidth, ::maxWidth)

    val heightValue = (height?.valueOrNull ?: heightFallback)
    heightValue.applyTo(::height, ::height, ::height)
    height?.minOrNull.applyTo(::minHeight, ::minHeight, ::minHeight)
    (height?.maxOrNull ?: heightValue).applyTo(::maxHeight, ::maxHeight, ::maxHeight)
  }
}

internal fun Modifier.flexStackStyle(
  blockSize: BlockSize?,
  stackConfig: BlockConfig.Stack,
  darkMode: Boolean,
): Modifier {
  val stackDirection = stackConfig.direction
  val isDepth = stackDirection == STACK_DEPTH
  val flexDirection = stackConfig.flexDirection
  val padding = BlockFormattingUtil.getBlockPadding(stackConfig.padding)
  val justifyContent = stackConfig.primaryAlignment
  val alignItems = stackConfig.crossAlignment
  val gapOffset = stackConfig.flexGap * (stackConfig.childrenCount - 1)
  val hasBorder = stackConfig.hasBorder()
  val tag = stackConfig.debugTag
  val shape = stackConfig.shape
  val backgroundColor = stackConfig.run {
    resolveColor(darkMode, backgroundColorsOrNull, backgroundColor.takeIf { hasBackgroundColor() })
  }

  val borderColor = stackConfig.border.run {
      resolveColor(darkMode, colorsOrNull, color.takeIf { hasColor() }) ?: "neutral-border"
    }
  val borderWidth = BlockFormattingUtil.getBorderWidth(stackConfig.border.width)

  return flex {
    direction(flexDirection)
    /**
     * Adjust bottom/end padding according to gap offset.
     * In order for two child nodes with 50% to be of equal size after gap has been applied
     * we need to reduce the available space of the container so flexbox can correctly
     * measure 50%. This allows us to keep `flex-shrink: 0` while achieving the same visual
     * results as the built-in CSS gap. See: https://jsfiddle.net/y85haf3t/
     * Offsets are applied via `position-top`/`position-start` in [flexStackChildStyle].
     * Note: We don't apply gap to depth stacks since there's no visible distance between
     *       overlapping elements (in other words, along the z-axis).
     */
    padding(
      top = padding,
      start = padding,
      bottom = padding + (gapOffset.takeIf { stackDirection == STACK_VERTICAL } ?: 0.dp),
      end = padding + (gapOffset.takeIf { stackDirection == STACK_HORIZONTAL } ?: 0.dp),
    )
    justifyContent(justifyContent)
    alignItems(alignItems)
    overflow(FlexOverflow.Hidden)
    debugTag(tag)
    /**
     * Flexbox does not have a depth direction, so we achieve it by abusing `absolute`
     * positioning which normally removes child nodes from layout, but since every
     * node is `relative` by default the positioning is still bound by the parent container.
     * The special [depthLayout] modifier informs the layout that all children should
     * be measured to determine the intrinsic size, whether it's `absolute` or not.
     */
    depthLayout(isDepth)
    if (isDepth) {
      if (blockSize?.hasWidth() != true) {
        maxWidth(type = FlexUnit.Undefined)
      }
      if (blockSize?.hasHeight() != true) {
        maxHeight(type = FlexUnit.Undefined)
      }
    }
    val size = borderWidth.value.takeIf { it > 0 && hasBorder }?.dp
    if (size != null) {
      minHeight(dp = size)
      minWidth(dp = size)
    } else {
      minHeight(type = FlexUnit.Undefined)
      minWidth(type = FlexUnit.Undefined)
    }
  }
    .clip(shape)
    .conditional(hasBorder && borderWidth.value > 0) {
      composed {
        // ensure borders are always drawn; compose draws borders as insets so make the view big enough to contain it
        border(borderWidth, Color.Red, shape)
      }
    }
    .conditional(backgroundColor != null) {
      composed { background(Color.Gray) }
    }
}

internal fun Modifier.flexStackChildStyle(
  stackConfig: BlockConfig.Stack,
  childSize: BlockSize?,
  childSizes: BlockSizes?,
  index: Int,
): Modifier {
  val isDepth = stackConfig.direction == STACK_DEPTH
  // Ignore grow of children on z-stack since we can not yet represent expansion on z-axis
  return flexBlockStyle(childSize, childSizes, !isDepth).flex {
    /**
     * Custom gap behavior:
     * Instead of using the CSS `gap` parameter, translate nodes to their correct positions.
     * The size of the nodes will be correct since space was removed from the container with
     * the extra padding added in [flexStackStyle].
     * Note: We don't apply gap to depth stacks since there's no visible distance between
     *       overlapping elements (in other words, along the z-axis).
     */
    if (!isDepth) {
      (index * stackConfig.flexGap).takeIf { it > 0.dp }?.let { offset ->
        position(
          top = offset.takeIf { stackConfig.direction == STACK_VERTICAL } ?: 0.dp,
          start = offset.takeIf { stackConfig.direction == STACK_HORIZONTAL } ?: 0.dp,
        )
      }
    }
  }
}

/**
 * Maps [BlockStackDirection] to [FlexDirection]
 */
@VisibleForTesting
val BlockConfig.Stack.flexDirection: FlexDirection
  get() =
    when (direction ?: BlockStackDirection.UNRECOGNIZED) {
      STACK_HORIZONTAL -> if (reverse) FlexDirection.RowReverse else FlexDirection.Row
      /** Special case: flexbox doesn't have "depth" so default to "column"
       *                since the direction isn't used anyway
       */
      STACK_DEPTH,
      STACK_VERTICAL,
      BlockStackDirection.UNRECOGNIZED,
      -> if (reverse) FlexDirection.ColumnReverse else FlexDirection.Column
    }

/**
 * Get [FlexJustify] for the primary axis from the appropriate Blocks alignment axis
 */
@VisibleForTesting
val BlockConfig.Stack.primaryAlignment: FlexJustify
  get() = when (direction ?: BlockStackDirection.UNRECOGNIZED) {
    STACK_HORIZONTAL -> alignment.horizontal.takeIf { alignment.hasHorizontal() }.justifyContent
    STACK_DEPTH,
    STACK_VERTICAL,
    BlockStackDirection.UNRECOGNIZED,
    -> alignment.vertical.takeIf { alignment.hasVertical() }.justifyContent
  }

/**
 * Get [FlexAlign] for the cross axis alignment from the appropriate Blocks alignment axis
 */
@VisibleForTesting
val BlockConfig.Stack.crossAlignment: FlexAlign
  get() = when (direction ?: BlockStackDirection.UNRECOGNIZED) {
    STACK_HORIZONTAL -> alignment.vertical.takeIf { alignment.hasVertical() }.alignItems
    STACK_DEPTH -> {
      // default cross alignment should be start for z stack
      // z stack flex direction is column, so the cross axis is the x axis (horizontal)
      alignment.horizontal.takeIf {
        alignment.hasHorizontal() && alignment.horizontal != BlockHorizontalAlignment.UNRECOGNIZED
      }?.alignItems
        ?: FlexAlign.Start
    }

    STACK_VERTICAL,
    BlockStackDirection.UNRECOGNIZED,
    -> alignment.horizontal.takeIf { alignment.hasHorizontal() }.alignItems
  }

/**
 * Maps [BlockGap] to [Dp]
 */
@VisibleForTesting
val BlockConfig.Stack.flexGap: Dp
  get() = when (gap ?: BlockGap.UNRECOGNIZED) {
    BlockGap.UNRECOGNIZED,
    GAP_NONE,
    -> 0.dp

    GAP_SMALL -> 8.dp
    GAP_MEDIUM -> 16.dp
    GAP_LARGE -> 24.dp
  }

/**
 * Maps [BlockVerticalAlignment] to [FlexJustify] for VStack vertical alignment
 */
@VisibleForTesting
val BlockVerticalAlignment?.justifyContent: FlexJustify
  get() = when (this ?: BlockVerticalAlignment.UNRECOGNIZED) {
    BlockVerticalAlignment.UNRECOGNIZED,
    ALIGN_TOP,
    -> FlexJustify.Start

    ALIGN_MIDDLE -> FlexJustify.Center
    ALIGN_BOTTOM -> FlexJustify.End
  }

/**
 * Maps [BlockHorizontalAlignment] to [FlexJustify] for HStack horizontal alignment
 */
@VisibleForTesting
val BlockHorizontalAlignment?.justifyContent: FlexJustify
  get() = when (this ?: BlockHorizontalAlignment.UNRECOGNIZED) {
    BlockHorizontalAlignment.UNRECOGNIZED,
    ALIGN_START,
    -> FlexJustify.Start

    ALIGN_CENTER -> FlexJustify.Center
    ALIGN_END -> FlexJustify.End
  }

/**
 * Maps [BlockVerticalAlignment] to [FlexAlign] for VStack horizontal alignment
 */
@VisibleForTesting
val BlockVerticalAlignment?.alignItems: FlexAlign
  get() = when (this ?: BlockVerticalAlignment.UNRECOGNIZED) {
    ALIGN_TOP -> FlexAlign.Start
    ALIGN_MIDDLE -> FlexAlign.Center
    ALIGN_BOTTOM -> FlexAlign.End
    BlockVerticalAlignment.UNRECOGNIZED -> FlexAlign.Stretch
  }

/**
 * Maps [BlockHorizontalAlignment] to [FlexAlign] for HStack vertical alignment
 */
@VisibleForTesting
val BlockHorizontalAlignment?.alignItems: FlexAlign
  get() = when (this ?: BlockHorizontalAlignment.UNRECOGNIZED) {
    ALIGN_START -> FlexAlign.Start
    ALIGN_CENTER -> FlexAlign.Center
    ALIGN_END -> FlexAlign.End
    BlockHorizontalAlignment.UNRECOGNIZED -> FlexAlign.Stretch
  }

/**
 * Constructs an appropriate debug tag for a stack for when layout tree logging is enabled
 */
@VisibleForTesting
val BlockConfig.Stack.debugTag: String
  get() = when (direction) {
    STACK_HORIZONTAL -> "h"
    STACK_VERTICAL -> "v"
    STACK_DEPTH -> "z"
    else -> "?"
  }.let { "<${it}stack>" }

/**
 * Maps [BlockRadius][com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockRadius] to a clip [Shape] for stacks
 */
@VisibleForTesting
val BlockConfig.Stack.shape: Shape
  get() = if (hasCornerRadius()) {
    BlockFormattingUtil.getBlockClipShape(cornerRadius)
  } else {
    RectangleShape
  }

@VisibleForTesting
fun BlockSizes.Dimension.Value?.applyTo(asPercent: (Float) -> Unit, asDp: (Dp) -> Unit, asType: (FlexUnit) -> Unit) {
  val unit = this?.unit ?: BlockSizeUnit.SIZE_UNIT_PERCENT.takeIf { this?.value != null } ?: BlockSizeUnit.UNRECOGNIZED
  val value = this?.value ?: 0f
  when (unit) {
    BlockSizeUnit.SIZE_UNIT_PERCENT -> asPercent(min(value, 100f))
    BlockSizeUnit.SIZE_UNIT_PIXELS -> asDp(value.dp)
    BlockSizeUnit.UNRECOGNIZED -> asType(FlexUnit.Undefined)
  }
}

fun resolveColor(darkMode: Boolean, colors: BlockColor?, legacyColor: String?): String? = colors?.let {
  // Always use light color unless dark mode is set and the device is in dark mode
  if (it.hasDark() && darkMode) it.dark else it.light
} ?: legacyColor
