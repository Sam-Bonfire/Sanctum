package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DuaLoopTest {

    @Test
    fun `validates loop bounds`() {
        assertTrue(DuaLoop("d1", 1_000, 5_000, 3).isValid(10_000))
        assertFalse(DuaLoop("d1", 5_000, 1_000, 3).isValid(10_000))
        assertFalse(DuaLoop("d1", 0, 11_000, 3).isValid(10_000))
        assertFalse(DuaLoop("", 0, 1_000, 1).isValid(10_000))
    }

    @Test
    fun `detects infinite loops and length`() {
        assertTrue(DuaLoop("d1", 0, 5_000, 0).isInfinite())
        assertFalse(DuaLoop("d1", 0, 5_000, 2).isInfinite())
        assertEquals(4_000, DuaLoop("d1", 1_000, 5_000).loopLengthMs())
    }
}
