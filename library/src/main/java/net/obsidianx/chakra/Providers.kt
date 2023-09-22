package net.obsidianx.chakra

import androidx.compose.ui.modifier.modifierLocalOf
import net.obsidianx.chakra.debug.DebugDumpFlag

internal val ModifierLocalDebugDumpFlags = modifierLocalOf<Set<DebugDumpFlag>?> { null }
