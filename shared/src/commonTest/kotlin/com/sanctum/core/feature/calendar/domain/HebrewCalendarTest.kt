package com.sanctum.core.feature.calendar.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HebrewCalendarTest {

    @Test
    fun gregorianAnchor2000() {
        assertEquals(2451545L, HebrewCalendar.gregorianToAbsolute(2000, 1, 1))
        assertEquals(GregorianDate(2000, 1, 1), HebrewCalendar.absoluteToGregorian(2451545L))
    }

    @Test
    fun gregorianRoundTrip() {
        val dates = listOf(
            Triple(2026, 9, 8),
            Triple(1948, 5, 14),
            Triple(2024, 2, 29),
            Triple(1999, 12, 31),
            Triple(1, 1, 1),
        )
        for ((y, m, d) in dates) {
            val abs = HebrewCalendar.gregorianToAbsolute(y, m, d)
            assertEquals(GregorianDate(y, m, d), HebrewCalendar.absoluteToGregorian(abs))
        }
    }

    @Test
    fun roshHashanahAnchors() {
        // Verified against hebcal/chabad: 1 Tishri dates.
        assertEquals(HebrewDate(5785, HebrewCalendar.TISHRI, 1), HebrewCalendar.fromGregorian(2024, 10, 3))
        assertEquals(HebrewDate(5786, HebrewCalendar.TISHRI, 1), HebrewCalendar.fromGregorian(2025, 9, 23))
        assertEquals(HebrewDate(5787, HebrewCalendar.TISHRI, 1), HebrewCalendar.fromGregorian(2026, 9, 12))
        assertEquals(HebrewDate(5787, HebrewCalendar.TISHRI, 2), HebrewCalendar.fromGregorian(2026, 9, 13))
        assertEquals(HebrewDate(5786, HebrewCalendar.ELUL, 29), HebrewCalendar.fromGregorian(2026, 9, 11))
    }

    @Test
    fun independenceDayAnchor() {
        // 5 Iyar 5708 = 14 May 1948.
        assertEquals(HebrewDate(5708, HebrewCalendar.IYYAR, 5), HebrewCalendar.fromGregorian(1948, 5, 14))
        assertEquals(
            GregorianDate(1948, 5, 14),
            HebrewCalendar.gregorianFromHebrew(HebrewDate(5708, HebrewCalendar.IYYAR, 5)),
        )
    }

    @Test
    fun hebrewRoundTripFullSweep() {
        // Every day of one leap year (5784) and one common year (5786).
        for (year in listOf(5784, 5786)) {
            for (month in HebrewCalendar.NISAN..HebrewCalendar.monthsInYear(year)) {
                for (day in 1..HebrewCalendar.daysInMonth(year, month)) {
                    val original = HebrewDate(year, month, day)
                    assertEquals(original, HebrewCalendar.fromAbsolute(original.toAbsolute()))
                    val gregorian = HebrewCalendar.gregorianFromHebrew(original)
                    assertEquals(original, HebrewCalendar.fromGregorian(gregorian.year, gregorian.month, gregorian.day))
                }
            }
        }
    }

    @Test
    fun leapCycle() {
        // 19-year Metonic leap years: 3, 6, 8, 11, 14, 17, 19 (mod 19).
        val leapRemainders = setOf(3, 6, 8, 11, 14, 17, 0)
        for (year in 5700..5760) {
            assertEquals(year % 19 in leapRemainders, HebrewCalendar.isLeapYear(year))
            assertEquals(if (HebrewCalendar.isLeapYear(year)) 13 else 12, HebrewCalendar.monthsInYear(year))
        }
    }

    @Test
    fun yearLengthsAreValid() {
        for (year in 5750..5800) {
            val length = HebrewCalendar.daysInYear(year)
            assertTrue(length in 353..355 || length in 383..385, "year $year has $length days")
        }
    }

    @Test
    fun adarNaming() {
        assertEquals("Adar", HebrewCalendar.monthName(HebrewCalendar.ADAR, 5786))
        assertEquals("Adar I", HebrewCalendar.monthName(HebrewCalendar.ADAR, 5784))
        assertEquals("Adar II", HebrewCalendar.monthName(HebrewCalendar.ADAR_II, 5784))
        assertFailsWith<IllegalArgumentException> { HebrewCalendar.monthName(HebrewCalendar.ADAR_II, 5786) }
    }

    @Test
    fun rejectsImpossibleDates() {
        assertFailsWith<IllegalArgumentException> { HebrewCalendar.gregorianToAbsolute(2026, 2, 30) }
        assertFailsWith<IllegalArgumentException> { HebrewCalendar.gregorianToAbsolute(2025, 2, 29) }
        assertFailsWith<IllegalArgumentException> { HebrewCalendar.fromAbsolute(0) }
    }

    @Test
    fun rejectsInvalidDates() {
        // Non-leap year has no Adar II; no month ever has 31 days.
        assertFailsWith<IllegalArgumentException> { HebrewDate(5786, HebrewCalendar.ADAR_II, 1) }
        assertFailsWith<IllegalArgumentException> { HebrewCalendar.toAbsolute(5786, HebrewCalendar.TISHRI, 31) }
    }

    @Test
    fun displayName() {
        assertEquals("26 Elul 5786", HebrewCalendar.fromGregorian(2026, 9, 8).displayName())
    }
}
