package com.sanctum.core.feature.reading.data

import com.russhwolf.settings.Settings
import com.sanctum.core.feature.reading.domain.PlanCategory
import com.sanctum.core.feature.reading.domain.ReadingPlan
import com.sanctum.core.feature.reading.domain.ReadingPlanRepository
import com.sanctum.core.feature.reading.domain.ReadingProgress
import kotlinx.serialization.json.Json

class SettingsReadingPlanRepository(
    private val settings: Settings,
) : ReadingPlanRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val availablePlans: List<ReadingPlan> = listOf(
        ReadingPlan(
            id = "nt_in_a_year",
            title = "New Testament in a Year",
            description = "Read through the entire New Testament in 365 days.",
            category = PlanCategory.GOSPEL,
            dayCount = 365,
            checkpointsPerDay = 1,
            verseRefs = emptyList(),
        ),
        ReadingPlan(
            id = "psalms_30",
            title = "Psalms in 30 Days",
            description = "Read through the Book of Psalms in 30 days.",
            category = PlanCategory.PSALMS,
            dayCount = 30,
            checkpointsPerDay = 5,
            verseRefs = emptyList(),
        ),
        ReadingPlan(
            id = "genesis_30",
            title = "Genesis in 30 Days",
            description = "Read through Genesis in 30 days.",
            category = PlanCategory.FOUNDATIONS,
            dayCount = 30,
            checkpointsPerDay = 3,
            verseRefs = emptyList(),
        ),
        ReadingPlan(
            id = "proverbs_31",
            title = "Proverbs in 31 Days",
            description = "Read one chapter of Proverbs each day for a month.",
            category = PlanCategory.WISDOM,
            dayCount = 31,
            checkpointsPerDay = 1,
            verseRefs = emptyList(),
        ),
    )

    override fun getAvailablePlans(): List<ReadingPlan> = availablePlans

    override fun getEnrolledPlanIds(): Set<String> {
        val raw = settings.getString(KEY_ENROLLED, "")
        return if (raw.isEmpty()) emptySet() else raw.split(",").toSet()
    }

    override fun enroll(planId: String) {
        val current = getEnrolledPlanIds().toMutableSet()
        current.add(planId)
        settings.putString(KEY_ENROLLED, current.joinToString(","))
        settings.putLong(keyEnrolledAt(planId), currentTimeMillis())
    }

    override fun unenroll(planId: String) {
        val current = getEnrolledPlanIds().toMutableSet()
        current.remove(planId)
        settings.putString(KEY_ENROLLED, current.joinToString(","))
        settings.remove(keyEnrolledAt(planId))
        settings.remove(keyCompletedDays(planId))
        settings.remove(keyCompletedCheckpoints(planId))
    }

    override fun getProgress(planId: String): ReadingProgress {
        val completedDaysStr = settings.getString(keyCompletedDays(planId), "")
        val completedDays = if (completedDaysStr.isEmpty()) {
            emptySet()
        } else {
            completedDaysStr.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }

        val completedCheckpointsStr = settings.getString(keyCompletedCheckpoints(planId), "")
        val completedCheckpoints = if (completedCheckpointsStr.isEmpty()) {
            emptySet()
        } else {
            completedCheckpointsStr.split(",").toSet()
        }

        val enrolledAt = settings.getLong(keyEnrolledAt(planId), 0L)

        return ReadingProgress(planId, completedDays, completedCheckpoints, enrolledAt)
    }

    override fun isCheckpointCompleted(planId: String, checkpointKey: String): Boolean {
        val completed = settings.getString(keyCompletedCheckpoints(planId), "")
        return completed.split(",").contains(checkpointKey)
    }

    override fun setCheckpointCompleted(planId: String, checkpointKey: String, completed: Boolean) {
        val current = settings.getString(keyCompletedCheckpoints(planId), "")
            .split(",").filter { it.isNotEmpty() }.toMutableSet()
        if (completed) current.add(checkpointKey) else current.remove(checkpointKey)
        settings.putString(keyCompletedCheckpoints(planId), current.joinToString(","))
    }

    private fun currentTimeMillis(): Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

    private fun keyEnrolledAt(planId: String): String = "reading_enrolled_at_$planId"
    private fun keyCompletedDays(planId: String): String = "reading_completed_days_$planId"
    private fun keyCompletedCheckpoints(planId: String): String = "reading_completed_checkpoints_$planId"

    companion object {
        private const val KEY_ENROLLED = "reading_enrolled_plans"
    }
}
