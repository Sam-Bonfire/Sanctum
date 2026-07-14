package com.sanctum.core.feature.sync.data

import com.sanctum.core.core.database.BookmarkEntity
import com.sanctum.core.core.database.UserDataDao
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.sync.domain.BackupPayload
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataExporter(private val userDataDao: UserDataDao) {

    suspend fun exportDataToJson(): String {
        // Collects the most recent state of the bookmarks from Room
        val bookmarks = userDataDao.getAllBookmarks().first()
        val payload = BackupPayload(
            version = 1,
            // Mock timestamp
            lastSyncTimestampMs = 123456789L,
            bookmarks = bookmarks.map { Bookmark(it.id, it.verseId, it.timestampMs) },
        )
        return Json.encodeToString(payload)
    }

    suspend fun importDataFromJson(jsonString: String) {
        val payload = Json.decodeFromString<BackupPayload>(jsonString)
        payload.bookmarks.forEach {
            // Restore from cloud
            userDataDao.addBookmark(BookmarkEntity(it.id, it.verseId, it.timestampMs))
        }
    }
}
