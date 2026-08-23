package com.sanctum.core.feature.scripture.domain.history

data class HistoricalContext(
    val verseId: String,
    val timelineDate: String?,
    val location: HistoricalLocation?,
    val figures: List<HistoricalFigure>,
)

data class HistoricalFigure(
    val id: String,
    val name: String,
    val summary: String,
    val imageUrl: String? = null,
)

data class HistoricalLocation(
    val id: String,
    val name: String,
    val description: String,
    val mapImageUrl: String? = null,
)
