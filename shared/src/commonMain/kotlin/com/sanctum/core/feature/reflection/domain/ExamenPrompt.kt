package com.sanctum.core.feature.reflection.domain

import kotlinx.serialization.Serializable

@Serializable
data class ExamenPrompt(
    val step: Int,
    val title: String,
    val question: String,
)

val DAILY_EXAMEN = listOf(
    ExamenPrompt(1, "Gratitude", "For what am I most grateful today?"),
    ExamenPrompt(2, "Review", "Where did I feel closest to God today?"),
    ExamenPrompt(3, "Sorrow", "Where did I turn away today?"),
    ExamenPrompt(4, "Forgiveness", "What do I ask forgiveness for?"),
    ExamenPrompt(5, "Tomorrow", "How will I live differently tomorrow?"),
)

fun examenForDay(dayOfYear: Int): ExamenPrompt = DAILY_EXAMEN[dayOfYear.mod(DAILY_EXAMEN.size)]
