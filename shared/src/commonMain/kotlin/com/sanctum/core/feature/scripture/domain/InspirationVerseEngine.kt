package com.sanctum.core.feature.scripture.domain

import kotlin.random.Random

enum class MoodTag {
    PEACE,
    COURAGE,
    GRATITUDE,
    STRENGTH,
    FORGIVENESS,
    HOPE,
}

class InspirationVerseEngine(
    private val random: Random = Random.Default,
) {
    // Basic implementation that provides mock verses filtered by tag.
    // In a real application, this might query a database.
    private val verseDatabase = listOf(
        Pair(MoodTag.PEACE, ScriptureVerse("p1", 1, "سلام", "Peace be upon you.", null, null)),
        Pair(MoodTag.COURAGE, ScriptureVerse("c1", 1, "Be strong", "Be strong and courageous.", null, null)),
        Pair(MoodTag.GRATITUDE, ScriptureVerse("g1", 1, "Thank you", "Give thanks in all circumstances.", null, null)),
        Pair(MoodTag.STRENGTH, ScriptureVerse("s1", 1, "Strength", "The Lord is my strength.", null, null)),
        Pair(MoodTag.FORGIVENESS, ScriptureVerse("f1", 1, "Forgive", "Forgive others as you have been forgiven.", null, null)),
        Pair(MoodTag.HOPE, ScriptureVerse("h1", 1, "Hope", "Hope anchors the soul.", null, null)),
        // Add more mock verses for better randomness
        Pair(MoodTag.PEACE, ScriptureVerse("p2", 2, "Shalom", "The Lord bless you with peace.", null, null)),
        Pair(MoodTag.COURAGE, ScriptureVerse("c2", 2, "Fear not", "Do not be afraid, I am with you.", null, null)),
    )

    fun getRandomVerse(tag: MoodTag? = null): ScriptureVerse {
        val filtered = if (tag != null) {
            verseDatabase.filter { it.first == tag }
        } else {
            verseDatabase
        }

        if (filtered.isEmpty()) {
            // Fallback just in case
            return verseDatabase.random(random).second
        }

        return filtered.random(random).second
    }
}
