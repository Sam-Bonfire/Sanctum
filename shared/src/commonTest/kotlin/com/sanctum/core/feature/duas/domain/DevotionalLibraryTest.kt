package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class DevotionalLibraryTest {

    private val library = DevotionalLibrary(
        listOf(
            DevotionalTrack("a1", "Om Jai Jagdish", "Vishnu", lyrics = "om jai jagdish hare"),
            DevotionalTrack("a2", "Ganesh Aarti", "Ganesha"),
        ),
    )

    @Test
    fun `filters by deity`() {
        assertEquals(listOf("a1"), library.forDeity("vishnu").map { it.id })
    }

    @Test
    fun `searches titles and lyrics`() {
        assertEquals(listOf("a1"), library.search("jagdish").map { it.id })
        assertEquals(listOf("a2"), library.search("Ganesh").map { it.id })
        assertEquals(0, library.search("  ").size)
    }
}
