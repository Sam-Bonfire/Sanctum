package com.sanctum.core.feature.scripture.domain

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InspirationVerseEngineTest {
    @Test
    fun testGetRandomVerseWithoutTag() {
        val engine = InspirationVerseEngine()
        val verse = engine.getRandomVerse()
        assertNotNull(verse)
    }

    @Test
    fun testGetRandomVerseWithTag() {
        val engine = InspirationVerseEngine()
        val verse = engine.getRandomVerse(MoodTag.PEACE)
        assertNotNull(verse)
        // Since we know the mock data, PEACE verses start with 'p'
        assertTrue(verse.id.startsWith("p"))
    }

    @Test
    fun testRandomnessDistribution() {
        // We'll use a fixed seed for deterministic testing
        val engine = InspirationVerseEngine(Random(42))

        val counts = mutableMapOf<String, Int>()
        for (i in 1..100) {
            val verse = engine.getRandomVerse(MoodTag.PEACE)
            counts[verse.id] = counts.getOrElse(verse.id) { 0 } + 1
        }

        // Ensure both 'p1' and 'p2' are selected at least once
        assertTrue(counts.getOrElse("p1") { 0 } > 0)
        assertTrue(counts.getOrElse("p2") { 0 } > 0)
    }
}
