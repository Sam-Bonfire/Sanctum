package com.sanctum.core.feature.scripture.domain.history

import kotlinx.serialization.Serializable

@Serializable
data class HistoricalContext(
    val verseId: String,
    val timelineDate: String?,
    val location: HistoricalLocation?,
    val figures: List<HistoricalFigure>,
)

@Serializable
data class HistoricalFigure(
    val id: String,
    val name: String,
    val summary: String,
    val imageUrl: String? = null,
)

@Serializable
data class HistoricalLocation(
    val id: String,
    val name: String,
    val description: String,
    val mapImageUrl: String? = null,
)
