package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MudraGuideTest {

    private val guides = listOf(
        MudraGuide("anjali", "Anjali", "Greeting", listOf("Join palms", "Bow slightly")),
        MudraGuide("empty", "Empty", "None"),
    )

    @Test
    fun `validates guide completeness`() {
        assertTrue(guides[0].isComplete())
        assertFalse(guides[1].isComplete())
    }

    @Test
    fun `finds by id`() {
        assertEquals("Anjali", findMudra(guides, "anjali")?.name)
        assertNull(findMudra(guides, "missing"))
    }
}
