package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class Highlight(
    val id: Int = 0,
    val verseId: Int,
    val colorHex: String?,
    val timestampMs: Long,
)
