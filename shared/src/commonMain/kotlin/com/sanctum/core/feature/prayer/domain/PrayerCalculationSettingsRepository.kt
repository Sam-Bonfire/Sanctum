package com.sanctum.core.feature.prayer.domain

import com.russhwolf.settings.Settings

class PrayerCalculationSettingsRepository(private val settings: Settings) {
    fun getAsrJuristicMethod(): AsrJuristicMethod {
        val methodStr = settings.getString("asr_juristic_method", AsrJuristicMethod.STANDARD_SHAFII.name)
        return try {
            AsrJuristicMethod.valueOf(methodStr)
        } catch (e: Exception) {
            AsrJuristicMethod.STANDARD_SHAFII
        }
    }

    fun saveAsrJuristicMethod(method: AsrJuristicMethod) {
        settings.putString("asr_juristic_method", method.name)
    }
}
