package com.reddit.devplatform.composables

import androidx.compose.ui.Modifier

internal fun Modifier.conditional(predicate: Boolean, block: Modifier.() -> Modifier): Modifier = this.then(
  if (predicate) {
    this.block()
  } else {
    this
  },
)
