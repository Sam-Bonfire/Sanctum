package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    val id: Int = 0,
    val verseId: Int,
    val timestampMs: Long,
)
