package com.sanctum.core.feature.reflection.domain

import kotlin.math.roundToInt
import kotlinx.serialization.Serializable

@Serializable
data class PratikramanGuide(
    val id: String,
    val name: String,
    val meaning: String,
    val steps: List<String> = emptyList(),
    val imageKey: String = "",
    val audioKey: String = "",
    val currentStepIndex: Int = 0,
)

fun PratikramanGuide.isComplete(): Boolean = steps.isNotEmpty() && currentStepIndex >= steps.size - 1

fun PratikramanGuide.advance(): PratikramanGuide =
    if (steps.isEmpty() || currentStepIndex >= steps.size - 1) this else copy(currentStepIndex = currentStepIndex + 1)

fun PratikramanGuide.progressPercentage(): Int =
    if (steps.isEmpty()) {
        0
    } else if (isComplete()) {
        100
    } else {
        ((currentStepIndex.toDouble() / (steps.size - 1)) * 100).roundToInt()
    }
