package com.sanctum.core.feature.scripture.domain

data class Verse(
    val id: Int,
    val surahId: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val translatedText: String,
)
