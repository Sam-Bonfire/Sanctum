package com.sanctum.core.feature.journal.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JournalTagTest {

    private val entries = listOf(
        JournalEntry(1, null, null, "A", "x", 0, 0, listOf("grateful")),
        JournalEntry(2, null, null, "B", "y", 0, 0, listOf("Tired")),
    )

    @Test
    fun `moves entries in folders`() {
        val folder = JournalFolder("f1", "Favorites")
        assertEquals(listOf(1), folder.addEntry(1).entryIds)
        assertEquals(listOf(1), folder.addEntry(1).addEntry(1).entryIds)
        assertTrue(folder.addEntry(1).removeEntry(1).entryIds.isEmpty())
    }

    @Test
    fun `filters entries by tag`() {
        assertEquals(listOf(1), entriesWithTag(entries, "GRATEFUL").map { it.id })
        assertTrue(entriesWithTag(entries, "missing").isEmpty())
    }
}
