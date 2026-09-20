package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class BulletinPost(
    val title: String,
    val body: String,
    val venue: String,
    val expiresAt: Long,
) {
    init {
        require(title.isNotBlank()) { "Title cannot be blank" }
        require(body.isNotBlank()) { "Body cannot be blank" }
        require(venue.isNotBlank()) { "Venue cannot be blank" }
        require(expiresAt > 0) { "Expiration timestamp must be positive" }
    }

    fun isActive(currentTimeMs: Long): Boolean {
        return expiresAt > currentTimeMs
    }
}
