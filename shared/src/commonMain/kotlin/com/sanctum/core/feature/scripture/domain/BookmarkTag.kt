package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkTag(
    val id: Int = 0,
    val name: String,
    val colorHex: String,
)
