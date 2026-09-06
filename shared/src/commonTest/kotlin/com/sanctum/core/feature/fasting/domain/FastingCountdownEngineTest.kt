package com.sanctum.core.feature.fasting.domain

import com.sanctum.core.feature.scripture.presentation.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FastingCountdownEngineTest {

    private val engine = FastingCountdownEngine()

    // Use an instant where the local time in default timezone is roughly 00:00 (Midnight)
    // We'll calculate a base instant and mock times relative to that day.
    private val baseTimeMillis = Instant.parse("2024-03-10T00:00:00Z").toEpochMilliseconds()

    // We mock prayer times for Fajr at 5:30 AM and Maghrib at 6:15 PM (18:15)
    private val mockPrayers = listOf(
        PrayerTime("Fajr", "05:30", "AM", false),
        PrayerTime("Dhuhr", "12:15", "PM", true),
        PrayerTime("Asr", "15:45", "PM", false),
        PrayerTime("Maghrib", "06:15", "PM", false),
        PrayerTime("Isha", "08:00", "PM", false),
    )

    private fun getMillisForTime(hour: Int, minute: Int): Long {
        // Simple approximation for test purposes: just add the hours/minutes to the base midnight
        // assuming base time is aligned with system timezone midnight.
        // Actually to be timezone safe, we should construct an Instant based on a known LocalDateTime
        // but given the engine logic parses local hours/minutes from the instant, we just need the
        // localDateTime corresponding to the instant to have the desired hours and minutes.
        val baseInstant = Instant.fromEpochMilliseconds(baseTimeMillis)
        val ldt = baseInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Let's just adjust the instant directly. Note: this might cross days in some edge cases,
        // but it's sufficient if we just need the hours/minutes to match.
        // For simplicity, we just use arbitrary exact minutes matching logic from engine
        val targetMinutes = hour * 60 + minute
        val currentMinutes = ldt.hour * 60 + ldt.minute
        val diffMinutes = targetMinutes - currentMinutes

        return baseTimeMillis + (diffMinutes * 60 * 1000L)
    }

    @Test
    fun testEatingWindowBeforeFajr() {
        // 4:00 AM
        val currentTime = getMillisForTime(4, 0)
        val state = engine.calculateFastingState(currentTime, mockPrayers)

        assertNotNull(state)
        assertEquals(FastingPhase.EATING_WINDOW, state.phase)
        assertEquals("Suhoor Ends", state.targetEventName)
        // From 4:00 AM to 5:30 AM is 1 hour and 30 minutes
        assertEquals(1, state.remainingHours)
        assertEquals(30, state.remainingMinutes)
    }

    @Test
    fun testActiveFastAfterFajr() {
        // 10:45 AM
        val currentTime = getMillisForTime(10, 45)
        val state = engine.calculateFastingState(currentTime, mockPrayers)

        assertNotNull(state)
        assertEquals(FastingPhase.ACTIVE_FAST, state.phase)
        assertEquals("Iftar", state.targetEventName)
        // From 10:45 AM to 18:15 (6:15 PM) is 7 hours and 30 minutes
        assertEquals(7, state.remainingHours)
        assertEquals(30, state.remainingMinutes)
    }

    @Test
    fun testEatingWindowAfterMaghrib() {
        // 8:00 PM (20:00)
        val currentTime = getMillisForTime(20, 0)
        val state = engine.calculateFastingState(currentTime, mockPrayers)

        assertNotNull(state)
        assertEquals(FastingPhase.EATING_WINDOW, state.phase)
        assertEquals("Suhoor Ends", state.targetEventName)
        // From 20:00 today to 05:30 tomorrow is (24:00 - 20:00) + 05:30 = 4h + 5.5h = 9 hours and 30 minutes
        assertEquals(9, state.remainingHours)
        assertEquals(30, state.remainingMinutes)
    }

    @Test
    fun testExactFajrBoundary() {
        // Exactly at Fajr (5:30 AM)
        val currentTime = getMillisForTime(5, 30)
        val state = engine.calculateFastingState(currentTime, mockPrayers)

        assertNotNull(state)
        // Since current time is not < Fajr (it is ==), it should transition to ACTIVE_FAST
        assertEquals(FastingPhase.ACTIVE_FAST, state.phase)
        assertEquals("Iftar", state.targetEventName)
        // From 5:30 AM to 6:15 PM is 12 hours and 45 minutes
        assertEquals(12, state.remainingHours)
        assertEquals(45, state.remainingMinutes)
    }

    @Test
    fun testExactMaghribBoundary() {
        // Exactly at Maghrib (6:15 PM)
        val currentTime = getMillisForTime(18, 15)
        val state = engine.calculateFastingState(currentTime, mockPrayers)

        assertNotNull(state)
        // Since current time is not < Maghrib (it is ==), it should transition to EATING_WINDOW
        assertEquals(FastingPhase.EATING_WINDOW, state.phase)
        assertEquals("Suhoor Ends", state.targetEventName)
        // From 18:15 today to 05:30 tomorrow is (24:00 - 18:15) + 05:30 = 5h 45m + 5h 30m = 11 hours and 15 minutes
        assertEquals(11, state.remainingHours)
        assertEquals(15, state.remainingMinutes)
    }
}
