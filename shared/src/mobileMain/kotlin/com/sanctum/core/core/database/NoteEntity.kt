package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "verse_id") val verseId: Int,
    val content: String,
    @ColumnInfo(name = "timestamp_ms") val timestampMs: Long,
)
