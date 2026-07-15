package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Pre-bundled read-only table storing all religious verses/scripture.
 * This is populated during the CI/CD build and updated via Cloudflare R2 OTA updates.
 */
@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "chapter_id") val chapterId: Int,
    @ColumnInfo(name = "verse_number") val verseNumber: Int,
    @ColumnInfo(name = "original_text") val originalText: String,
    @ColumnInfo(name = "translated_text") val translatedText: String,
)
