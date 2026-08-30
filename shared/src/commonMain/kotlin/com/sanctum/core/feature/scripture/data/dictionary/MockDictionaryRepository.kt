package com.sanctum.core.feature.scripture.data.dictionary

import com.sanctum.core.feature.scripture.domain.dictionary.DictionaryRepository
import com.sanctum.core.feature.scripture.domain.dictionary.DictionaryTerm

class MockDictionaryRepository : DictionaryRepository {
    private val terms = listOf(
        DictionaryTerm(
            word = "Selah",
            definition = "A musical or liturgical instruction, often meant as a pause or to praise.",
            root = "s-l-h",
            transliteration = "selah",
            etymology = "Hebrew",
        ),
        DictionaryTerm(
            word = "Amen",
            definition = "So be it; truly.",
            root = "a-m-n",
            transliteration = "amen",
            etymology = "Hebrew",
        ),
        DictionaryTerm(
            word = "Hallelujah",
            definition = "Praise ye Yah (the Lord).",
            root = "h-l-l",
            transliteration = "hallelujah",
            etymology = "Hebrew",
        ),
        DictionaryTerm(
            word = "Taqwa",
            definition = "Consciousness and fear of God; piety.",
            root = "w-q-y",
            transliteration = "taqwa",
            etymology = "Arabic",
        ),
        DictionaryTerm(
            word = "Nirvana",
            definition = "The state of profound peace of mind that is acquired with moksha.",
            root = "nir-va",
            transliteration = "nirvana",
            etymology = "Sanskrit",
        ),
        DictionaryTerm(
            word = "Dharma",
            definition = "Righteousness, law, duty, moral teachings.",
            root = "dhri",
            transliteration = "dharma",
            etymology = "Sanskrit",
        ),
    )

    override fun lookupTerm(word: String): DictionaryTerm? {
        val cleanWord = word.trim().lowercase().replace(Regex("[^a-z]"), "")
        return terms.find { it.word.lowercase() == cleanWord }
    }

    override fun getAllTerms(): List<DictionaryTerm> {
        return terms
    }
}
