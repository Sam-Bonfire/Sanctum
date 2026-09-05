package com.sanctum.core.feature.scripture.domain.memorization

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerseMaskerTest {

    @Test
    fun testLevel0Read() {
        val text = "In the beginning, God created."
        val result = VerseMasker.maskVerse(text, MemorizationDifficulty.LEVEL_0_READ)

        val combined = result.joinToString("") { it.maskedWord }
        assertEquals(text, combined)
    }

    @Test
    fun testLevel1FirstLetter() {
        val text = "In the beginning, God created."
        val result = VerseMasker.maskVerse(text, MemorizationDifficulty.LEVEL_1_FIRST_LETTER)

        // words: In, the, beginning, God, created
        val words = result.filter { !it.isPunctuation }
        assertEquals("I_", words[0].maskedWord)
        assertEquals("t__", words[1].maskedWord)
        assertEquals("b________", words[2].maskedWord)
        assertEquals("G__", words[3].maskedWord)
        assertEquals("c______", words[4].maskedWord)

        val combined = result.joinToString("") { it.maskedWord }
        assertEquals("I_ t__ b________, G__ c______.", combined)
    }

    @Test
    fun testLevel2HalfBlank() {
        val text = "In the beginning, God created."
        val result = VerseMasker.maskVerse(text, MemorizationDifficulty.LEVEL_2_HALF_BLANK)

        // alternate blanks. First word (index 0) remains, second word (index 1) blanks, etc.
        val combined = result.joinToString("") { it.maskedWord }
        assertEquals("In ___ beginning, ___ created.", combined)
    }

    @Test
    fun testLevel3FullBlank() {
        val text = "In the beginning, God created."
        val result = VerseMasker.maskVerse(text, MemorizationDifficulty.LEVEL_3_FULL_BLANK)

        val combined = result.joinToString("") { it.maskedWord }
        assertEquals("__ ___ _________, ___ _______.", combined)
    }

    @Test
    fun testArabicTextLevel1() {
        val text = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ"
        val result = VerseMasker.maskVerse(text, MemorizationDifficulty.LEVEL_1_FIRST_LETTER)

        // Very basic checks on the first word for structure, length and start char
        val words = result.filter { !it.isPunctuation }
        assertTrue(words.isNotEmpty())
        assertTrue(words[0].maskedWord.startsWith("ب"))

        // Since Arabic has diacritics, length masking might vary slightly depending on exact regex,
        // but it should contain underscores.
        assertTrue(words[0].maskedWord.contains("_"))
    }
}
