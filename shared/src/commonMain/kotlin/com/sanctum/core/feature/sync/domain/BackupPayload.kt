package com.sanctum.core.feature.sync.domain

import com.sanctum.core.feature.scripture.domain.Bookmark
import kotlinx.serialization.Serializable

@Serializable
data class BackupPayload(
    val version: Int = 1,
    val lastSyncTimestampMs: Long,
    val bookmarks: List<Bookmark>,
)
