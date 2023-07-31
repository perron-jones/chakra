package net.obsidianx.chakra

import androidx.compose.runtime.compositionLocalOf
import net.obsidianx.chakra.debug.DebugDumpFlag
import net.obsidianx.chakra.layout.FlexLayoutState

internal val LocalFlexLayoutState = compositionLocalOf<FlexLayoutState?> { null }

internal val LocalDebugDumpFlags = compositionLocalOf<Set<DebugDumpFlag>?> { null }
