package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PratikramanGuideTest {
    private fun create(steps: List<String> = listOf("1", "2", "3", "4")) =
        PratikramanGuide("1", "Name", "Meaning", steps)

    @Test fun testState() {
        val g = create()
        assertEquals(0, g.currentStepIndex)
        assertEquals(0, g.progressPercentage())
        assertFalse(g.isComplete())
    }

    @Test fun testAdvance() {
        var g = create()
        g = g.advance()
        assertEquals(1, g.currentStepIndex)
        assertEquals(33, g.progressPercentage())
        g = g.advance()
        assertEquals(2, g.currentStepIndex)
        assertEquals(67, g.progressPercentage())
        g = g.advance()
        assertEquals(3, g.currentStepIndex)
        assertEquals(100, g.progressPercentage())
        assertTrue(g.isComplete())
        g = g.advance()
        assertEquals(3, g.currentStepIndex)
        assertEquals(100, g.progressPercentage())
        assertTrue(g.isComplete())
    }

    @Test fun testEmpty() {
        val g = create(emptyList())
        assertEquals(0, g.progressPercentage())
        assertFalse(g.isComplete())
        assertFalse(g.advance().isComplete())
    }

    @Test fun testSingle() {
        var g = create(listOf("1"))
        assertEquals(100, g.progressPercentage())
        assertTrue(g.isComplete())
        g = g.advance()
        assertEquals(100, g.progressPercentage())
        assertTrue(g.isComplete())
    }
}
