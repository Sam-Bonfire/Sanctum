package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
public data class AnonymousPrayer(
    val id: String,
    val requestText: String,
    val isAnonymous: Boolean,
    val name: String?,
    val contact: String?,
) {
    init {
        require(id.isNotBlank()) { "ID cannot be blank" }
        require(requestText.isNotBlank()) { "Request text cannot be blank" }
        if (isAnonymous) {
            require(name == null && contact == null) { "Anonymous prayers must not contain name or contact info" }
        }
    }

    public fun toggleAnonymous(anonymous: Boolean): AnonymousPrayer {
        return if (anonymous) {
            this.copy(
                isAnonymous = true,
                name = null,
                contact = null,
            )
        } else {
            this.copy(isAnonymous = false)
        }
    }
}
