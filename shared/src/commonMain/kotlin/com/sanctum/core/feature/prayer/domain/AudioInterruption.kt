package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
enum class InterruptionType {
    PHONE_CALL,
    NAVIGATION,
    NOTIFICATION,
    OTHER,
}

@Serializable
enum class InterruptionAction {
    PAUSE,
    DUCK,
    STOP,
}

@Serializable
data class AudioInterruption(
    val type: InterruptionType,
    val canResume: Boolean = true,
)

fun resolveInterruption(interruption: AudioInterruption): InterruptionAction =
    when (interruption.type) {
        InterruptionType.PHONE_CALL -> InterruptionAction.PAUSE
        InterruptionType.NAVIGATION -> InterruptionAction.DUCK
        InterruptionType.NOTIFICATION -> InterruptionAction.DUCK
        InterruptionType.OTHER -> if (interruption.canResume) InterruptionAction.PAUSE else InterruptionAction.STOP
    }
