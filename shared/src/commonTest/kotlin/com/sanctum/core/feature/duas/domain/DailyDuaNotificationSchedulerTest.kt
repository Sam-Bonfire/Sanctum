package com.sanctum.core.feature.duas.domain

import com.russhwolf.settings.MapSettings
import com.sanctum.core.core.notifications.PlatformNotificationManager
import com.sanctum.core.feature.duas.data.DuasRepository
import com.sanctum.core.feature.duas.presentation.Dua
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeDuasRepository : DuasRepository {
    override suspend fun getDuas(religionId: String): List<Dua> {
        return listOf(Dua(id = "1", title = "Test Dua", originalText = "Original", translation = "Test Translation"))
    }
}

class FakeNotificationManager : PlatformNotificationManager {
    var scheduledId: Int? = null
    var scheduledTriggerTime: Long? = null
    var canceledId: Int? = null

    override fun scheduleNotification(
        id: Int,
        title: String,
        message: String,
        triggerTimeInMillis: Long,
        alertType: String,
        soundFileName: String?,
    ) {
        scheduledId = id
        scheduledTriggerTime = triggerTimeInMillis
    }

    override fun cancelNotification(id: Int) {
        canceledId = id
    }
}

class DailyDuaNotificationSchedulerTest {

    @Test
    fun testCancelWhenDisabled() = runTest {
        val settings = MapSettings()
        settings.putBoolean("daily_dua_enabled", false)
        val notificationManager = FakeNotificationManager()
        val scheduler = DailyDuaNotificationScheduler(FakeDuasRepository(), notificationManager, settings)

        scheduler.scheduleDailyNotification("Title")

        assertEquals(DailyDuaNotificationScheduler.NOTIFICATION_ID, notificationManager.canceledId)
        assertEquals(null, notificationManager.scheduledId)
    }

    @Test
    fun testScheduleFutureTimeToday() = runTest {
        val settings = MapSettings()
        settings.putBoolean("daily_dua_enabled", true)
        settings.putString("religion_id", "islam")

        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(timeZone)

        // Schedule for 1 hour from now
        val targetHour = (now.hour + 1) % 24

        // Only run if not near midnight to avoid complexity in test
        if (targetHour > now.hour) {
            settings.putInt("daily_dua_hour", targetHour)
            settings.putInt("daily_dua_minute", now.minute)

            val notificationManager = FakeNotificationManager()
            val scheduler = DailyDuaNotificationScheduler(FakeDuasRepository(), notificationManager, settings)

            scheduler.scheduleDailyNotification("Title")

            assertEquals(DailyDuaNotificationScheduler.NOTIFICATION_ID, notificationManager.scheduledId)
            assertTrue(notificationManager.scheduledTriggerTime != null)
            assertTrue(notificationManager.scheduledTriggerTime!! > Clock.System.now().toEpochMilliseconds())
        }
    }
}
