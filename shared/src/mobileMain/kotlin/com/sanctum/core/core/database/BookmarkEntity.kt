package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Read-write table storing the user's saved verses and timestamps.
 * This is the table that will be backed up to Google Drive/iCloud (BYOC).
 */
@Serializable
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "verse_id") val verseId: Int,
    @ColumnInfo(name = "timestamp_ms") val timestampMs: Long,
)
