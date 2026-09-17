package com.sanctum.core.feature.journal.domain

import kotlinx.serialization.Serializable

@Serializable
data class JournalTag(
    val id: String,
    val name: String,
    val colorHex: String = "",
)

@Serializable
data class JournalFolder(
    val id: String,
    val name: String,
    val entryIds: List<Int> = emptyList(),
)

fun JournalFolder.addEntry(entryId: Int): JournalFolder {
    if (entryId in entryIds) return this
    return copy(entryIds = entryIds + entryId)
}

fun JournalFolder.removeEntry(entryId: Int): JournalFolder = copy(entryIds = entryIds - entryId)

fun entriesWithTag(entries: List<JournalEntry>, tag: String): List<JournalEntry> =
    entries.filter { entry -> entry.moodTags.any { it.equals(tag, ignoreCase = true) } }
