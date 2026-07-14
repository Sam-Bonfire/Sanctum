package com.sanctum.core.core.notifications

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

class IosPlatformNotificationManager : PlatformNotificationManager {

    override fun scheduleNotification(id: Int, title: String, message: String, triggerTimeInMillis: Long) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
        }

        val date = NSDate.dateWithTimeIntervalSince1970(triggerTimeInMillis / 1000.0)
        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            fromDate = date,
        )

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(components, repeats = false)
        val request = UNNotificationRequest.requestWithIdentifier(id.toString(), content, trigger)

        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Failed to schedule iOS notification: ${error.localizedDescription}")
            }
        }
    }

    override fun cancelNotification(id: Int) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removePendingNotificationRequestsWithIdentifiers(listOf(id.toString()))
    }
}

actual fun getPlatformNotificationManager(): PlatformNotificationManager = IosPlatformNotificationManager()
