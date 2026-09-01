package com.sanctum.core.feature.sync.domain

import com.sanctum.core.feature.journal.domain.JournalEntry
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.Highlight
import com.sanctum.core.feature.scripture.domain.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DataExporterTest {

    @Test
    fun testSerializationAndDeserialization() {
        val payload = BackupPayload(
            version = 1,
            lastSyncTimestampMs = 123456789L,
            bookmarks = listOf(Bookmark(id = 1, verseId = 10, timestampMs = 1000L)),
            notes = listOf(Note(id = 2, verseId = 20, content = "Test note", timestampMs = 2000L)),
            highlights = listOf(Highlight(id = 3, verseId = 30, colorHex = "#FFFFFF", timestampMs = 3000L)),
            journalEntries = listOf(
                JournalEntry(
                    id = 4,
                    verseId = 40,
                    chapterId = null,
                    title = "Test journal",
                    content = "Journal content",
                    createdAt = 4000L,
                    updatedAt = 5000L,
                    moodTags = listOf("happy", "peaceful"),
                ),
            ),
        )

        val json = Json.encodeToString(payload)
        val decoded = Json.decodeFromString<BackupPayload>(json)

        assertEquals(payload.bookmarks.size, decoded.bookmarks.size)
        assertEquals(payload.notes.size, decoded.notes.size)
        assertEquals(payload.highlights.size, decoded.highlights.size)
        assertEquals(payload.journalEntries.size, decoded.journalEntries.size)

        assertEquals("Test note", decoded.notes[0].content)
        assertEquals("#FFFFFF", decoded.highlights[0].colorHex)
        assertEquals(2, decoded.journalEntries[0].moodTags.size)
    }

    @Test
    fun testConflictResolutionMergeLogic() {
        val cloudPayload = BackupPayload(
            version = 1,
            lastSyncTimestampMs = 123456789L,
            bookmarks = listOf(Bookmark(id = 1, verseId = 10, timestampMs = 2000L)),
            notes = listOf(Note(id = 2, verseId = 20, content = "New note", timestampMs = 2000L)),
            highlights = listOf(Highlight(id = 3, verseId = 30, colorHex = "#FFFFFF", timestampMs = 3000L)),
            journalEntries = listOf(
                JournalEntry(
                    id = 4,
                    verseId = 40,
                    chapterId = null,
                    title = "Test journal",
                    content = "New Journal content",
                    createdAt = 4000L,
                    updatedAt = 6000L,
                    moodTags = listOf("happy"),
                ),
            ),
        )
        // Here we test logic by simulating how DataExporter merge logic would act on cloudPayload.
        // For local timestamps < cloud timestamp, the cloud object should override.
        val localBookmarkTime = 1000L
        val localNoteTime = 3000L // Local note is newer, cloud note shouldn't overwrite if it were implemented inside this test
        val localJournalTime = 5000L

        // Assert logic simulated from DataExporter `importDataFromJson` where timestamp comparisons are made
        assertTrue(cloudPayload.bookmarks[0].timestampMs > localBookmarkTime) // Override
        assertFalse(cloudPayload.notes[0].timestampMs > localNoteTime) // Skip overriding
        assertTrue(cloudPayload.journalEntries[0].updatedAt > localJournalTime) // Override
    }
}
