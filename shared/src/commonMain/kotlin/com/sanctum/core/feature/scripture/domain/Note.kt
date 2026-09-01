package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int = 0,
    val verseId: Int,
    val content: String,
    val timestampMs: Long,
)
