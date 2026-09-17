package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MeditationSessionTest {

    @Test
    fun `counts bowl chimes`() {
        val session = MeditationSession(600_000, 60_000).advance(150_000)
        assertEquals(2, session.chimesDue())
        assertFalse(session.isComplete())
    }

    @Test
    fun `completes and clamps`() {
        val session = MeditationSession(1_000).advance(5_000)
        assertTrue(session.isComplete())
        assertEquals(1_000, session.elapsedMs)
    }

    @Test
    fun `no chimes without interval`() {
        assertEquals(0, MeditationSession(1_000).advance(900).chimesDue())
    }
}
