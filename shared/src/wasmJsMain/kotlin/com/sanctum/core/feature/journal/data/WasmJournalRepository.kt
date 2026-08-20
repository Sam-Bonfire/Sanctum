package com.sanctum.core.feature.journal.data

import com.sanctum.core.feature.journal.domain.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WasmJournalRepository : JournalRepository {
    override fun getAllEntries(): Flow<List<JournalEntry>> = flowOf(emptyList())
    override fun getEntriesByVerseId(verseId: Int): Flow<List<JournalEntry>> = flowOf(emptyList())
    override suspend fun getEntryById(id: Int): JournalEntry? = null
    override suspend fun saveEntry(entry: JournalEntry) {}
    override suspend fun deleteEntry(id: Int) {}
}
