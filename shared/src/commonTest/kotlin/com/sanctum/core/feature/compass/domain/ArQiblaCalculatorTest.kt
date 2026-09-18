package com.sanctum.core.feature.compass.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArQiblaCalculatorTest {

    @Test
    fun `centers aligned qibla`() {
        val state = ArQiblaCalculator.calculateViewportState(90.0, 90f)
        assertEquals(0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertTrue(state.isAligned)
        assertEquals(0f, state.horizontalOffsetRatio)
    }

    @Test
    fun `marks off-screen bearing`() {
        val state = ArQiblaCalculator.calculateViewportState(90.0, 0f)
        assertEquals(90f, state.relativeBearing)
        assertFalse(state.isVisible)
        assertFalse(state.isAligned)
        assertEquals(1f, state.horizontalOffsetRatio)
    }

    @Test
    fun `normalizes wraparound`() {
        val state = ArQiblaCalculator.calculateViewportState(10.0, 350f)
        assertEquals(20f, state.relativeBearing)
        assertTrue(state.isVisible)
    }
}
