package com.sanctum.core.feature.search.domain

import com.sanctum.core.feature.scripture.domain.ScriptureVerse

class SearchQueryBuilder {

    fun buildPredicate(query: String, filterState: SearchFilterState, annotatedVerseIds: Set<String> = emptySet()): (ScriptureVerse) -> Boolean {
        return { verse ->
            val matchesQuery = query.isBlank() ||
                verse.translation.contains(query, ignoreCase = true) ||
                verse.originalText.contains(query, ignoreCase = true) ||
                (verse.transliteration != null && verse.transliteration.contains(query, ignoreCase = true))

            val matchesAnnotation = when (filterState.requireAnnotation) {
                AnnotationFilter.NONE -> true
                AnnotationFilter.BOOKMARKED, AnnotationFilter.HIGHLIGHTED, AnnotationFilter.NOTED -> annotatedVerseIds.contains(verse.id)
            }

            // Note: Since ScriptureVerse doesn't directly expose bookId or division,
            // these filters would need to be applied at a higher level (e.g. database query)
            // or ScriptureVerse needs to be extended to include this metadata.
            // For this implementation, we handle the predicate based on available data.

            matchesQuery && matchesAnnotation
        }
    }
}
