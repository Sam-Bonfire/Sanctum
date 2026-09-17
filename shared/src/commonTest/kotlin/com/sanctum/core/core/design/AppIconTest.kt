package com.sanctum.core.core.design

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppIconTest {

    @Test
    fun `free users see only classic`() {
        assertEquals(listOf("classic"), availableIcons(false).map { it.id })
    }

    @Test
    fun `premium unlocks all and finds by id`() {
        assertEquals(3, availableIcons(true).size)
        assertEquals("Midnight Gold", findIcon("midnight", true)?.name)
        assertNull(findIcon("midnight", false))
        assertNull(findIcon("missing", true))
    }
}
