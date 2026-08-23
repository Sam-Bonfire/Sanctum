package com.sanctum.core.feature.scripture.domain.history

object MockHistory {
    val mockContext = HistoricalContext(
        verseId = "S-008",
        timelineDate = "c. 1000 BCE",
        location = HistoricalLocation(
            id = "loc-1",
            name = "Ancient Near East",
            description = "A region encompassing the ancient civilizations of the Middle East.",
            mapImageUrl = "https://example.com/map.jpg",
        ),
        figures = listOf(
            HistoricalFigure(
                id = "fig-1",
                name = "Notable Figure",
                summary = "A prominent historical figure related to the events of this verse.",
                imageUrl = "https://example.com/figure.jpg",
            ),
        ),
    )
}
