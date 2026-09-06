package com.sanctum.core.feature.duas.domain

import com.russhwolf.settings.Settings
import com.sanctum.core.core.notifications.PlatformNotificationManager
import com.sanctum.core.feature.duas.data.DuasRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class DailyDuaNotificationScheduler(
    private val duasRepository: DuasRepository,
    private val notificationManager: PlatformNotificationManager,
    private val settings: Settings,
) {
    companion object {
        const val NOTIFICATION_ID = 2000
    }

    suspend fun scheduleDailyNotification(configTitle: String) {
        val isEnabled = settings.getBoolean("daily_dua_enabled", false)
        if (!isEnabled) {
            notificationManager.cancelNotification(NOTIFICATION_ID)
            return
        }

        val religionId = settings.getString("religion_id", "")
        if (religionId.isEmpty()) return

        val hour = settings.getInt("daily_dua_hour", 8)
        val minute = settings.getInt("daily_dua_minute", 0)

        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val nowLocal = now.toLocalDateTime(timeZone)

        var scheduledTimeLocal = LocalDateTime(
            year = nowLocal.year,
            monthNumber = nowLocal.monthNumber,
            dayOfMonth = nowLocal.dayOfMonth,
            hour = hour,
            minute = minute,
            second = 0,
            nanosecond = 0,
        )

        var scheduledInstant = scheduledTimeLocal.toInstant(timeZone)
        if (scheduledInstant <= now) {
            scheduledInstant = scheduledInstant.plus(1, DateTimeUnit.DAY, timeZone)
        }

        try {
            val duas = duasRepository.getDuas(religionId)
            if (duas.isNotEmpty()) {
                val todayDua = duas.first() // or could be based on day of year
                val message = todayDua.translation.take(50) + if (todayDua.translation.length > 50) "..." else ""

                notificationManager.scheduleNotification(
                    id = NOTIFICATION_ID,
                    title = configTitle,
                    message = message,
                    triggerTimeInMillis = scheduledInstant.toEpochMilliseconds(),
                    alertType = "DEFAULT",
                    soundFileName = null,
                )
            }
        } catch (e: Exception) {
            // handle error if needed
        }
    }
}
