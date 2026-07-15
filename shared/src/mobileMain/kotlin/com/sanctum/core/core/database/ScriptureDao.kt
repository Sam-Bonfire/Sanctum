package com.sanctum.core.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptureDao {
    @Query("SELECT DISTINCT chapter_id FROM verses ORDER BY chapter_id ASC")
    fun getChapterIds(): Flow<List<Int>>

    @Query("SELECT * FROM verses WHERE chapter_id = :chapterId ORDER BY verse_number ASC")
    fun getVersesByChapter(chapterId: Int): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE id = :verseId")
    suspend fun getVerseById(verseId: Int): VerseEntity?

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Query("SELECT COUNT(*) FROM verses")
    suspend fun count(): Int
}
