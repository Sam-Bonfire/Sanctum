package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SilentWindowTest {

    @Test
    fun `test happy path calculation with positive grace periods`() {
        val prayerStart: Long = 1000L
        val prayerEnd: Long = 2000L
        val graceBefore: Long = 100L
        val graceAfter: Long = 200L

        val result: SilentWindow = SilentWindow.calculate(
            prayerStartMillis = prayerStart,
            prayerEndMillis = prayerEnd,
            gracePeriodBeforeMs = graceBefore,
            gracePeriodAfterMs = graceAfter,
        )

        assertEquals(900L, result.startMillis)
        assertEquals(2200L, result.endMillis)
    }

    @Test
    fun `test calculation with zero grace periods`() {
        val prayerStart: Long = 1000L
        val prayerEnd: Long = 2000L

        val result: SilentWindow = SilentWindow.calculate(
            prayerStartMillis = prayerStart,
            prayerEndMillis = prayerEnd,
            gracePeriodBeforeMs = 0L,
            gracePeriodAfterMs = 0L,
        )

        assertEquals(1000L, result.startMillis)
        assertEquals(2000L, result.endMillis)
    }

    @Test
    fun `test calculation with negative grace periods falls back to zero`() {
        val prayerStart: Long = 1000L
        val prayerEnd: Long = 2000L

        val result: SilentWindow = SilentWindow.calculate(
            prayerStartMillis = prayerStart,
            prayerEndMillis = prayerEnd,
            gracePeriodBeforeMs = -100L,
            gracePeriodAfterMs = -200L,
        )

        assertEquals(1000L, result.startMillis)
        assertEquals(2000L, result.endMillis)
    }

    @Test
    fun `test calculation handles invalid prayer ends by falling back to start time`() {
        val prayerStart: Long = 2000L
        val prayerEnd: Long = 1000L

        val result: SilentWindow = SilentWindow.calculate(
            prayerStartMillis = prayerStart,
            prayerEndMillis = prayerEnd,
            gracePeriodBeforeMs = 0L,
            gracePeriodAfterMs = 0L,
        )

        assertEquals(2000L, result.startMillis)
        // max(prayerStart, prayerEnd) + safeAfter
        assertEquals(2000L, result.endMillis)
    }

    @Test
    fun `test requirement failure on invalid manual creation`() {
        assertFailsWith<IllegalArgumentException> {
            SilentWindow(startMillis = 2000L, endMillis = 1000L)
        }
    }
}
