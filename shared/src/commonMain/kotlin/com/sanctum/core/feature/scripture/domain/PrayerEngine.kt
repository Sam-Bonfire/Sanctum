package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.prayer.domain.AsrJuristicMethod
import com.sanctum.core.feature.scripture.presentation.PrayerTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

/**
 * Agnostic engine for calculating daily devotional times (e.g. Zmanim, Islamic Salah, etc.)
 */
interface PrayerEngine {
    fun calculateDailySchedule(
        latitude: Double,
        longitude: Double,
        dateInMillis: Long,
        religionId: String,
        asrJuristicMethod: AsrJuristicMethod? = null,
    ): List<PrayerTime>
}

class BaselinePrayerEngine : PrayerEngine {
    override fun calculateDailySchedule(
        latitude: Double,
        longitude: Double,
        dateInMillis: Long,
        religionId: String,
        asrJuristicMethod: AsrJuristicMethod?,
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

                val timeZone = TimeZone.currentSystemDefault()
                val instant = Instant.fromEpochMilliseconds(dateInMillis)
                val localDateTime = instant.toLocalDateTime(timeZone)
                val dayOfYear = localDateTime.date.dayOfYear

                val fractionalYear = (2 * PI / 365.0) * (dayOfYear - 1.0 + (12.0 - 12.0) / 24.0)

                val declination = 0.006918 - 0.399912 * cos(fractionalYear) + 0.070257 * sin(fractionalYear) -
                    0.006758 * cos(2 * fractionalYear) + 0.000907 * sin(2 * fractionalYear) -
                    0.002697 * cos(3 * fractionalYear) + 0.00148 * sin(3 * fractionalYear)

                val latRad = latitude * PI / 180.0
                val shadowMultiplier = if (asrJuristicMethod == AsrJuristicMethod.HANAFI) 2.0 else 1.0
                val asrAltitude = atan(1.0 / (shadowMultiplier + tan(abs(latRad - declination))))
                val hourAngle = acos((sin(asrAltitude) - sin(latRad) * sin(declination)) / (cos(latRad) * cos(declination)))
                val asrHourOffset = hourAngle * 180.0 / PI / 15.0

                val eqTime = 229.18 * (
                    0.000075 + 0.001868 * cos(fractionalYear) - 0.032077 * sin(fractionalYear) -
                        0.014615 * cos(2 * fractionalYear) - 0.040849 * sin(2 * fractionalYear)
                    )

                val timeOffset = eqTime + 4.0 * longitude
                val solarNoonUTC = 12.0 - timeOffset / 60.0
                val asrUTC = solarNoonUTC + asrHourOffset
                val asrLocalTime = asrUTC + solarOffsetHours
                var asrHour = asrLocalTime.toInt()
                val asrMin = ((asrLocalTime - asrHour) * 60).roundToInt()

                var adjustedAsrMin = asrMin
                if (adjustedAsrMin >= 60) {
                    adjustedAsrMin -= 60
                    asrHour += 1
                } else if (adjustedAsrMin < 0) {
                    adjustedAsrMin += 60
                    asrHour -= 1
                }

                var newAsrHour = asrHour % 24
                if (newAsrHour < 0) newAsrHour += 24
                val asrAmPm = if (newAsrHour >= 12) "PM" else "AM"
                val displayAsrHour = if (newAsrHour % 12 == 0) 12 else newAsrHour % 12
                val formattedAsrTime = "${displayAsrHour.toString().padStart(2, '0')}:${adjustedAsrMin.toString().padStart(2, '0')}"

                listOf(
                    PrayerTime("Fajr", fajr.first, fajr.second, isCurrent = false),
                    PrayerTime("Dhuhr", dhuhr.first, dhuhr.second, isCurrent = true),
                    PrayerTime("Asr", formattedAsrTime, asrAmPm, isCurrent = false),
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
