package com.reddit.devplatform.composables.blocks.beta.block

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockBorderWidth
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockPadding
import com.reddit.devvit.ui.block_kit.v1beta.Enums.BlockRadius

/**
 * Utility for providing formatting for Custom Post Blocks
 */
internal object BlockFormattingUtil {

  /**
   * Returns the appropriate measurement for a [Block]'s border width based on the provided [width].
   *
   * @param width see [BlockBorderWidth]
   */
  fun getBorderWidth(width: BlockBorderWidth?): Dp {
    return when (width) {
      BlockBorderWidth.BORDER_WIDTH_NONE -> 0.dp
      BlockBorderWidth.BORDER_WIDTH_THIN -> 1.dp
      BlockBorderWidth.BORDER_WIDTH_THICK -> 2.dp
      BlockBorderWidth.UNRECOGNIZED,
      null,
      -> 0.dp
    }
  }

  /**
   * Returns the appropriate [Shape] for a [Block] based on the provided [cornerRadius].
   *
   * @param cornerRadius see [BlockRadius]
   */
  fun getBlockClipShape(cornerRadius: BlockRadius?): Shape {
    return when (cornerRadius) {
      BlockRadius.RADIUS_NONE -> RectangleShape
      BlockRadius.RADIUS_SMALL -> RoundedCornerShape(size = 8.dp)
      BlockRadius.RADIUS_MEDIUM -> RoundedCornerShape(size = 16.dp)
      BlockRadius.RADIUS_LARGE -> RoundedCornerShape(size = 24.dp)
      BlockRadius.RADIUS_FULL -> RoundedCornerShape(percent = 100)
      BlockRadius.UNRECOGNIZED,
      null,
      -> RectangleShape
    }
  }

  /**
   * Returns the appropriate [Dp] padding size for a [Block] based on the provided [padding].
   *
   * @param padding see [BlockPadding]
   */
  fun getBlockPadding(padding: BlockPadding?): Dp {
    return when (padding) {
      null,
      BlockPadding.UNRECOGNIZED,
      BlockPadding.PADDING_NONE,
      -> 0.dp
      BlockPadding.PADDING_XSMALL -> 4.dp
      BlockPadding.PADDING_SMALL -> 8.dp
      BlockPadding.PADDING_MEDIUM -> 16.dp
      BlockPadding.PADDING_LARGE -> 32.dp
    }
  }
}
