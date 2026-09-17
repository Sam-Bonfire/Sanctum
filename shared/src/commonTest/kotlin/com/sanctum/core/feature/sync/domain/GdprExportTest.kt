package com.sanctum.core.feature.sync.domain

import com.sanctum.core.feature.journal.domain.JournalEntry
import com.sanctum.core.feature.scripture.domain.Bookmark
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GdprExportTest {

    private fun payload() = BackupPayload(
        lastSyncTimestampMs = 0,
        bookmarks = listOf(Bookmark(1, 2, 3L)),
        journalEntries = listOf(JournalEntry(1, null, null, "t", "c", 0, 0, emptyList())),
    )

    @Test
    fun `summarizes categories`() {
        val export = GdprExport(0, payload())
        assertEquals(1, export.categories()["bookmarks"])
        assertEquals(0, export.categories()["notes"])
        assertEquals(2, export.totalRecords())
        assertFalse(export.isEmpty())
    }

    @Test
    fun `detects empty exports`() {
        assertTrue(GdprExport(0, BackupPayload(lastSyncTimestampMs = 0)).isEmpty())
    }
}
