package com.sanctum.core.feature.scripture.domain.tajweed

import androidx.compose.ui.text.SpanStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TajweedParserTest {

    @Test
    fun testParse_TajweedDisabled() {
        val text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        val result = TajweedParser.parse(text, false)
        assertEquals(text, result.text)
        assertTrue(result.spanStyles.isEmpty())
    }

    @Test
    fun testParse_Qalqalah() {
        // قْ -> Qalqalah
        val text = "أَقْبَلَ"
        val result = TajweedParser.parse(text, true)
        assertEquals(text, result.text)

        // Find Qalqalah span
        val spans = result.spanStyles
        assertTrue(spans.isNotEmpty())

        // The قْ is at index 1 and 2 (length 2)
        // أ (1) ق (1) ْ (1) ب (1) ل (1) َ (1) -> Wait, arabic text is just standard string indices
        // أ = 0
        // ق = 1
        // ْ = 2
        // ب = 3
        // َ = 4
        // ل = 5
        // َ = 6
        // Result length 7

        val qalqalahSpan = spans.find { it.item.color == TajweedThemeColors.Light.qalqalah }
        assertTrue(qalqalahSpan != null)
        // أ (0623), َ (064E), ق (0642), ْ (0652), ب (0628), َ (064E), ل (0644), َ (064E)
        // ق is at index 2, ْ is at index 3
        assertEquals(2, qalqalahSpan.start)
        assertEquals(4, qalqalahSpan.end) // start is inclusive, end is exclusive
    }

    @Test
    fun testParse_Madd() {
        // Maddah (ٓ)
        val text = "جَآءَ" // ج 062c, َ 064e, ا 0627, ٓ 0653, ء 0621, َ 064e
        val result = TajweedParser.parse(text, true)
        assertEquals(text, result.text)

        val spans = result.spanStyles
        val maddSpan = spans.find { it.item.color == TajweedThemeColors.Light.madd }
        assertTrue(maddSpan != null)
        // ٓ is at index 3, but the grapheme cluster for ا is (ا, ٓ).
        // ا is at 2, ٓ is at 3
        assertEquals(2, maddSpan.start)
        assertEquals(4, maddSpan.end)
    }

    @Test
    fun testParse_Ghunnah() {
        // Noon Mushaddadah: نّ, but without extra diacritics in between to test the parser
        val text = "إِنّ" // إ 0625, ِ 0650, ن 0646, ّ 0651
        val result = TajweedParser.parse(text, true)
        assertEquals(text, result.text)

        val spans = result.spanStyles
        val ghunnahSpan = spans.find { it.item.color == TajweedThemeColors.Light.ghunnah }
        assertTrue(ghunnahSpan != null)
        // ن is at 2, ّ is at 3
        assertEquals(2, ghunnahSpan.start)
        assertEquals(4, ghunnahSpan.end)
    }

    @Test
    fun testParse_Ghunnah_with_Fatha() {
        // Noon Mushaddadah with Fatha: نَّ
        val text = "إِنَّ" // إ 0625, ِ 0650, ن 0646, َ 064e, ّ 0651  or ن 0646, ّ 0651, َ 064e
        val result = TajweedParser.parse(text, true)
        assertEquals(text, result.text)

        val spans = result.spanStyles
        val ghunnahSpan = spans.find { it.item.color == TajweedThemeColors.Light.ghunnah }
        assertTrue(ghunnahSpan != null)
        // ن is at 2, followed by multiple diacritics depending on the encoding
        // Either way, it should span from 2 to the end (5)
        assertEquals(2, ghunnahSpan.start)
        assertEquals(5, ghunnahSpan.end)
    }
}
