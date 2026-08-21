package com.sanctum.core.core.notifications

/**
 * Cross-platform manager for scheduling local push notifications.
 * Used for offline prayer alerts.
 */
interface PlatformNotificationManager {
    /**
     * Schedules a local push notification.
     * @param id Unique identifier for the notification to allow cancelling/updating.
     * @param title The title of the notification (e.g. "Time to Pray").
     * @param message The body of the notification (e.g. "It is now time for Maghrib").
     * @param triggerTimeInMillis The epoch time in milliseconds when the notification should fire.
     * @param alertType The type of alert to trigger.
     * @param soundFileName The filename of the custom sound to play, if applicable.
     */
    fun scheduleNotification(
        id: Int,
        title: String,
        message: String,
        triggerTimeInMillis: Long,
        alertType: String = "AUDIO",
        soundFileName: String? = null,
    )

    /**
     * Cancels a previously scheduled notification.
     */
    fun cancelNotification(id: Int)
}

expect fun getPlatformNotificationManager(): PlatformNotificationManager
