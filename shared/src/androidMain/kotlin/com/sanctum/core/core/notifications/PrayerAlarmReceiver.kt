package com.sanctum.core.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("NOTIFICATION_ID", 0)
        val title = intent.getStringExtra("NOTIFICATION_TITLE") ?: "Prayer Time"
        val message = intent.getStringExtra("NOTIFICATION_MESSAGE") ?: "It is time to pray."
        val alertType = intent.getStringExtra("ALERT_TYPE") ?: "AUDIO"
        val soundFileName = intent.getStringExtra("SOUND_FILE_NAME")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "prayer_channel",
                "Prayer Alerts",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Notifies you when it is time to pray"
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (alertType == "AUDIO" && soundFileName != null) {
            val serviceIntent = Intent(context, AdhanPlaybackService::class.java).apply {
                putExtra("NOTIFICATION_TITLE", title)
                putExtra("NOTIFICATION_MESSAGE", message)
                putExtra("SOUND_FILE_NAME", soundFileName)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } else {
            val notificationBuilder = NotificationCompat.Builder(context, "prayer_channel")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            if (alertType == "VIBRATE") {
                notificationBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (androidx.core.content.ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS,
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    notificationManager.notify(id, notificationBuilder.build())
                }
            } else {
                notificationManager.notify(id, notificationBuilder.build())
            }
        }
    }
}
