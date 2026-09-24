package com.sanctum.core.feature.charity.domain

import com.sanctum.core.core.money.MinorUnits
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
    val amount: MinorUnits,
    val dateIso: String,
    val categoryId: CharityCategory,
    val privateNotes: String?,
)

@Serializable
data class CharityGoal(
    val monthlyGoalAmount: MinorUnits,
)

data class CharitySummary(
    val totalGiven: MinorUnits,
    val goalAmount: MinorUnits,
    val percentageCompletion: Float,
)
