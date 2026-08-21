package com.sanctum.core.feature.prayer.domain

enum class NotificationAlertType {
    SILENT,
    VIBRATE,
    AUDIO,
}

enum class MuezzinVoice(val displayName: String, val fileName: String) {
    MECCA("Mecca", "adhan_mecca.mp3"),
    MEDINA("Medina", "adhan_medina.mp3"),
    AL_AQSA("Al-Aqsa", "adhan_al_aqsa.mp3"),
    TRADITIONAL("Traditional", "adhan_traditional.mp3"),
}

data class PrayerNotificationSetting(
    val prayerName: String,
    val alertType: NotificationAlertType = NotificationAlertType.AUDIO,
    val muezzinVoice: MuezzinVoice = MuezzinVoice.MECCA,
)
