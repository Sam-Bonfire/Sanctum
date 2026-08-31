package com.sanctum.core.feature.search.domain

import com.sanctum.core.feature.scripture.domain.Verse

/**
 * Represents a vector embedding for semantic search.
 */
typealias EmbeddingVector = FloatArray

/**
 * A user's search query for semantic matching.
 */
data class SemanticQuery(
    val text: String,
)

/**
 * Represents a matched verse with its calculated relevance score.
 */
data class SemanticSearchResult(
    val verse: Verse,
    val score: Float,
)
