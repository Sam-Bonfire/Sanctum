package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "verse_id") val verseId: Int?,
    @ColumnInfo(name = "chapter_id") val chapterId: Int?,
    val title: String,
    val content: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    // Comma separated for simplicity in Room
    @ColumnInfo(name = "mood_tags") val moodTags: String,
)
