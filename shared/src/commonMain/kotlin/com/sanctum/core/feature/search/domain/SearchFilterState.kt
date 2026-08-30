package com.sanctum.core.feature.search.domain

data class SearchFilterState(
    val bookId: String? = null,
    val division: String? = null,
    val requireAnnotation: AnnotationFilter = AnnotationFilter.NONE,
    val sortOrder: SortOrder = SortOrder.RELEVANCE,
)

enum class AnnotationFilter {
    NONE,
    BOOKMARKED,
    HIGHLIGHTED,
    NOTED,
}

enum class SortOrder {
    RELEVANCE,
    CHRONOLOGICAL,
    ALPHABETICAL,
}
