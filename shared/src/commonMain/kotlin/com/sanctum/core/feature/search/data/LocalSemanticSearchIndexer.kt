package com.sanctum.core.feature.search.data

import com.sanctum.core.feature.scripture.domain.Verse
import com.sanctum.core.feature.search.domain.EmbeddingVector

/**
 * A local mock indexer that simulates vector embeddings for offline semantic search.
 * In a production KMP app without external dependencies, this could map predefined
 * thematic axes to embeddings (e.g. TF-IDF over pre-computed local SQLite data)
 * or utilize an on-device ONNX model.
 */
class LocalSemanticSearchIndexer {

    // Simple 3-dimensional embedding space: [Comfort, Praise, Patience]
    companion object {
        const val DIMENSIONS = 3
    }

    /**
     * Converts a natural language string into a mock semantic embedding vector.
     */
    fun embedQuery(query: String): EmbeddingVector {
        val q = query.lowercase()
        var comfort = 0f
        var praise = 0f
        var patience = 0f

        if ("comfort" in q || "sorrow" in q || "sadness" in q || "peace" in q || "grief" in q) {
            comfort = 1f
        }
        if ("praise" in q || "gratitude" in q || "thanks" in q || "glory" in q) {
            praise = 1f
        }
        if ("patience" in q || "wait" in q || "endurance" in q || "time" in q || "trial" in q) {
            patience = 1f
        }

        // If no keywords matched, return a small uniform vector or zero vector
        if (comfort == 0f && praise == 0f && patience == 0f) {
            return floatArrayOf(0.1f, 0.1f, 0.1f)
        }

        return normalize(floatArrayOf(comfort, praise, patience))
    }

    /**
     * Embeds a verse based on its translated text.
     */
    fun embedVerse(verse: Verse): EmbeddingVector {
        return embedQuery(verse.translatedText)
    }

    /**
     * Helper to mock index a list of verses.
     */
    fun buildCorpus(verses: List<Verse>): Map<Verse, EmbeddingVector> {
        val corpus = mutableMapOf<Verse, EmbeddingVector>()
        for (verse in verses) {
            corpus[verse] = embedVerse(verse)
        }
        return corpus
    }

    private fun normalize(vector: FloatArray): FloatArray {
        var norm = 0f
        for (v in vector) {
            norm += v * v
        }
        if (norm == 0f) return vector
        val sqrtNorm = kotlin.math.sqrt(norm.toDouble()).toFloat()
        return FloatArray(vector.size) { vector[it] / sqrtNorm }
    }
}
