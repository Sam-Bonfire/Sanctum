package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.scripture.presentation.PrayerTime

/**
 * Agnostic engine for calculating daily devotional times (e.g. Zmanim, Islamic Salah, etc.)
 */
interface PrayerEngine {
    fun calculateDailySchedule(latitude: Double, longitude: Double, dateInMillis: Long, religionId: String): List<PrayerTime>
}

class BaselinePrayerEngine : PrayerEngine {
    override fun calculateDailySchedule(
        latitude: Double,
        longitude: Double,
        dateInMillis: Long,
        religionId: String,
    ): List<PrayerTime> {
        // A simple approximation for the MVP: every 15 degrees of longitude is approx 1 hour offset from UTC.
        // We offset a base UTC schedule to the local solar time using the provided live GPS coordinates.
        val solarOffsetHours = (longitude / 15.0).toInt()

        fun offsetTime(baseHour: Int, baseMin: Int): Pair<String, String> {
            var newHour = (baseHour + solarOffsetHours) % 24
            if (newHour < 0) newHour += 24
            val amPm = if (newHour >= 12) "PM" else "AM"
            val displayHour = if (newHour % 12 == 0) 12 else newHour % 12
            val formattedTime = "${displayHour.toString().padStart(2, '0')}:${baseMin.toString().padStart(2, '0')}"
            return Pair(formattedTime, amPm)
        }

        return when (religionId) {
            "islam" -> {
                val fajr = offsetTime(5, 12)
                val dhuhr = offsetTime(12, 0) // Solar Noon
                val maghrib = offsetTime(18, 0) // Sunset approximation
                listOf(
                    PrayerTime("Fajr", fajr.first, fajr.second, isCurrent = false),
                    PrayerTime("Dhuhr", dhuhr.first, dhuhr.second, isCurrent = true),
                    PrayerTime("Asr", offsetTime(15, 30).first, offsetTime(15, 30).second, isCurrent = false),
                    PrayerTime("Maghrib", maghrib.first, maghrib.second, isCurrent = false),
                    PrayerTime("Isha", offsetTime(19, 30).first, offsetTime(19, 30).second, isCurrent = false),
                )
            }
            "jewish" -> {
                val shacharit = offsetTime(7, 0)
                val mincha = offsetTime(15, 30)
                listOf(
                    PrayerTime("Shacharit", shacharit.first, shacharit.second, isCurrent = false),
                    PrayerTime("Mincha", mincha.first, mincha.second, isCurrent = true),
                    PrayerTime("Arvit", offsetTime(18, 15).first, offsetTime(18, 15).second, isCurrent = false),
                )
            }
            else -> listOf(
                PrayerTime("Morning", offsetTime(6, 0).first, offsetTime(6, 0).second, isCurrent = false),
                PrayerTime("Noon", offsetTime(12, 0).first, offsetTime(12, 0).second, isCurrent = true),
            )
        }
    }
}
