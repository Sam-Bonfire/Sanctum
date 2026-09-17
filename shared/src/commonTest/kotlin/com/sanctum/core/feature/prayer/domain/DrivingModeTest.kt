package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DrivingModeTest {

    @Test
    fun `toggles mode`() {
        assertTrue(DrivingMode().toggle().enabled)
        assertFalse(DrivingMode(true).toggle().enabled)
    }

    @Test
    fun `scales controls when driving`() {
        assertEquals(1.5f, DrivingMode(true).controlScale())
        assertEquals(1f, DrivingMode(false).controlScale())
        assertEquals(1f, DrivingMode(true, largeControls = false).controlScale())
    }
}
