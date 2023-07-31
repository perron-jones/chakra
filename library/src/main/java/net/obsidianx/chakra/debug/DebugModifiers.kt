package net.obsidianx.chakra.debug

import androidx.compose.ui.Modifier
import net.obsidianx.chakra.modifiers.flexboxParentData

fun Modifier.flexDebugTag(tag: String) = flexboxParentData {
    debugTag = tag
}

fun Modifier.flexDebugDump(tag: String? = null, flags: Set<DebugDumpFlag> = DebugDumpFlag.ALL) =
    flexboxParentData {
        debugDumpFlags = flags
        debugLogTag = tag
    }