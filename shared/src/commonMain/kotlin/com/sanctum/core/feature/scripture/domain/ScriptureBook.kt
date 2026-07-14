package com.sanctum.core.feature.scripture.domain

/**
 * A highly agnostic representation of religious scripture.
 * This structure supports:
 * - Islam (Surah -> Ayah)
 * - Judaism (Torah -> Parsha -> Pasuk)
 * - Christianity (Book -> Chapter -> Verse)
 * - Hinduism (Mandala -> Sukta -> Mantra)
 */
data class ScriptureBook(
    val id: String,
    // e.g. "Al-Baqarah", "Genesis", "Rigveda"
    val title: String,
    // e.g. "The Cow", "Bereshit"
    val subtitle: String?,
    val chapters: List<ScriptureChapter>,
)
