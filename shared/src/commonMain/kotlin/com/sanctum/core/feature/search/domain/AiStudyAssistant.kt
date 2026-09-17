package com.sanctum.core.feature.search.domain

import kotlinx.serialization.Serializable

@Serializable
data class AiStudyRequest(
    val question: String,
    val verseContext: String = "",
    val flavorId: String = "",
)

@Serializable
data class AiStudyConfig(
    val endpoint: String = "",
    val model: String = "",
)

fun canUseAiAssistant(isPremium: Boolean, config: AiStudyConfig): Boolean =
    isPremium && config.endpoint.isNotBlank()

fun buildAiStudyPrompt(request: AiStudyRequest): String {
    require(request.question.isNotBlank()) { "Question must not be blank" }
    return buildString {
        if (request.flavorId.isNotBlank()) append("[${request.flavorId}] ")
        append(request.question.trim())
        if (request.verseContext.isNotBlank()) append("\nContext: ${request.verseContext.trim()}")
    }
}
