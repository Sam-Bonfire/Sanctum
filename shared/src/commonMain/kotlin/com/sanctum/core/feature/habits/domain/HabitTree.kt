package com.sanctum.core.feature.habits.domain

enum class GrowthStage(val displayName: String, val minStreakDays: Int) {
    SEED("Seed", 0),
    SPROUT("Sprout", 1),
    SAPLING("Sapling", 7),
    TREE("Tree", 14),
    BLOOMING("Blooming", 30),
    EVERGREEN("Evergreen", 60),
}

data class HabitTreeState(
    val stage: GrowthStage,
    /** Days remaining to the next stage, null when fully grown. */
    val daysToNextStage: Int?,
    /** 0..1 progress within the current stage, null when fully grown. */
    val stageProgress: Float?,
)

/**
 * Maps a consecutive-day streak to a visual tree growth state.
 *
 * Thresholds are intentionally simple weekly-ish milestones; tune them once
 * real engagement data exists. Fully grown (EVERGREEN) trees report null
 * [HabitTreeState.daysToNextStage] so UI can show a "maxed" treatment.
 */
object HabitTree {

    fun growthForStreak(streakDays: Int): HabitTreeState {
        require(streakDays >= 0) { "streakDays must be >= 0" }
        val stages = GrowthStage.entries
        val index = stages.indexOfLast { streakDays >= it.minStreakDays }
        val stage = stages[index]
        val next = stages.getOrNull(index + 1)
            ?: return HabitTreeState(stage, null, null)
        val span = next.minStreakDays - stage.minStreakDays
        return HabitTreeState(
            stage = stage,
            daysToNextStage = next.minStreakDays - streakDays,
            stageProgress = ((streakDays - stage.minStreakDays).toFloat() / span).coerceIn(0f, 1f),
        )
    }
}
