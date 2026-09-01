package com.sanctum.core.feature.sync.domain

import com.sanctum.core.feature.journal.domain.JournalEntry
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.Highlight
import com.sanctum.core.feature.scripture.domain.Note
import kotlinx.serialization.Serializable

@Serializable
data class BackupPayload(
    val version: Int = 1,
    val lastSyncTimestampMs: Long,
    val bookmarks: List<Bookmark> = emptyList(),
    val notes: List<Note> = emptyList(),
    val highlights: List<Highlight> = emptyList(),
    val journalEntries: List<JournalEntry> = emptyList(),
)
