package com.sanctum.core.core.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookmarkWithTags(
    @Embedded val bookmark: BookmarkEntity,
    @Relation(
        parentColumn = "verse_id",
        entityColumn = "id",
        associateBy = Junction(
            value = BookmarkTagCrossRef::class,
            parentColumn = "verse_id",
            entityColumn = "tag_id",
        ),
    )
    val tags: List<BookmarkTagEntity>,
)
