package com.sanctum.core.feature.scripture.domain.tajweed

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object TajweedParser {

    // Extracted diacritic checker
    private fun isDiacritic(c: Char): Boolean {
        // Arabic diacritics range from U+064B to U+065F, and U+0670
        return c in '\u064B'..'\u065F' || c == '\u0670'
    }

    fun parse(text: String, isTajweedEnabled: Boolean, isDarkTheme: Boolean = false): AnnotatedString {
        if (!isTajweedEnabled) return AnnotatedString(text)

        val colors = if (isDarkTheme) TajweedThemeColors.Dark else TajweedThemeColors.Light

        return buildAnnotatedString {
            var i = 0
            while (i < text.length) {
                val c = text[i]

                // Read ahead to collect all following diacritics
                var j = i + 1
                while (j < text.length && isDiacritic(text[j])) {
                    j++
                }
                val graphemeCluster = text.substring(i, j)

                // Check for rules in the grapheme cluster
                // Qalqalah: Letter + Sukun (ْ - U+0652)
                if (c in listOf('\u0642', '\u0637', '\u0628', '\u062C', '\u062F') && graphemeCluster.contains('\u0652')) {
                    withStyle(SpanStyle(color = colors.qalqalah)) {
                        append(graphemeCluster)
                    }
                }
                // Ghunnah: Noon (ن - U+0646) or Meem (م - U+0645) + Shadda (ّ - U+0651)
                else if ((c == '\u0646' || c == '\u0645') && graphemeCluster.contains('\u0651')) {
                    withStyle(SpanStyle(color = colors.ghunnah)) {
                        append(graphemeCluster)
                    }
                }
                // Madd: Maddah (ٓ - U+0653) in the cluster or if the base char is Maddah (though usually it's a diacritic)
                else if (c == '\u0653' || graphemeCluster.contains('\u0653')) {
                    withStyle(SpanStyle(color = colors.madd)) {
                        append(graphemeCluster)
                    }
                } else {
                    append(graphemeCluster)
                }

                i = j
            }
        }
    }
}
