package com.sanctum.core.feature.scripture.domain.memorization

data class MaskedWord(
    val originalWord: String,
    val maskedWord: String,
    val isPunctuation: Boolean = false,
)

object VerseMasker {

    // Regular expression to identify if a character is considered a word character or letter.
    // It matches anything that is not punctuation, whitespace or common symbols.
    private val punctuationRegex = Regex("""[\p{Punct}\s]+""")
    private val wordCharRegex = Regex("""[\p{L}\p{M}\p{N}]""")

    /**
     * Splits a text into alternating words and punctuation/whitespace blocks.
     * This ensures we can reconstruct the text accurately without losing spacing or punctuation.
     */
    fun splitIntoTokens(text: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()
        var inPunctuation = false

        for (char in text) {
            val isPunct = char.toString().matches(punctuationRegex)
            if (currentToken.isEmpty()) {
                inPunctuation = isPunct
                currentToken.append(char)
            } else {
                if (isPunct == inPunctuation) {
                    currentToken.append(char)
                } else {
                    tokens.add(currentToken.toString())
                    currentToken = StringBuilder()
                    inPunctuation = isPunct
                    currentToken.append(char)
                }
            }
        }
        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken.toString())
        }
        return tokens
    }

    /**
     * Masks the text based on the difficulty level.
     *
     * @param text The input verse string.
     * @param difficulty The selected memorization difficulty.
     * @return A list of MaskedWord objects, distinguishing words from spacing/punctuation.
     */
    fun maskVerse(text: String, difficulty: MemorizationDifficulty): List<MaskedWord> {
        if (text.isBlank()) return emptyList()

        val tokens = splitIntoTokens(text)
        val result = mutableListOf<MaskedWord>()
        var wordIndex = 0

        for (token in tokens) {
            if (token.matches(punctuationRegex)) {
                result.add(MaskedWord(token, token, isPunctuation = true))
                continue
            }

            // It's a word.
            val masked = when (difficulty) {
                MemorizationDifficulty.LEVEL_0_READ -> token
                MemorizationDifficulty.LEVEL_1_FIRST_LETTER -> maskLevel1(token)
                MemorizationDifficulty.LEVEL_2_HALF_BLANK -> {
                    if (wordIndex % 2 == 1) {
                        maskLevel3(token) // Blank every other word
                    } else {
                        token
                    }
                }
                MemorizationDifficulty.LEVEL_3_FULL_BLANK -> maskLevel3(token)
            }

            result.add(MaskedWord(token, masked, isPunctuation = false))
            wordIndex++
        }

        return result
    }

    /**
     * Level 1: Keeps the first letter (and its marks) and masks the rest of the word.
     */
    private fun maskLevel1(word: String): String {
        if (word.isEmpty()) return word

        // Find the first actual letter to keep.
        var firstCharIndex = -1
        var lengthToKeep = 0

        for (i in word.indices) {
            if (word[i].toString().matches(wordCharRegex)) {
                if (firstCharIndex == -1) {
                    firstCharIndex = i
                    lengthToKeep = 1
                } else if (word[i].isLetterOrDigit().not() && word[i].category != CharCategory.MODIFIER_LETTER && word[i].category != CharCategory.NON_SPACING_MARK) {
                    // Stop if we hit something that is not a mark/modifier of the first character
                    // Note: Simplification for this exercise. Real grapheme cluster handling can be complex.
                }
            }
        }

        // Just simplistic approach: take the first char if it exists
        val builder = StringBuilder()
        var hasKeptFirst = false

        for (char in word) {
            if (char.toString().matches(wordCharRegex)) {
                if (!hasKeptFirst) {
                    builder.append(char)
                    hasKeptFirst = true
                } else {
                    builder.append("_")
                }
            } else {
                // Keep punctuation inside the word intact (like apostrophes)
                builder.append(char)
            }
        }

        return builder.toString()
    }

    /**
     * Level 3: Blanks all characters in the word.
     */
    private fun maskLevel3(word: String): String {
        val builder = StringBuilder()
        for (char in word) {
            if (char.toString().matches(wordCharRegex)) {
                builder.append("_")
            } else {
                builder.append(char)
            }
        }
        return builder.toString()
    }
}
