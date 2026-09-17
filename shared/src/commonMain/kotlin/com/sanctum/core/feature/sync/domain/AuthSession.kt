package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class AuthSession(
    val userId: String,
    val email: String,
    val provider: String = "supabase",
    val token: String = "",
)

fun AuthSession.isSignedIn(): Boolean = userId.isNotBlank() && token.isNotBlank()

fun AuthSession.signOut(): AuthSession = copy(userId = "", token = "")

fun isValidEmail(email: String): Boolean {
    val trimmed = email.trim()
    return trimmed.contains("@") && trimmed.substringAfter("@").contains(".")
}
