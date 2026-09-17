package com.sanctum.core.core.notifications

import kotlinx.serialization.Serializable

@Serializable
enum class BackgroundTask {
    DUA_REMINDER,
    VERSE_REFRESH,
    SYNC,
}

@Serializable
data class BackgroundSchedule(
    val task: BackgroundTask,
    val intervalHours: Int,
    val requiresCharging: Boolean = false,
)

fun BackgroundSchedule.isValid(): Boolean = intervalHours in 1..168

fun defaultSchedules(): List<BackgroundSchedule> = listOf(
    BackgroundSchedule(BackgroundTask.DUA_REMINDER, 24),
    BackgroundSchedule(BackgroundTask.VERSE_REFRESH, 24),
    BackgroundSchedule(BackgroundTask.SYNC, 12),
)
