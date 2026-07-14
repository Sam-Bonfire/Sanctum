package com.sanctum.core.core.notifications

actual fun getPlatformNotificationManager(): PlatformNotificationManager {
    return object : PlatformNotificationManager {
        override fun scheduleNotification(id: Int, title: String, message: String, triggerTimeInMillis: Long) {
            // Web Notifications API could be hooked up here
            println("Wasm Notification Scheduled: $title - $message at $triggerTimeInMillis")
        }

        override fun cancelNotification(id: Int) {
            // No-op for web
        }
    }
}
