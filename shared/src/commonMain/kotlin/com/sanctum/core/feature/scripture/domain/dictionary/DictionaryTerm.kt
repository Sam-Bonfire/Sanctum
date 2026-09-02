package com.sanctum.core.feature.scripture.domain.dictionary

data class DictionaryTerm(
    val word: String,
    val definition: String,
    val root: String? = null,
    val transliteration: String? = null,
    val etymology: String? = null,
)
