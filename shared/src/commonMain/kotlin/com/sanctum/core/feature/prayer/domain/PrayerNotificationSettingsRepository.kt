package com.sanctum.core.feature.prayer.domain

import com.russhwolf.settings.Settings

class PrayerNotificationSettingsRepository(private val settings: Settings) {

    fun getSetting(prayerName: String): PrayerNotificationSetting {
        val alertTypeStr = settings.getString("prayer_${prayerName}_alert_type", NotificationAlertType.AUDIO.name)
        val muezzinVoiceStr = settings.getString("prayer_${prayerName}_muezzin_voice", MuezzinVoice.MECCA.name)

        val alertType = try {
            NotificationAlertType.valueOf(alertTypeStr)
        } catch (e: Exception) {
            NotificationAlertType.AUDIO
        }

        val muezzinVoice = try {
            MuezzinVoice.valueOf(muezzinVoiceStr)
        } catch (e: Exception) {
            MuezzinVoice.MECCA
        }

        return PrayerNotificationSetting(prayerName, alertType, muezzinVoice)
    }

    fun saveSetting(setting: PrayerNotificationSetting) {
        settings.putString("prayer_${setting.prayerName}_alert_type", setting.alertType.name)
        settings.putString("prayer_${setting.prayerName}_muezzin_voice", setting.muezzinVoice.name)
    }
}
