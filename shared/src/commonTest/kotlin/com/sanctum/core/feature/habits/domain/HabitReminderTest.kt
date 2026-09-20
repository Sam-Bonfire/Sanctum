package com.sanctum.core.feature.habits.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class HabitReminderTest {

    @Test
    fun testValidationSuccess() {
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 30, daysOfWeek = setOf(1, 3, 5), isEnabled = true)
        assertEquals(8, reminder.hour)
    }

    @Test
    fun testValidationFailureInvalidHour() {
        assertFailsWith<IllegalArgumentException> {
            HabitReminder(hour = 24, minute = 0, daysOfWeek = setOf(1), isEnabled = true)
        }
    }

    @Test
    fun testValidationFailureInvalidMinute() {
        assertFailsWith<IllegalArgumentException> {
            HabitReminder(hour = 8, minute = 60, daysOfWeek = setOf(1), isEnabled = true)
        }
    }

    @Test
    fun testValidationFailureInvalidDays() {
        assertFailsWith<IllegalArgumentException> {
            HabitReminder(hour = 8, minute = 0, daysOfWeek = setOf(0, 1), isEnabled = true)
        }
        assertFailsWith<IllegalArgumentException> {
            HabitReminder(hour = 8, minute = 0, daysOfWeek = setOf(8), isEnabled = true)
        }
    }

    @Test
    fun testComputeNextFireTimeMillis_Disabled() {
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 0, daysOfWeek = setOf(1), isEnabled = false)
        assertNull(reminder.computeNextFireTimeMillis(1000L))
    }

    @Test
    fun testComputeNextFireTimeMillis_EmptyDays() {
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 0, daysOfWeek = emptySet(), isEnabled = true)
        assertNull(reminder.computeNextFireTimeMillis(1000L))
    }

    @Test
    fun testComputeNextFireTimeMillis_SameDayLater() {
        val timeZone: TimeZone = TimeZone.UTC
        val currentDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 2, hour = 7, minute = 0, second = 0, nanosecond = 0)
        val currentInstant: Instant = currentDateTime.toInstant(timeZone)
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 30, daysOfWeek = setOf(1), isEnabled = true) // 2023-01-02 is Monday (Day 1)
        val nextMillis: Long? = reminder.computeNextFireTimeMillis(currentInstant.toEpochMilliseconds(), timeZone)

        val expectedDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 2, hour = 8, minute = 30, second = 0, nanosecond = 0)
        assertEquals(expectedDateTime.toInstant(timeZone).toEpochMilliseconds(), nextMillis)
    }

    @Test
    fun testComputeNextFireTimeMillis_SameDayEarlier() {
        val timeZone: TimeZone = TimeZone.UTC
        val currentDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 2, hour = 9, minute = 0, second = 0, nanosecond = 0)
        val currentInstant: Instant = currentDateTime.toInstant(timeZone)
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 30, daysOfWeek = setOf(1), isEnabled = true) // 2023-01-02 is Monday (Day 1)
        val nextMillis: Long? = reminder.computeNextFireTimeMillis(currentInstant.toEpochMilliseconds(), timeZone)

        // Next Monday
        val expectedDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 9, hour = 8, minute = 30, second = 0, nanosecond = 0)
        assertEquals(expectedDateTime.toInstant(timeZone).toEpochMilliseconds(), nextMillis)
    }

    @Test
    fun testComputeNextFireTimeMillis_NextDay() {
        val timeZone: TimeZone = TimeZone.UTC
        val currentDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 2, hour = 9, minute = 0, second = 0, nanosecond = 0)
        val currentInstant: Instant = currentDateTime.toInstant(timeZone)
        val reminder: HabitReminder = HabitReminder(hour = 8, minute = 30, daysOfWeek = setOf(2), isEnabled = true) // Day 2 is Tuesday
        val nextMillis: Long? = reminder.computeNextFireTimeMillis(currentInstant.toEpochMilliseconds(), timeZone)

        val expectedDateTime: LocalDateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 3, hour = 8, minute = 30, second = 0, nanosecond = 0)
        assertEquals(expectedDateTime.toInstant(timeZone).toEpochMilliseconds(), nextMillis)
    }
}
