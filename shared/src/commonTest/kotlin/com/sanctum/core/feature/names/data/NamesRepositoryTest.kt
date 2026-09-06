package com.sanctum.core.feature.names.data

import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NamesRepositoryTest {

    private val settings = MapSettings()
    private val repository = NamesRepository(settings)

    @Test
    fun testGetNamesReturnsAll99() {
        val names = repository.getNames()
        assertEquals(99, names.size)
    }

    @Test
    fun testGetNamesHaveRequiredFields() {
        val names = repository.getNames()
        val everyFieldPopulated = names.all { name ->
            name.arabic.isNotBlank() &&
                name.transliteration.isNotBlank() &&
                name.meaning.isNotBlank() &&
                name.explanation.isNotBlank() &&
                name.audioFileName.isNotBlank()
        }
        assertTrue(everyFieldPopulated)
    }

    @Test
    fun testSearchByQuery() {
        assertEquals(99, repository.searchNames("").size)
        val results = repository.searchNames("merc")
        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.transliteration.lowercase().contains("merc") })
    }

    @Test
    fun testFavoriteAndMemorizedPersistence() {
        assertFalse(repository.isFavorited(1))
        repository.setFavorited(1, true)
        assertTrue(repository.isFavorited(1))
        repository.setFavorited(1, false)
        assertFalse(repository.isFavorited(1))

        assertFalse(repository.isMemorized(99))
        repository.setMemorized(99, true)
        assertTrue(repository.isMemorized(99))
    }
}
