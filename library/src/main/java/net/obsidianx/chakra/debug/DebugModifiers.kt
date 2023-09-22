package net.obsidianx.chakra.debug

import net.obsidianx.chakra.FlexboxStyleScope

fun FlexboxStyleScope.debugTag(tag: String) {
    nodeData.debugTag = tag
}

fun FlexboxStyleScope.debugDump(
    logTag: String? = null,
    flags: Set<DebugDumpFlag> = DebugDumpFlag.ALL,
) {
    nodeData.debugDumpFlags = flags
    nodeData.debugLogTag = logTag
}
