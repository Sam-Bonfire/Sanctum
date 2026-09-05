package com.sanctum.core.feature.fasting.domain

enum class FastingStatus {
    COMPLETED,
    MISSED,
    EXEMPT,
}

enum class FastingPhase {
    EATING_WINDOW,
    ACTIVE_FAST,
}

data class FastingDayRecord(
    val dayOfRamadan: Int,
    val status: FastingStatus?,
    val notes: String,
)

data class FastingState(
    val phase: FastingPhase,
    val targetEventName: String,
    val remainingHours: Int,
    val remainingMinutes: Int,
)
