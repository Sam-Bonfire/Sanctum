package com.sanctum.core.feature.scripture.domain

import kotlinx.serialization.Serializable

@Serializable
data class ScriptureVerse(
    val id: String,
    val number: Int,
    // e.g. Arabic, Hebrew, Sanskrit
    val originalText: String,
    // e.g. English translation
    val translation: String,
    // For pronunciation assistance
    val transliteration: String? = null,
    val historicalContext: com.sanctum.core.feature.scripture.domain.history.HistoricalContext? = null,
)
