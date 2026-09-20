package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProphetsGlossaryTest {

    private val glossary: ProphetsGlossary = ProphetsGlossary()

    @Test
    fun testSearchExactMatch() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("Abraham")
        assertEquals(1, results.size)
        assertEquals("Abraham", results[0].term)
    }

    @Test
    fun testSearchCaseInsensitive() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("mOsEs")
        assertEquals(1, results.size)
        assertEquals("Moses", results[0].term)
    }

    @Test
    fun testSearchPartialMatch() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("braham")
        assertEquals(1, results.size)
        assertEquals("Abraham", results[0].term)
    }

    @Test
    fun testSearchInSummary() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("Patriarch")
        assertEquals(1, results.size)
        assertEquals("Abraham", results[0].term)
    }

    @Test
    fun testSearchEmptyQuery() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("")
        assertEquals(glossary.entries.size, results.size)
    }

    @Test
    fun testSearchNoMatch() {
        val results: List<ProphetsGlossary.Entry> = glossary.search("Buddha")
        assertTrue(results.isEmpty())
    }
}
