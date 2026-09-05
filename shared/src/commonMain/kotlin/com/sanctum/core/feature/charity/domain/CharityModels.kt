package com.sanctum.core.feature.charity.domain

import kotlinx.serialization.Serializable

@Serializable
enum class CharityCategory(val displayName: String) {
    ZAKAT("Zakat"),
    SADAQAH("Sadaqah"),
    GENERAL("General"),
    TZEDAKAH("Tzedakah"),
    TITHES("Tithes"),
    OFFERING("Offering"),
    OTHER("Other"),
}

@Serializable
data class CharityRecord(
    val id: String,
    val amount: Double,
    val dateIso: String,
    val categoryId: CharityCategory,
    val privateNotes: String?,
)

@Serializable
data class CharityGoal(
    val monthlyGoalAmount: Double,
)

data class CharitySummary(
    val totalGiven: Double,
    val goalAmount: Double,
    val percentageCompletion: Float,
)
