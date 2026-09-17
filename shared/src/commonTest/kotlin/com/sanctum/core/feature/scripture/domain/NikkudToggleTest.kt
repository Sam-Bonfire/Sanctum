package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class NikkudToggleTest {

    private val pointed = "בְּרֵאשִׁית"

    @Test
    fun `keeps vowels when enabled`() {
        assertEquals(pointed, applyNikkudSetting(pointed, NikkudSetting(true)))
    }

    @Test
    fun `strips vowels when disabled`() {
        assertEquals("בראשית", applyNikkudSetting(pointed, NikkudSetting(false)))
    }

    @Test
    fun `leaves plain text untouched`() {
        assertEquals("abc", stripNikkud("abc"))
    }
}
