package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.prayer.domain.AsrJuristicMethod
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PrayerEngineTest {

    @Test
    fun testIslamicPrayerScheduleIsCalculated() {
        val engine = BaselinePrayerEngine()
        val schedule = engine.calculateDailySchedule(
            latitude = 40.7128,
            longitude = -74.0060,
            dateInMillis = Clock.System.now().toEpochMilliseconds(),
            religionId = "islam",
            asrJuristicMethod = AsrJuristicMethod.STANDARD_SHAFII,
        )

        assertEquals(5, schedule.size)
        assertTrue(schedule.any { it.name == "Fajr" })
        assertTrue(schedule.any { it.name == "Dhuhr" })
        assertTrue(schedule.any { it.name == "Asr" })
        assertTrue(schedule.any { it.name == "Maghrib" })
        assertTrue(schedule.any { it.name == "Isha" })
    }

    @Test
    fun testAsrTimesDifferBetweenMadhabs() {
        val engine = BaselinePrayerEngine()
        val testDate = Instant.parse("2024-03-15T12:00:00Z").toEpochMilliseconds()

        val shafiiSchedule = engine.calculateDailySchedule(
            latitude = 21.4225,
            longitude = 39.8262,
            dateInMillis = testDate,
            religionId = "islam",
            asrJuristicMethod = AsrJuristicMethod.STANDARD_SHAFII,
        )
        val shafiiAsr = shafiiSchedule.first { it.name == "Asr" }

        val hanafiSchedule = engine.calculateDailySchedule(
            latitude = 21.4225,
            longitude = 39.8262,
            dateInMillis = testDate,
            religionId = "islam",
            asrJuristicMethod = AsrJuristicMethod.HANAFI,
        )
        val hanafiAsr = hanafiSchedule.first { it.name == "Asr" }

        // Since Hanafi uses 2x object height, it should be later in the day than Shafii.
        // Therefore, the time string should differ.
        assertTrue(
            shafiiAsr.time != hanafiAsr.time,
            "Asr times should differ between Shafii (${shafiiAsr.time}) and Hanafi (${hanafiAsr.time}) methods.",
        )
    }

    @Test
    fun testJewishPrayerScheduleIsCalculated() {
        val engine = BaselinePrayerEngine()
        val schedule = engine.calculateDailySchedule(
            latitude = 40.7128,
            longitude = -74.0060,
            dateInMillis = Clock.System.now().toEpochMilliseconds(),
            religionId = "jewish",
        )
        assertEquals(3, schedule.size)
    }
}
