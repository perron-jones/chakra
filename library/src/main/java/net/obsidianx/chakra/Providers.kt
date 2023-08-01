package net.obsidianx.chakra

import androidx.compose.runtime.compositionLocalOf
import net.obsidianx.chakra.debug.DebugDumpFlag

internal val LocalDebugDumpFlags = compositionLocalOf<Set<DebugDumpFlag>?> { null }
