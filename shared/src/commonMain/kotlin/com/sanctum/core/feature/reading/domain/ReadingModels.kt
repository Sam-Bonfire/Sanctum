package com.sanctum.core.feature.reading.domain

import kotlinx.serialization.Serializable

enum class PlanCategory(val displayName: String) {
    FOUNDATIONS("Foundations"),
    PSALMS("Psalms"),
    PROPHETS("Prophets"),
    GOSPEL("Gospel"),
    EPISTLES("Epistles"),
    CUSTOM("Custom"),
    WISDOM("Wisdom"),
    BIBLE("Bible"),
    QURAN("Quran"),
}

@Serializable
data class ReadingPlan(
    val id: String,
    val title: String,
    val description: String,
    val category: PlanCategory,
    val dayCount: Int,
    val checkpointsPerDay: Int,
    val verseRefs: List<String>,
)

@Serializable
data class DailyCheckpoint(
    val planId: String,
    val dayIndex: Int,
    val checkpointIndex: Int,
    val verseRef: String,
    val label: String,
)

@Serializable
data class ReadingProgress(
    val planId: String,
    val completedDays: Set<Int> = emptySet(),
    val completedCheckpoints: Set<String> = emptySet(),
    val enrolledAt: Long = 0L,
)

// ponytail: category-based allowlist, per-plan flavor tags if denominations need finer splits
fun ReadingPlan.isVisibleFor(flavorId: String): Boolean =
    when (category) {
        PlanCategory.QURAN -> flavorId == "islam"
        PlanCategory.BIBLE, PlanCategory.GOSPEL -> flavorId == "christianity"
        else -> true
    }

fun List<ReadingPlan>.visibleFor(flavorId: String): List<ReadingPlan> = filter { it.isVisibleFor(flavorId) }
