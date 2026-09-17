package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class InlineCommentary(
    val verseId: Int,
    val snippet: String,
    val explanation: String,
    val source: String = "",
)

fun InlineCommentary.isValid(): Boolean = verseId > 0 && snippet.isNotBlank() && explanation.isNotBlank()

fun commentariesFor(
    commentaries: List<InlineCommentary>,
    verseId: Int,
): List<InlineCommentary> = commentaries.filter { it.verseId == verseId }
