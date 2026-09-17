package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
data class CrossfadeConfig(
    val durationMs: Long = 3_000,
)

fun CrossfadeConfig.isValid(): Boolean = durationMs in 500..12_000

fun outgoingVolume(elapsedMs: Long, config: CrossfadeConfig): Float {
    if (!config.isValid() || elapsedMs <= 0) return 1f
    if (elapsedMs >= config.durationMs) return 0f
    return 1f - (elapsedMs.toFloat() / config.durationMs)
}

fun incomingVolume(elapsedMs: Long, config: CrossfadeConfig): Float {
    if (!config.isValid() || elapsedMs <= 0) return 0f
    if (elapsedMs >= config.durationMs) return 1f
    return elapsedMs.toFloat() / config.durationMs
}
