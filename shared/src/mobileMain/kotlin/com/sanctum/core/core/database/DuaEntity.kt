package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duas")
data class DuaEntity(
    @PrimaryKey val id: String,
    val title: String,
    @ColumnInfo(name = "original_text") val originalText: String,
    @ColumnInfo(name = "translated_text") val translatedText: String,
    val transliteration: String?,
)
