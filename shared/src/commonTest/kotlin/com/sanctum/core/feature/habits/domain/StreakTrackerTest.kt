package com.sanctum.core.feature.habits.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StreakTrackerTest {

    @Test
    fun currentAndBestOnUnbrokenRun() {
        assertEquals(StreakInfo(5, 5), StreakTracker.streaks(setOf(0, 1, 2, 3, 4), 4))
    }

    @Test
    fun gapDaySplitsRuns() {
        assertEquals(StreakInfo(2, 3), StreakTracker.streaks(setOf(0, 1, 2, 4, 5), 5))
    }

    @Test
    fun absentTodayKeepsYesterdayRun() {
        assertEquals(StreakInfo(3, 3), StreakTracker.streaks(setOf(0, 1, 2), 3))
    }

    @Test
    fun twoAbsentDaysZeroCurrent() {
        assertEquals(StreakInfo(0, 2), StreakTracker.streaks(setOf(0, 1), 3))
    }

    @Test
    fun ignoresNegativeDays() {
        assertEquals(StreakInfo(2, 2), StreakTracker.streaks(setOf(-5, 0, 1), 1))
    }

    @Test
    fun bestSurvivesBrokenCurrent() {
        assertEquals(StreakInfo(1, 4), StreakTracker.streaks(setOf(0, 1, 2, 3, 7), 7))
    }

    @Test
    fun ignoresFutureDays() {
        assertEquals(StreakInfo(1, 1), StreakTracker.streaks(setOf(0, 9), 0))
    }

    @Test
    fun emptyHistoryIsZero() {
        assertEquals(StreakInfo(0, 0), StreakTracker.streaks(emptySet(), 3))
    }

    @Test
    fun rejectsNegativeToday() {
        assertFailsWith<IllegalArgumentException> { StreakTracker.streaks(setOf(0), -1) }
    }
}
