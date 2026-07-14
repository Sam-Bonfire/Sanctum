package com.sanctum.core.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.sanctum.core.core.database.applicationContext

class AndroidPlatformNotificationManager : PlatformNotificationManager {

    private val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(id: Int, title: String, message: String, triggerTimeInMillis: Long) {
        val intent = Intent(applicationContext, PrayerAlarmReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", id)
            putExtra("NOTIFICATION_TITLE", title)
            putExtra("NOTIFICATION_MESSAGE", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // Schedule exact alarm
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
                } else {
                    // Fallback if permission is missing
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun cancelNotification(id: Int) {
        val intent = Intent(applicationContext, PrayerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(pendingIntent)
    }
}

actual fun getPlatformNotificationManager(): PlatformNotificationManager = AndroidPlatformNotificationManager()
