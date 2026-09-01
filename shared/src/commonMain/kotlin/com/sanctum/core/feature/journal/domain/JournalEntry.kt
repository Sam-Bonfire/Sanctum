package com.sanctum.core.feature.journal.domain

import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    val id: Int,
    val verseId: Int?,
    val chapterId: Int?,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val moodTags: List<String>,
)
