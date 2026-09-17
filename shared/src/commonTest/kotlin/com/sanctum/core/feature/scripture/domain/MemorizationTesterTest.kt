package com.sanctum.core.feature.scripture.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MemorizationTesterTest {

    @Test
    fun `hides one more word each round`() {
        val first = MemorizationRound("a b c").nextRound()
        assertEquals(listOf("…", "b", "c"), first.displayWords())
        assertEquals(listOf("…", "…", "c"), first.nextRound().displayWords())
    }

    @Test
    fun `stops when fully hidden`() {
        val full = MemorizationRound("a", setOf(0))
        assertEquals(full, full.nextRound())
    }

    @Test
    fun `scores word by position`() {
        assertEquals(2, MemorizationRound("a b c").score(listOf("a", "x", "c")))
        assertTrue(MemorizationRound("").words().isEmpty())
    }
}
