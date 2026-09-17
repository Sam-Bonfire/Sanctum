package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ExamenPromptTest {

    @Test
    fun `rotates through five steps`() {
        assertEquals(1, examenForDay(0).step)
        assertEquals(5, examenForDay(4).step)
        assertEquals(1, examenForDay(5).step)
    }

    @Test
    fun `covers full examen`() {
        assertEquals(5, DAILY_EXAMEN.size)
        assertEquals(listOf(1, 2, 3, 4, 5), DAILY_EXAMEN.map { it.step })
    }
}
