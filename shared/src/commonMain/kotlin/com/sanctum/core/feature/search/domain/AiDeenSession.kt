package com.sanctum.core.feature.search.domain

import kotlinx.serialization.Serializable

@Serializable
data class AiDeenTurn(
    val question: String,
    val answer: String = "",
)

@Serializable
data class AiDeenSession(
    val turns: List<AiDeenTurn> = emptyList(),
    val maxTurns: Int = 20,
)

fun AiDeenSession.ask(question: String): AiDeenSession {
    require(question.isNotBlank()) { "Question must not be blank" }
    require(turns.size < maxTurns) { "Session is full" }
    return copy(turns = turns + AiDeenTurn(question.trim()))
}

fun AiDeenSession.answer(answer: String): AiDeenSession {
    require(turns.isNotEmpty() && turns.last().answer.isEmpty()) { "No pending question" }
    return copy(turns = turns.dropLast(1) + turns.last().copy(answer = answer.trim()))
}
