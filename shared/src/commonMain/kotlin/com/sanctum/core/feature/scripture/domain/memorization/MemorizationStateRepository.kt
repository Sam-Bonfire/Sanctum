package com.sanctum.core.feature.scripture.domain.memorization

import com.russhwolf.settings.Settings

class MemorizationStateRepository(private val settings: Settings) {

    private val prefix = "memo_verse_"

    /**
     * Gets the mastery level of a verse by its ID.
     * Returns MemorizationDifficulty.LEVEL_0_READ if not found.
     */
    fun getMasteryLevel(verseId: String): MemorizationDifficulty {
        val level = settings.getInt(prefix + verseId, 0)
        return when (level) {
            1 -> MemorizationDifficulty.LEVEL_1_FIRST_LETTER
            2 -> MemorizationDifficulty.LEVEL_2_HALF_BLANK
            3 -> MemorizationDifficulty.LEVEL_3_FULL_BLANK
            else -> MemorizationDifficulty.LEVEL_0_READ
        }
    }

    /**
     * Sets the mastery level of a verse by its ID.
     */
    fun setMasteryLevel(verseId: String, difficulty: MemorizationDifficulty) {
        val level = when (difficulty) {
            MemorizationDifficulty.LEVEL_0_READ -> 0
            MemorizationDifficulty.LEVEL_1_FIRST_LETTER -> 1
            MemorizationDifficulty.LEVEL_2_HALF_BLANK -> 2
            MemorizationDifficulty.LEVEL_3_FULL_BLANK -> 3
        }
        settings.putInt(prefix + verseId, level)
    }

    /**
     * Toggles whether the user considers a verse "Mastered" completely.
     * We'll map "Mastered" to having reached LEVEL_3_FULL_BLANK.
     */
    fun toggleMastered(verseId: String) {
        val current = getMasteryLevel(verseId)
        if (current == MemorizationDifficulty.LEVEL_3_FULL_BLANK) {
            setMasteryLevel(verseId, MemorizationDifficulty.LEVEL_0_READ)
        } else {
            setMasteryLevel(verseId, MemorizationDifficulty.LEVEL_3_FULL_BLANK)
        }
    }

    /**
     * Checks if a verse is considered "Mastered".
     */
    fun isMastered(verseId: String): Boolean {
        return getMasteryLevel(verseId) == MemorizationDifficulty.LEVEL_3_FULL_BLANK
    }
}
