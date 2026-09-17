package com.sanctum.core.feature.rosary.domain

import kotlinx.serialization.Serializable

@Serializable
data class MantraLoop(
    val mantra: String,
    val repetitions: Int,
    val completed: Int = 0,
)

fun MantraLoop.isComplete(): Boolean = completed >= repetitions

fun MantraLoop.chant(): MantraLoop {
    require(repetitions > 0) { "Repetitions must be positive" }
    if (isComplete()) return this
    return copy(completed = completed + 1)
}

fun MantraLoop.remaining(): Int = (repetitions - completed).coerceAtLeast(0)
