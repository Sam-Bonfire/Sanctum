package com.sanctum.core.feature.search.domain

import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchQueryBuilderTest {

    private val verse1 = ScriptureVerse(
        id = "v1",
        number = 1,
        originalText = "In the beginning",
        translation = "In the beginning",
        transliteration = null,
    )

    private val verse2 = ScriptureVerse(
        id = "v2",
        number = 2,
        originalText = "God created",
        translation = "God created",
        transliteration = null,
    )

    @Test
    fun testEmptyQueryAndNoFilters() {
        val builder = SearchQueryBuilder()
        val predicate = builder.buildPredicate("", SearchFilterState())

        assertTrue(predicate(verse1))
        assertTrue(predicate(verse2))
    }

    @Test
    fun testQueryMatch() {
        val builder = SearchQueryBuilder()
        val predicate = builder.buildPredicate("beginning", SearchFilterState())

        assertTrue(predicate(verse1))
        assertFalse(predicate(verse2))
    }

    @Test
    fun testAnnotationFilterMatch() {
        val builder = SearchQueryBuilder()
        val filterState = SearchFilterState(requireAnnotation = AnnotationFilter.BOOKMARKED)
        val annotatedIds = setOf("v1")
        val predicate = builder.buildPredicate("", filterState, annotatedIds)

        assertTrue(predicate(verse1))
        assertFalse(predicate(verse2))
    }

    @Test
    fun testQueryAndAnnotationMatch() {
        val builder = SearchQueryBuilder()
        val filterState = SearchFilterState(requireAnnotation = AnnotationFilter.BOOKMARKED)
        val annotatedIds = setOf("v1")

        // Match both
        val predicate1 = builder.buildPredicate("beginning", filterState, annotatedIds)
        assertTrue(predicate1(verse1))

        // Query matches, but annotation doesn't (v2 is not in annotatedIds)
        val predicate2 = builder.buildPredicate("created", filterState, annotatedIds)
        assertFalse(predicate2(verse2))
    }
}
