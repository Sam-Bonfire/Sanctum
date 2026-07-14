package com.sanctum.core.feature.scripture.domain

data class ScriptureChapter(
    val id: String,
    val number: Int,
    // e.g. optional chapter names
    val title: String?,
    val verses: List<ScriptureVerse>,
)
