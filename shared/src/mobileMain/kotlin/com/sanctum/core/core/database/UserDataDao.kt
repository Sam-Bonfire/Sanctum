package com.sanctum.core.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp_ms DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verse_id = :verseId")
    suspend fun removeBookmark(verseId: Int)
}
