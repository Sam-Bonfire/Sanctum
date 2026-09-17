package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PrayerTapTest {

    @Test
    fun `counts taps and detects bursts`() {
        val first = PrayerTap("d1").tap(1_000)
        assertEquals(1, first.count)
        assertFalse(first.burstActive)
        val second = first.tap(1_500)
        assertEquals(2, second.count)
        assertTrue(second.burstActive)
    }

    @Test
    fun `settles burst animation`() {
        assertFalse(PrayerTap("d1", burstActive = true).settle().burstActive)
    }
}
