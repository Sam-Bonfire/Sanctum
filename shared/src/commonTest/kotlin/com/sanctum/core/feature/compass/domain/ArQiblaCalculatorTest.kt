package com.sanctum.core.feature.compass.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArQiblaCalculatorTest {

    @Test
    fun testExactAlignment() {
        // Pointing exactly at Qibla
        val result = ArQiblaCalculator.calculate(deviceHeading = 90f, qiblaBearing = 90f)
        assertTrue(result.isVisible)
        assertTrue(result.isAligned)
        assertEquals(0f, result.horizontalOffset)
        assertEquals("Aligned", result.instructionText)
    }

    @Test
    fun testVisibleButNotAlignedRight() {
        // Qibla is 90, device pointing 80 (Qibla is 10 degrees to the right)
        val result = ArQiblaCalculator.calculate(deviceHeading = 80f, qiblaBearing = 90f)
        assertTrue(result.isVisible)
        assertFalse(result.isAligned)
        assertTrue(result.horizontalOffset > 0f)
        assertEquals("Turn 10° Right", result.instructionText)
    }

    @Test
    fun testVisibleButNotAlignedLeft() {
        // Qibla is 90, device pointing 100 (Qibla is 10 degrees to the left)
        val result = ArQiblaCalculator.calculate(deviceHeading = 100f, qiblaBearing = 90f)
        assertTrue(result.isVisible)
        assertFalse(result.isAligned)
        assertTrue(result.horizontalOffset < 0f)
        assertEquals("Turn 10° Left", result.instructionText)
    }

    @Test
    fun testOffScreenRight() {
        // Qibla is 90, device pointing 10 (Qibla is 80 degrees to the right)
        // FOV is 60 (±30). So >30 is offscreen.
        val result = ArQiblaCalculator.calculate(deviceHeading = 10f, qiblaBearing = 90f)
        assertFalse(result.isVisible)
        assertFalse(result.isAligned)
        assertEquals(0f, result.horizontalOffset) // When not visible, offset is 0 by our logic
        assertEquals("Turn 80° Right", result.instructionText)
    }

    @Test
    fun testCrossing360BoundaryRight() {
        // Qibla is 10, device pointing 350. Diff is 20 degrees right.
        val result = ArQiblaCalculator.calculate(deviceHeading = 350f, qiblaBearing = 10f)
        assertTrue(result.isVisible)
        assertFalse(result.isAligned)
        assertTrue(result.horizontalOffset > 0f)
        assertEquals("Turn 20° Right", result.instructionText)
    }

    @Test
    fun testCrossing360BoundaryLeft() {
        // Qibla is 350, device pointing 10. Diff is 20 degrees left.
        val result = ArQiblaCalculator.calculate(deviceHeading = 10f, qiblaBearing = 350f)
        assertTrue(result.isVisible)
        assertFalse(result.isAligned)
        assertTrue(result.horizontalOffset < 0f)
        assertEquals("Turn 20° Left", result.instructionText)
    }
}
