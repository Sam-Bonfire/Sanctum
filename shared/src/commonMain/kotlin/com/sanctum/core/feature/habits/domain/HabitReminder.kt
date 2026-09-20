package com.sanctum.core.feature.habits.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class HabitReminder(
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<Int>,
    val isEnabled: Boolean,
) {
    init {
        require(hour in 0..23) { "Hour must be between 0 and 23" }
        require(minute in 0..59) { "Minute must be between 0 and 59" }
        require(daysOfWeek.all { it in 1..7 }) { "Days of week must be between 1 and 7" }
    }

    fun computeNextFireTimeMillis(
        currentEpochMillis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Long? {
        if (!isEnabled || daysOfWeek.isEmpty()) {
            return null
        }

        val currentInstant: Instant = Instant.fromEpochMilliseconds(currentEpochMillis)
        var daysAdded: Int = 0

        while (daysAdded <= 7) {
            val candidateInstant: Instant = currentInstant.plus(daysAdded, DateTimeUnit.DAY, timeZone)
            val tempDateTime: LocalDateTime = candidateInstant.toLocalDateTime(timeZone)
            val candidateDateTime: LocalDateTime = LocalDateTime(
                year = tempDateTime.year,
                monthNumber = tempDateTime.monthNumber,
                dayOfMonth = tempDateTime.dayOfMonth,
                hour = hour,
                minute = minute,
                second = 0,
                nanosecond = 0,
            )

            val nextInstant: Instant = candidateDateTime.toInstant(timeZone)
            if (nextInstant.toEpochMilliseconds() > currentEpochMillis && daysOfWeek.contains(candidateDateTime.dayOfWeek.isoDayNumber)) {
                return nextInstant.toEpochMilliseconds()
            }
            daysAdded += 1
        }

        return null
    }
}
