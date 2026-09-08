package com.sanctum.core.feature.goals.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
class GoalEngineTest {

    private val verses5 = GoalDefinition("g1", GoalKind.VERSES_PER_DAY, 5)

    @Test
    fun metTodayBuildsStreak() {
        val status = GoalEngine.evaluate(verses5, mapOf(0 to 5, 1 to 7, 2 to 5), 2)
        assertEquals(5, status.todayCount)
        assertTrue(status.targetMetToday)
        assertEquals(1f, status.todayProgress)
        assertEquals(3, status.currentStreakDays)
        assertEquals(3, status.totalDaysMet)
    }

    @Test
    fun missedTodayKeepsYesterdayStreak() {
        val status = GoalEngine.evaluate(verses5, mapOf(0 to 5, 1 to 6, 2 to 2), 2)
        assertEquals(2, status.todayCount)
        assertFalse(status.targetMetToday)
        assertTrue(status.todayProgress in 0.39f..0.41f)
        assertEquals(2, status.currentStreakDays)
        assertEquals(2, status.totalDaysMet)
    }

    @Test
    fun brokenStreakResets() {
        val status = GoalEngine.evaluate(verses5, mapOf(0 to 5, 1 to 0, 2 to 9, 3 to 9), 3)
        assertEquals(2, status.currentStreakDays)
        assertEquals(3, status.totalDaysMet)
    }

    @Test
    fun ignoresFutureDays() {
        val status = GoalEngine.evaluate(verses5, mapOf(0 to 5, 9 to 99), 0)
        assertEquals(1, status.currentStreakDays)
        assertEquals(1, status.totalDaysMet)
    }

    @Test
    fun emptyHistoryIsZero() {
        val status = GoalEngine.evaluate(verses5, emptyMap(), 3)
        assertEquals(0, status.todayCount)
        assertFalse(status.targetMetToday)
        assertEquals(0f, status.todayProgress)
        assertEquals(0, status.currentStreakDays)
        assertEquals(0, status.totalDaysMet)
    }

    @Test
    fun rejectsInvalidInput() {
        assertFailsWith<IllegalArgumentException> { GoalDefinition("g", GoalKind.MINUTES_PER_DAY, 0) }
        assertFailsWith<IllegalArgumentException> { GoalEngine.evaluate(verses5, emptyMap(), -1) }
    }
}
