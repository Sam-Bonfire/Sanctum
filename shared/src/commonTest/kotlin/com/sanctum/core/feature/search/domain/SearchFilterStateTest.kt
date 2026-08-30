package com.sanctum.core.feature.search.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SearchFilterStateTest {

    @Test
    fun testDefaultState() {
        val state = SearchFilterState()
        assertNull(state.bookId)
        assertNull(state.division)
        assertEquals(AnnotationFilter.NONE, state.requireAnnotation)
        assertEquals(SortOrder.RELEVANCE, state.sortOrder)
    }

    @Test
    fun testCustomState() {
        val state = SearchFilterState(
            bookId = "book1",
            division = "Old Testament",
            requireAnnotation = AnnotationFilter.BOOKMARKED,
            sortOrder = SortOrder.CHRONOLOGICAL,
        )
        assertEquals("book1", state.bookId)
        assertEquals("Old Testament", state.division)
        assertEquals(AnnotationFilter.BOOKMARKED, state.requireAnnotation)
        assertEquals(SortOrder.CHRONOLOGICAL, state.sortOrder)
    }
}
