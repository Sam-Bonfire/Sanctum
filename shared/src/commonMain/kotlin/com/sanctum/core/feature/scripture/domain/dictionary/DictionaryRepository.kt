package com.sanctum.core.feature.scripture.domain.dictionary

interface DictionaryRepository {
    fun lookupTerm(word: String): DictionaryTerm?
    fun getAllTerms(): List<DictionaryTerm>
}
