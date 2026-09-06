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
