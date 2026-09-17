package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
data class DuaLoop(
    val duaId: String,
    val startMs: Long = 0,
    val endMs: Long = 0,
    val repeatCount: Int = 0,
)

fun DuaLoop.isInfinite(): Boolean = repeatCount <= 0

fun DuaLoop.isValid(durationMs: Long): Boolean {
    if (duaId.isBlank() || durationMs <= 0) return false
    if (startMs < 0 || endMs <= startMs || endMs > durationMs) return false
    return repeatCount >= 0
}

fun DuaLoop.loopLengthMs(): Long = (endMs - startMs).coerceAtLeast(0)
