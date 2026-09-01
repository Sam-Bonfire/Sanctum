package com.sanctum.core.feature.search.domain

import com.sanctum.core.feature.scripture.domain.Verse
import com.sanctum.core.feature.search.data.LocalSemanticSearchIndexer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SemanticSearchEngineTest {

    private val engine = SemanticSearchEngine()
    private val indexer = LocalSemanticSearchIndexer()

    @Test
    fun testCosineSimilarityIdenticalVectors() {
        val vec1 = floatArrayOf(1f, 0f, 0f)
        val vec2 = floatArrayOf(1f, 0f, 0f)

        val score = engine.cosineSimilarity(vec1, vec2)
        assertEquals(1.0f, score)
    }

    @Test
    fun testCosineSimilarityOrthogonalVectors() {
        val vec1 = floatArrayOf(1f, 0f, 0f)
        val vec2 = floatArrayOf(0f, 1f, 0f)

        val score = engine.cosineSimilarity(vec1, vec2)
        assertEquals(0.0f, score)
    }

    @Test
    fun testCosineSimilarityOppositeVectors() {
        val vec1 = floatArrayOf(1f, 0f, 0f)
        val vec2 = floatArrayOf(-1f, 0f, 0f)

        val score = engine.cosineSimilarity(vec1, vec2)
        assertEquals(-1.0f, score)
    }

    @Test
    fun testEmptyQueryReturnsEmptyList() {
        val corpus = mapOf(
            Verse(1, 1, 1, "test", "test") to floatArrayOf(1f, 0f, 0f),
        )
        val results = engine.search(floatArrayOf(), corpus)
        assertTrue(results.isEmpty())
    }

    @Test
    fun testEmptyCorpusReturnsEmptyList() {
        val results = engine.search(floatArrayOf(1f, 0f, 0f), emptyMap())
        assertTrue(results.isEmpty())
    }

    @Test
    fun testRankingPrecision() {
        val queryText = "comfort"
        val queryVector = indexer.embedQuery(queryText)

        val comfortVerse = Verse(1, 1, 1, "Test", "God gives comfort and peace to the sorrowful.")
        val praiseVerse = Verse(2, 1, 2, "Test", "All praise and gratitude is due.")
        val patienceVerse = Verse(3, 1, 3, "Test", "Have patience in trial.")

        val corpus = indexer.buildCorpus(listOf(comfortVerse, praiseVerse, patienceVerse))

        val results = engine.search(queryVector, corpus)

        // Since the query is "comfort", the comfortVerse should be ranked highest
        assertTrue(results.isNotEmpty())
        assertEquals(comfortVerse.id, results.first().verse.id)
        assertTrue(results.first().score > 0.9f) // Should be a very strong match based on our mock indexer
    }
}
