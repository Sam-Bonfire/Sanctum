package com.sanctum.core.feature.journal.data

import com.sanctum.core.feature.journal.domain.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getAllEntries(): Flow<List<JournalEntry>>
    fun getEntriesByVerseId(verseId: Int): Flow<List<JournalEntry>>
    suspend fun getEntryById(id: Int): JournalEntry?
    suspend fun saveEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: Int)
}
