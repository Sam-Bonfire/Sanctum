package com.sanctum.core.feature.sync.data

import com.sanctum.core.core.database.BookmarkEntity
import com.sanctum.core.core.database.HighlightEntity
import com.sanctum.core.core.database.JournalDao
import com.sanctum.core.core.database.JournalEntryEntity
import com.sanctum.core.core.database.NoteEntity
import com.sanctum.core.core.database.UserDataDao
import com.sanctum.core.feature.journal.domain.JournalEntry
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.Highlight
import com.sanctum.core.feature.scripture.domain.Note
import com.sanctum.core.feature.sync.domain.BackupPayload
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataExporter(
    private val userDataDao: UserDataDao,
    private val journalDao: JournalDao,
) {

    suspend fun exportDataToJson(): String {
        // Collects the most recent state from Room
        val bookmarks = userDataDao.getAllBookmarks().first()
        val notes = userDataDao.getAllNotes().first()
        val highlights = userDataDao.getAllHighlights().first()
        val journalEntries = journalDao.getAllEntries().first()

        val payload = BackupPayload(
            version = 1,
            lastSyncTimestampMs = Clock.System.now().toEpochMilliseconds(),
            bookmarks = bookmarks.map { Bookmark(it.id, it.verseId, it.timestampMs) },
            notes = notes.map { Note(it.id, it.verseId, it.content, it.timestampMs) },
            highlights = highlights.map { Highlight(it.id, it.verseId, it.colorHex, it.timestampMs) },
            journalEntries = journalEntries.map {
                JournalEntry(
                    id = it.id,
                    verseId = it.verseId,
                    chapterId = it.chapterId,
                    title = it.title,
                    content = it.content,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    moodTags = it.moodTags.split(",").filter { tag -> tag.isNotBlank() },
                )
            },
        )
        return Json.encodeToString(payload)
    }

    suspend fun importDataFromJson(jsonString: String) {
        val payload = Json.decodeFromString<BackupPayload>(jsonString)

        // Merge Bookmarks
        val localBookmarks = userDataDao.getAllBookmarks().first().associateBy { it.id }
        payload.bookmarks.forEach { cloudBookmark ->
            val local = localBookmarks[cloudBookmark.id]
            if (local == null || cloudBookmark.timestampMs > local.timestampMs) {
                userDataDao.addBookmark(BookmarkEntity(cloudBookmark.id, cloudBookmark.verseId, cloudBookmark.timestampMs))
            }
        }

        // Merge Notes
        val localNotes = userDataDao.getAllNotes().first().associateBy { it.id }
        payload.notes.forEach { cloudNote ->
            val local = localNotes[cloudNote.id]
            if (local == null || cloudNote.timestampMs > local.timestampMs) {
                userDataDao.addNote(NoteEntity(cloudNote.id, cloudNote.verseId, cloudNote.content, cloudNote.timestampMs))
            }
        }

        // Merge Highlights
        val localHighlights = userDataDao.getAllHighlights().first().associateBy { it.id }
        payload.highlights.forEach { cloudHighlight ->
            val local = localHighlights[cloudHighlight.id]
            if (local == null || cloudHighlight.timestampMs > local.timestampMs) {
                userDataDao.addHighlight(HighlightEntity(cloudHighlight.id, cloudHighlight.verseId, cloudHighlight.colorHex, cloudHighlight.timestampMs))
            }
        }

        // Merge Journal Entries
        val localJournals = journalDao.getAllEntries().first().associateBy { it.id }
        payload.journalEntries.forEach { cloudJournal ->
            val local = localJournals[cloudJournal.id]
            if (local == null || cloudJournal.updatedAt > local.updatedAt) {
                journalDao.insertEntry(
                    JournalEntryEntity(
                        id = cloudJournal.id,
                        verseId = cloudJournal.verseId,
                        chapterId = cloudJournal.chapterId,
                        title = cloudJournal.title,
                        content = cloudJournal.content,
                        createdAt = cloudJournal.createdAt,
                        updatedAt = cloudJournal.updatedAt,
                        moodTags = cloudJournal.moodTags.joinToString(","),
                    ),
                )
            }
        }
    }
}
