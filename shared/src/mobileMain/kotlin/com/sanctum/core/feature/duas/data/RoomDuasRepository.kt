package com.sanctum.core.feature.duas.data

import com.sanctum.core.core.database.PrayerDatabase
import com.sanctum.core.feature.duas.domain.Dua

class RoomDuasRepository(
    private val database: PrayerDatabase,
) : DuasRepository {
    override suspend fun getDuas(religionId: String): List<Dua> {
        return try {
            database.duasDao().getAllDuas().map { entity ->
                Dua(
                    id = entity.id,
                    title = entity.title,
                    originalText = entity.originalText,
                    translation = entity.translatedText,
                    transliteration = entity.transliteration,
                )
            }
        } catch (e: Exception) {
            println("RoomDuasRepository: failed to load duas for '$religionId': ${e.message}")
            emptyList()
        }
    }
}
