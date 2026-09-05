package com.sanctum.core.feature.fasting.domain

import com.sanctum.core.feature.scripture.presentation.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FastingCountdownEngine {

    /**
     * Calculates the current FastingState.
     * Assumes prayers list contains at least "Fajr" and "Maghrib".
     * time is formatted as "HH:mm" and amPm as "AM" or "PM".
     * currentTimeInMillis should be epoch milliseconds.
     */
    fun calculateFastingState(currentTimeInMillis: Long, prayers: List<PrayerTime>): FastingState? {
        val fajrPrayer = prayers.find { it.name.equals("Fajr", ignoreCase = true) }
        val maghribPrayer = prayers.find { it.name.equals("Maghrib", ignoreCase = true) }

        if (fajrPrayer == null || maghribPrayer == null) {
            return null
        }

        // We need to compare time of day. We'll extract current hours/minutes from the instant in the system timezone
        val instant = Instant.fromEpochMilliseconds(currentTimeInMillis)
        // Note: In a real app we might want to pass the timezone or location,
        // but since PrayerEngine just offsets from UTC, let's assume we can use system timezone or UTC depending on how it's designed.
        // For simplicity, let's use the local time from the Instant.
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentTotalMinutes = localDateTime.hour * 60 + localDateTime.minute

        val fajrMinutes = parseTimeToMinutes(fajrPrayer.time, fajrPrayer.amPm)
        val maghribMinutes = parseTimeToMinutes(maghribPrayer.time, maghribPrayer.amPm)

        return when {
            currentTotalMinutes < fajrMinutes -> {
                // Before Fajr -> Eating Window, next event is Suhoor Cutoff (Fajr)
                val diff = fajrMinutes - currentTotalMinutes
                FastingState(
                    phase = FastingPhase.EATING_WINDOW,
                    targetEventName = "Suhoor Ends",
                    remainingHours = diff / 60,
                    remainingMinutes = diff % 60,
                )
            }
            currentTotalMinutes < maghribMinutes -> {
                // Between Fajr and Maghrib -> Active Fast, next event is Iftar (Maghrib)
                val diff = maghribMinutes - currentTotalMinutes
                FastingState(
                    phase = FastingPhase.ACTIVE_FAST,
                    targetEventName = "Iftar",
                    remainingHours = diff / 60,
                    remainingMinutes = diff % 60,
                )
            }
            else -> {
                // After Maghrib -> Eating Window, next event is tomorrow's Suhoor Cutoff
                // We'll approximate tomorrow's Fajr to be at the same time as today's
                val diff = (24 * 60 - currentTotalMinutes) + fajrMinutes
                FastingState(
                    phase = FastingPhase.EATING_WINDOW,
                    targetEventName = "Suhoor Ends",
                    remainingHours = diff / 60,
                    remainingMinutes = diff % 60,
                )
            }
        }
    }

    private fun parseTimeToMinutes(timeStr: String, amPm: String): Int {
        val parts = timeStr.split(":")
        if (parts.size != 2) return 0
        var hours = parts[0].toIntOrNull() ?: 0
        val minutes = parts[1].toIntOrNull() ?: 0

        if (amPm.equals("PM", ignoreCase = true) && hours < 12) {
            hours += 12
        } else if (amPm.equals("AM", ignoreCase = true) && hours == 12) {
            hours = 0
        }

        return hours * 60 + minutes
    }
}
