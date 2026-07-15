package com.sanctum.core.core.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.sanctum.core.core.database.applicationContext

actual fun createSettings(): Settings {
    val sharedPrefs = applicationContext.getSharedPreferences("sanctum_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
}
