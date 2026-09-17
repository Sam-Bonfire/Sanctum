package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InlineCommentaryTest {

    private val commentaries = listOf(
        InlineCommentary(1, "light", "Guidance in darkness.", "Tafsir"),
        InlineCommentary(2, "mercy", "Compassion.", "Notes"),
    )

    @Test
    fun `validates popups`() {
        assertTrue(commentaries[0].isValid())
        assertFalse(InlineCommentary(0, "x", "y").isValid())
        assertFalse(InlineCommentary(1, "", "y").isValid())
    }

    @Test
    fun `filters by verse`() {
        assertEquals(listOf(1), commentariesFor(commentaries, 1).map { it.verseId })
        assertEquals(0, commentariesFor(commentaries, 9).size)
    }
}
