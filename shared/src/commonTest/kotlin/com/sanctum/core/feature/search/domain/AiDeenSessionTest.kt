package com.sanctum.core.feature.search.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AiDeenSessionTest {

    @Test
    fun `asks and answers in turn`() {
        val session = AiDeenSession().ask("When is Fajr?").answer("Before sunrise.")
        assertEquals(1, session.turns.size)
        assertEquals("Before sunrise.", session.turns[0].answer)
    }

    @Test
    fun `rejects blank questions and answers without questions`() {
        assertFailsWith<IllegalArgumentException> { AiDeenSession().ask("  ") }
        assertFailsWith<IllegalArgumentException> { AiDeenSession().answer("hi") }
    }

    @Test
    fun `caps session length`() {
        val full = AiDeenSession(maxTurns = 1).ask("q")
        assertFailsWith<IllegalArgumentException> { full.ask("another") }
    }
}
