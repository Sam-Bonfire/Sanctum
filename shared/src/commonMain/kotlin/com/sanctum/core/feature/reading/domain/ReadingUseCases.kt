package com.sanctum.core.feature.reading.domain

data class ReadingPlanState(
    val plan: ReadingPlan,
    val progress: ReadingProgress,
    val isEnrolled: Boolean,
    val streakDays: Int,
    val completionPercent: Float,
) {
    val completedCheckpointCount: Int get() = progress.completedCheckpoints.size
    val totalCheckpointCount: Int get() = plan.dayCount * plan.checkpointsPerDay
    val todayCheckpointKeys: List<String>
        get() {
            val todayIndex = currentDayIndex()
            return (0 until plan.checkpointsPerDay).map { "${plan.id}_day${todayIndex}_cp$it" }
        }

    fun currentDayIndex(): Int {
        if (progress.enrolledAt == 0L) return 0
        val elapsed = (currentTimeMillis() - progress.enrolledAt) / (24 * 60 * 60 * 1000L)
        return elapsed.toInt().coerceIn(0, plan.dayCount - 1)
    }
}

class GetEnrolledPlansUseCase(private val repository: ReadingPlanRepository) {
    operator fun invoke(): List<ReadingPlanState> {
        val enrolledIds = repository.getEnrolledPlanIds()
        return repository.getAvailablePlans()
            .filter { it.id in enrolledIds }
            .map { plan ->
                val progress = repository.getProgress(plan.id)
                ReadingPlanState(
                    plan = plan,
                    progress = progress,
                    isEnrolled = true,
                    streakDays = calculateStreak(progress),
                    completionPercent = if (plan.dayCount > 0) {
                        progress.completedDays.size.toFloat() / plan.dayCount
                    } else {
                        0f
                    },
                )
            }
    }
}

class GetAvailablePlansUseCase(private val repository: ReadingPlanRepository) {
    operator fun invoke(): List<ReadingPlan> = repository.getAvailablePlans()
}

class ToggleCheckpointCompletedUseCase(private val repository: ReadingPlanRepository) {
    operator fun invoke(planId: String, checkpointKey: String, completed: Boolean) {
        repository.setCheckpointCompleted(planId, checkpointKey, completed)
        val progress = repository.getProgress(planId)
        val plan = repository.getAvailablePlans().find { it.id == planId }
        if (plan != null) {
            val todayIndex = ReadingPlanState(plan, progress, true, 0, 0f).currentDayIndex()
            val todayKeys = (0 until plan.checkpointsPerDay).map { "${planId}_day${todayIndex}_cp$it" }
            val allCompleted = todayKeys.all { repository.isCheckpointCompleted(planId, it) }
            val days = progress.completedDays.toMutableSet()
            if (allCompleted) days.add(todayIndex) else days.remove(todayIndex)
            repository.setCheckpointCompleted(planId, checkpointKey, completed)
        }
    }
}

class GetDailyReadingTargetUseCase(private val repository: ReadingPlanRepository) {
    operator fun invoke(planId: String): List<DailyCheckpoint> {
        val plan = repository.getAvailablePlans().find { it.id == planId } ?: return emptyList()
        val state = ReadingPlanState(
            plan = plan,
            progress = repository.getProgress(planId),
            isEnrolled = true,
            streakDays = 0,
            completionPercent = 0f,
        )
        val dayIndex = state.currentDayIndex()
        return (0 until plan.checkpointsPerDay).map { cpIdx ->
            DailyCheckpoint(
                planId = planId,
                dayIndex = dayIndex,
                checkpointIndex = cpIdx,
                verseRef = plan.verseRefs.getOrElse(dayIndex * plan.checkpointsPerDay + cpIdx) { "" },
                label = "Day ${dayIndex + 1} - Reading ${cpIdx + 1}",
            )
        }
    }
}

private fun calculateStreak(progress: ReadingProgress): Int {
    val sorted = progress.completedDays.sortedDescending()
    if (sorted.isEmpty()) return 0
    var streak = 1
    for (i in 1 until sorted.size) {
        if (sorted[i - 1] - sorted[i] == 1) streak++ else break
    }
    return streak
}

private fun currentTimeMillis(): Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
