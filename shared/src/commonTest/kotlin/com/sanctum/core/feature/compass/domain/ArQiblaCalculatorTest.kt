package com.sanctum.core.feature.compass.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArQiblaCalculatorTest {

    @Test
    fun `test exact alignment`() {
        val qiblaBearing = 90.0
        val deviceHeading = 90.0f

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(0.0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertTrue(state.isAligned)
        assertEquals(0.0f, state.horizontalOffsetRatio)
    }

    @Test
    fun `test slight misalignment but still aligned within tolerance`() {
        val qiblaBearing = 90.0
        val deviceHeading = 89.0f // 1 degree diff, tolerance is 2

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(1.0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertTrue(state.isAligned)
        assertEquals(1.0f / (ArQiblaCalculator.CAMERA_FOV_DEGREES / 2f), state.horizontalOffsetRatio)
    }

    @Test
    fun `test visible but not aligned`() {
        val qiblaBearing = 90.0
        val deviceHeading = 70.0f // 20 degrees difference

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(20.0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertFalse(state.isAligned)
        assertEquals(20.0f / (ArQiblaCalculator.CAMERA_FOV_DEGREES / 2f), state.horizontalOffsetRatio)
    }

    @Test
    fun `test outside FOV`() {
        val qiblaBearing = 90.0
        val deviceHeading = 40.0f // 50 degrees diff, FOV is 60 (so +/- 30)

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(50.0f, state.relativeBearing)
        assertFalse(state.isVisible)
        assertFalse(state.isAligned)
        // Ratio should be clamped to 1.0 because 50/30 > 1
        assertEquals(1.0f, state.horizontalOffsetRatio)
    }

    @Test
    fun `test wraparound crossing north (qibla 350, device 10)`() {
        val qiblaBearing = 350.0
        val deviceHeading = 10.0f // diff is -20 degrees (turn left 20)

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(-20.0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertFalse(state.isAligned)
        assertEquals(-20.0f / 30.0f, state.horizontalOffsetRatio)
    }

    @Test
    fun `test wraparound crossing north (qibla 10, device 350)`() {
        val qiblaBearing = 10.0
        val deviceHeading = 350.0f // diff is +20 degrees (turn right 20)

        val state = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

        assertEquals(20.0f, state.relativeBearing)
        assertTrue(state.isVisible)
        assertFalse(state.isAligned)
        assertEquals(20.0f / 30.0f, state.horizontalOffsetRatio)
    }
}
