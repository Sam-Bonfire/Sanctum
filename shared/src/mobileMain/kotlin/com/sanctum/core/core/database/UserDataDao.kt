package com.sanctum.core.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp_ms DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verse_id = :verseId")
    suspend fun removeBookmark(verseId: Int)

    @Transaction
    @Query("SELECT * FROM bookmarks ORDER BY timestamp_ms DESC")
    fun getBookmarksWithTags(): Flow<List<BookmarkWithTags>>

    // Tags
    @Query("SELECT * FROM bookmark_tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<BookmarkTagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: BookmarkTagEntity): Long

    @Query("UPDATE bookmark_tags SET name = :name WHERE id = :tagId")
    suspend fun renameTag(tagId: Int, name: String)

    @Query("DELETE FROM bookmark_tags WHERE id = :tagId")
    suspend fun deleteTag(tagId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun assignTag(crossRef: BookmarkTagCrossRef)

    @Query("DELETE FROM bookmark_tag_cross_ref WHERE verse_id = :verseId AND tag_id = :tagId")
    suspend fun unassignTag(verseId: Int, tagId: Int)

    // Notes
    @Query("SELECT * FROM notes ORDER BY timestamp_ms DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Query("DELETE FROM notes WHERE verse_id = :verseId")
    suspend fun removeNote(verseId: Int)

    // Highlights
    @Query("SELECT * FROM highlights ORDER BY timestamp_ms DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHighlight(highlight: HighlightEntity)

    @Query("SELECT * FROM highlights WHERE id = :id")
    suspend fun getHighlightById(id: Int): HighlightEntity?

    @Query("DELETE FROM highlights WHERE verse_id = :verseId")
    suspend fun removeHighlight(verseId: Int)
}
