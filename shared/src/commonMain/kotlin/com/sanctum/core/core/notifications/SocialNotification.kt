package com.sanctum.core.core.notifications

import kotlinx.serialization.Serializable

@Serializable
enum class SocialEvent {
    LIKE,
    COMMENT,
}

@Serializable
data class SocialNotification(
    val event: SocialEvent,
    val actorName: String,
    val targetTitle: String,
)

fun SocialNotification.title(): String =
    when (event) {
        SocialEvent.LIKE -> "$actorName liked your post"
        SocialEvent.COMMENT -> "$actorName commented on your post"
    }

fun SocialNotification.body(): String = targetTitle.take(120)

fun SocialNotification.isValid(): Boolean = actorName.isNotBlank() && targetTitle.isNotBlank()
