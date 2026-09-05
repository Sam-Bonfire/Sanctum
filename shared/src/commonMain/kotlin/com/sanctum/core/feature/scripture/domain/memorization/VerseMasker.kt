package com.sanctum.core.feature.scripture.domain.memorization

data class MaskedWord(
    val originalWord: String,
    val maskedWord: String,
    val isPunctuation: Boolean = false,
)

object VerseMasker {

    // Regular expression to identify if a character is considered a word character or letter.
    // It matches anything that is not punctuation, whitespace or common symbols.
    // We use a simpler regex that works across platforms (especially JS/WASM which has varying support for unicode properties in regex).
    // \w usually covers a-zA-Z0-9_, so we just check for basic letters/numbers for simple cases and rely on character categorization for complex ones.

    private fun isPunctuationOrSpace(char: Char): Boolean {
        return char.isWhitespace() || char.category == CharCategory.DASH_PUNCTUATION ||
            char.category == CharCategory.START_PUNCTUATION || char.category == CharCategory.END_PUNCTUATION ||
            char.category == CharCategory.CONNECTOR_PUNCTUATION || char.category == CharCategory.OTHER_PUNCTUATION ||
            char.category == CharCategory.INITIAL_QUOTE_PUNCTUATION || char.category == CharCategory.FINAL_QUOTE_PUNCTUATION
    }

    private fun isWordCharacter(char: Char): Boolean {
        return char.isLetterOrDigit() || char.category == CharCategory.NON_SPACING_MARK ||
            char.category == CharCategory.COMBINING_SPACING_MARK || char.category == CharCategory.ENCLOSING_MARK ||
            char.category == CharCategory.MODIFIER_LETTER || char.category == CharCategory.MODIFIER_SYMBOL
    }

    /**
     * Splits a text into alternating words and punctuation/whitespace blocks.
     * This ensures we can reconstruct the text accurately without losing spacing or punctuation.
     */
    fun splitIntoTokens(text: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()
        var inPunctuation = false

        for (char in text) {
            val isPunct = isPunctuationOrSpace(char)
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
            if (token.isNotEmpty() && isPunctuationOrSpace(token[0])) {
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

        val builder = StringBuilder()
        var hasKeptFirstBaseLetter = false

        for (char in word) {
            if (isWordCharacter(char)) {
                // If it is a base letter, mark it kept if we haven't already.
                // If it is a modifier/mark, keep it if we are keeping the current letter.
                val isMark = char.category == CharCategory.NON_SPACING_MARK ||
                    char.category == CharCategory.COMBINING_SPACING_MARK ||
                    char.category == CharCategory.ENCLOSING_MARK ||
                    char.category == CharCategory.MODIFIER_LETTER ||
                    char.category == CharCategory.MODIFIER_SYMBOL

                if (!hasKeptFirstBaseLetter) {
                    builder.append(char)
                    if (!isMark) {
                        hasKeptFirstBaseLetter = true
                    }
                } else {
                    if (isMark) {
                        // It's a mark for the first base letter, keep it
                        builder.append(char)
                    } else {
                        // This is a new base letter or number, mask it
                        builder.append("_")
                    }
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
            if (isWordCharacter(char)) {
                builder.append("_")
            } else {
                builder.append(char)
            }
        }
        return builder.toString()
    }
}
