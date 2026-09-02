package com.sanctum.core.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "bookmark_tag_cross_ref",
    primaryKeys = ["verse_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = BookmarkTagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("tag_id")],
)
data class BookmarkTagCrossRef(
    @ColumnInfo(name = "verse_id") val verseId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int,
)
