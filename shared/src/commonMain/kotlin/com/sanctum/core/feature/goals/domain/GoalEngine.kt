package com.sanctum.core.feature.goals.domain

enum class GoalKind(val displayName: String, val unitName: String) {
    VERSES_PER_DAY("Verses per day", "verses"),
    CHAPTERS_PER_DAY("Chapters per day", "chapters"),
    MINUTES_PER_DAY("Minutes per day", "minutes"),
}

data class GoalDefinition(
    val id: String,
    val kind: GoalKind,
    val dailyTarget: Int,
) {
    init {
        require(dailyTarget > 0) { "dailyTarget must be > 0" }
    }
}

data class GoalStatus(
    val todayCount: Int,
    val targetMetToday: Boolean,
    val todayProgress: Float,
    val currentStreakDays: Int,
    val totalDaysMet: Int,
)

/**
 * Evaluates a daily goal against per-day counts keyed by day index.
 *
 * Daily counts come from future activity logging (S-121); the engine takes
 * them as input so it stays pure and testable. Only days up to [todayIndex]
 * count; future entries are ignored. Days absent from [dailyCounts] count as
 * zero. A streak ending yesterday still counts as current until today is missed.
 */
object GoalEngine {

    fun evaluate(
        definition: GoalDefinition,
        dailyCounts: Map<Int, Int>,
        todayIndex: Int,
    ): GoalStatus {
        require(todayIndex >= 0) { "todayIndex must be >= 0" }
        val past = dailyCounts
            .filterKeys { it <= todayIndex }
            .mapValues { it.value.coerceAtLeast(0) }
        val todayCount = past[todayIndex] ?: 0
        val met = { day: Int -> (past[day] ?: 0) >= definition.dailyTarget }

        var streak = 0
        var day = if (met(todayIndex)) todayIndex else todayIndex - 1
        while (day >= 0 && met(day)) {
            streak++
            day--
        }

        return GoalStatus(
            todayCount = todayCount,
            targetMetToday = met(todayIndex),
            todayProgress = (todayCount.toFloat() / definition.dailyTarget).coerceIn(0f, 1f),
            currentStreakDays = streak,
            totalDaysMet = past.keys.count { met(it) },
        )
    }
}
