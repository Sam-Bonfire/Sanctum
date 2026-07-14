package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.scripture.presentation.PrayerTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

class PrayerScheduleUseCase(
    private val prayerEngine: PrayerEngine,
) {
    fun calculateSchedule(
        latitude: Double,
        longitude: Double,
        dateInMillis: Long = 0L,
        religionId: String = "islamic",
    ): List<PrayerTime> {
        return prayerEngine.calculateDailySchedule(latitude, longitude, dateInMillis, religionId)
    }

    fun parsePrayerTimeToMillis(prayer: PrayerTime): Long? {
        val timeParts = prayer.time.split(":")
        if (timeParts.size != 2) return null

        var hours = timeParts[0].toIntOrNull() ?: 0
        val mins = timeParts[1].substringBefore(" ").toIntOrNull() ?: 0

        if (prayer.amPm == "PM" && hours != 12) {
            hours += 12
        } else if (prayer.amPm == "AM" && hours == 12) {
            hours = 0
        }

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val targetDate = LocalDateTime(currentDateTime.year, currentDateTime.monthNumber, currentDateTime.dayOfMonth, hours, mins, 0)
        return targetDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun getNextPrayerAndRemainingTime(schedule: List<PrayerTime>): Pair<PrayerTime?, Duration> {
        val now = Clock.System.now()
        var nextPrayer: PrayerTime? = null
        var minDiff = Long.MAX_VALUE

        for (prayer in schedule) {
            val prayerMillis = parsePrayerTimeToMillis(prayer) ?: continue
            val diff = prayerMillis - now.toEpochMilliseconds()
            if (diff in 1..<minDiff) {
                minDiff = diff
                nextPrayer = prayer
            }
        }

        // If all prayers today have passed, we would ideally check tomorrow's schedule.
        // For MVP, if none found, we just return null or the first prayer.
        if (nextPrayer == null && schedule.isNotEmpty()) {
            val firstPrayerMillis = parsePrayerTimeToMillis(schedule.first()) ?: return Pair(null, Duration.ZERO)
            // Add 24 hours (86400000 ms) for tomorrow's first prayer
            val diff = (firstPrayerMillis + 86400000L) - now.toEpochMilliseconds()
            return Pair(schedule.first(), kotlin.time.Duration.parse("${diff}ms"))
        }

        return Pair(nextPrayer, kotlin.time.Duration.parse("${minDiff}ms"))
    }
}
