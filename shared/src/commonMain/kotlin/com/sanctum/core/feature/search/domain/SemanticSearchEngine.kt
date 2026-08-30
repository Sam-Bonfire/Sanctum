package com.sanctum.core.feature.search.domain

import com.sanctum.core.feature.scripture.domain.Verse
import kotlin.math.sqrt

/**
 * Engine for performing offline semantic vector search using cosine similarity.
 */
class SemanticSearchEngine {

    /**
     * Finds the most semantically similar verses for a given query vector.
     *
     * @param queryVector The embedding vector of the search query.
     * @param corpus The pre-indexed map of verses to their embedding vectors.
     * @param limit The maximum number of results to return.
     * @return A list of [SemanticSearchResult] sorted by descending relevance score.
     */
    fun search(
        queryVector: EmbeddingVector,
        corpus: Map<Verse, EmbeddingVector>,
        limit: Int = 10,
    ): List<SemanticSearchResult> {
        if (queryVector.isEmpty() || corpus.isEmpty()) {
            return emptyList()
        }

        return corpus.mapNotNull { (verse, verseVector) ->
            if (verseVector.isEmpty()) return@mapNotNull null

            // Only compare if dimensions match, skip otherwise
            if (queryVector.size != verseVector.size) return@mapNotNull null

            val score = cosineSimilarity(queryVector, verseVector)
            if (score.isNaN()) null else SemanticSearchResult(verse, score)
        }
            .sortedByDescending { it.score }
            .take(limit)
    }

    /**
     * Calculates the cosine similarity between two vectors.
     *
     * @param vec1 First vector
     * @param vec2 Second vector
     * @return A float representing the cosine similarity [-1.0, 1.0].
     */
    internal fun cosineSimilarity(vec1: EmbeddingVector, vec2: EmbeddingVector): Float {
        if (vec1.size != vec2.size) {
            throw IllegalArgumentException("Vectors must be of the same size. (vec1: ${vec1.size}, vec2: ${vec2.size})")
        }

        var dotProduct = 0f
        var norm1 = 0f
        var norm2 = 0f

        for (i in vec1.indices) {
            val v1 = vec1[i]
            val v2 = vec2[i]
            dotProduct += v1 * v2
            norm1 += v1 * v1
            norm2 += v2 * v2
        }

        if (norm1 == 0f || norm2 == 0f) return 0f

        return dotProduct / (sqrt(norm1.toDouble()) * sqrt(norm2.toDouble())).toFloat()
    }
}
