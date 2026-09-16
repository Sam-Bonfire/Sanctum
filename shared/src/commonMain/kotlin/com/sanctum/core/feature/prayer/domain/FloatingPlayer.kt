package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
data class FloatingPlayerState(
    val trackTitle: String = "",
    val isPlaying: Boolean = false,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val isVisible: Boolean = false,
)

fun FloatingPlayerState.progress(): Float {
    if (durationMs <= 0) return 0f
    return (positionMs.toFloat() / durationMs).coerceIn(0f, 1f)
}

fun FloatingPlayerState.toggle(): FloatingPlayerState = copy(isPlaying = !isPlaying)

fun FloatingPlayerState.seekTo(position: Long): FloatingPlayerState =
    copy(positionMs = position.coerceIn(0, durationMs))
