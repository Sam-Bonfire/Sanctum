package com.sanctum.core.feature.reflection.domain

import kotlinx.serialization.Serializable

@Serializable
data class MeditationSession(
    val totalMs: Long,
    val chimeIntervalMs: Long = 0,
    val elapsedMs: Long = 0,
)

fun MeditationSession.chimesDue(): Int {
    if (chimeIntervalMs <= 0 || elapsedMs <= 0) return 0
    return (elapsedMs / chimeIntervalMs).toInt()
}

fun MeditationSession.advance(nowElapsedMs: Long): MeditationSession =
    copy(elapsedMs = nowElapsedMs.coerceIn(0, totalMs))

fun MeditationSession.isComplete(): Boolean = elapsedMs >= totalMs
