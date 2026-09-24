package com.sanctum.core.feature.duas.domain

data class Dua(
    val id: String,
    val title: String,
    val originalText: String,
    val translation: String,
    val transliteration: String? = null,
)
