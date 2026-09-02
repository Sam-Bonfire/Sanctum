package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosWidgetDataProvider {
    fun writeDailyVerseToAppGroup(suiteName: String, verse: ScriptureVerse) {
        val userDefaults = NSUserDefaults(suiteName = suiteName)

        // We'll just encode the domain object directly or write the properties we need
        // For simplicity, let's just write the JSON string so iOS can parse it, or
        // we can store specific string keys.
        val jsonString = Json.encodeToString(
            mapOf(
                "id" to verse.id,
                "number" to verse.number.toString(),
                "translation" to verse.translation,
                "originalText" to verse.originalText,
            ),
        )

        userDefaults.setObject(jsonString, forKey = "daily_verse_data")
        userDefaults.synchronize()
    }
}
