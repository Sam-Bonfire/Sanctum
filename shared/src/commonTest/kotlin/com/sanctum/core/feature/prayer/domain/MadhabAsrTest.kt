package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MadhabAsrTest {

    @Test
    fun `hanafi needs longer shadow`() {
        assertEquals(1.0, MadhabAsrMethod.SHAFII.shadowFactor())
        assertEquals(2.0, MadhabAsrMethod.HANAFI.shadowFactor())
        assertTrue(asrAltitudeDegrees(0.0, MadhabAsrMethod.SHAFII) > asrAltitudeDegrees(0.0, MadhabAsrMethod.HANAFI))
    }

    @Test
    fun `computes asr altitude`() {
        assertEquals(45.0, asrAltitudeDegrees(0.0, MadhabAsrMethod.SHAFII), 0.001)
        assertFailsWith<IllegalArgumentException> { asrAltitudeDegrees(-1.0, MadhabAsrMethod.SHAFII) }
    }
}
