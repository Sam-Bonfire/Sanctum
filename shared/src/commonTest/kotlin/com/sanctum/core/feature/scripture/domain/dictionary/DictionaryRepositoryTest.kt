package com.sanctum.core.feature.scripture.domain.dictionary

import com.sanctum.core.feature.scripture.data.dictionary.MockDictionaryRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DictionaryRepositoryTest {

    private val repository = MockDictionaryRepository()

    @Test
    fun testLookupExistingTerm() {
        val term = repository.lookupTerm("selah")
        assertNotNull(term)
        assertEquals("Selah", term.word)
    }

    @Test
    fun testLookupExistingTermWithPunctuation() {
        val term = repository.lookupTerm("Amen,")
        assertNotNull(term)
        assertEquals("Amen", term.word)
    }

    @Test
    fun testLookupExistingTermWithUppercase() {
        val term = repository.lookupTerm("HALLELUJAH")
        assertNotNull(term)
        assertEquals("Hallelujah", term.word)
    }

    @Test
    fun testLookupNonExistingTerm() {
        val term = repository.lookupTerm("nonexistent")
        assertNull(term)
    }
}
