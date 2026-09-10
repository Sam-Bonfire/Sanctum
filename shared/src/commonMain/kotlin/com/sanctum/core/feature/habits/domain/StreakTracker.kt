package com.sanctum.core.feature.habits.domain

data class StreakInfo(
    val currentStreakDays: Int,
    val bestStreakDays: Int,
)

/**
 * Canonical consecutive-day streak calculator over a set of active day
 * indices. Day-index-to-date mapping is owned by the caller (S-121 logging);
 * past days absent from [activeDays] break the streak, but a not-yet-active
 * today does not zero it: current counts back from today, or from yesterday
 * when today is absent. Future and negative indices are ignored.
 */
object StreakTracker {

    fun streaks(activeDays: Set<Int>, todayIndex: Int): StreakInfo {
        require(todayIndex >= 0) { "todayIndex must be >= 0" }
        val past = activeDays.filter { it in 0..todayIndex }.toSet()

        var current = 0
        var day = if (todayIndex in past) todayIndex else todayIndex - 1
        while (day in past) {
            current++
            day--
        }

        var best = 0
        var run = 0
        var prev = Int.MIN_VALUE
        for (d in past.sorted()) {
            run = if (d == prev + 1) run + 1 else 1
            if (run > best) best = run
            prev = d
        }

        return StreakInfo(current, best)
    }
}
