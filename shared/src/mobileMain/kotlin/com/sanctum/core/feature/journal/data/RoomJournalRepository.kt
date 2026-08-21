package com.sanctum.core.feature.journal.data

import com.sanctum.core.core.database.JournalDao
import com.sanctum.core.core.database.JournalEntryEntity
import com.sanctum.core.feature.journal.domain.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomJournalRepository(
    private val journalDao: JournalDao,
) : JournalRepository {

    override fun getAllEntries(): Flow<List<JournalEntry>> =
        journalDao.getAllEntries().map { entities -> entities.map { it.toDomain() } }

    override fun getEntriesByVerseId(verseId: Int): Flow<List<JournalEntry>> =
        journalDao.getEntriesByVerseId(verseId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getEntryById(id: Int): JournalEntry? =
        journalDao.getEntryById(id)?.toDomain()

    override suspend fun saveEntry(entry: JournalEntry) {
        journalDao.insertEntry(entry.toEntity())
    }

    override suspend fun deleteEntry(id: Int) {
        journalDao.deleteEntry(id)
    }

    private fun JournalEntryEntity.toDomain() = JournalEntry(
        id = id,
        verseId = verseId,
        chapterId = chapterId,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        moodTags = if (moodTags.isEmpty()) emptyList() else moodTags.split(","),
    )

    private fun JournalEntry.toEntity() = JournalEntryEntity(
        id = id,
        verseId = verseId,
        chapterId = chapterId,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        moodTags = moodTags.joinToString(","),
    )
}
