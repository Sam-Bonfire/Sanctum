package com.sanctum.core.feature.reading.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BibleYearScheduleTest {

    private fun coveredChapters(refs: List<String>): List<Int> = refs.flatMap { ref ->
        val range = ref.removePrefix("Chapters ").removePrefix("Chapter ").split("-").map { it.toInt() }
        if (range.size == 1) listOf(range[0]) else (range[0]..range[1]).toList()
    }

    @Test
    fun defaultYearCoversEveryChapterExactlyOnce() {
        val refs = buildBibleYearRefs()
        assertEquals(BIBLE_YEAR_DAY_COUNT, refs.size)
        assertEquals((1..BIBLE_CHAPTER_COUNT).toList(), coveredChapters(refs))
        assertEquals("Chapters 1-4", refs.first())
        assertTrue(refs.last().endsWith("1189"))
    }

    @Test
    fun shorterPlansCoverEveryChapterExactlyOnce() {
        for (days in listOf(1, 30, 100, BIBLE_CHAPTER_COUNT)) {
            val refs = buildBibleYearRefs(days)
            assertEquals(days, refs.size, "dayCount $days")
            assertEquals((1..BIBLE_CHAPTER_COUNT).toList(), coveredChapters(refs), "dayCount $days")
        }
    }

    @Test
    fun rejectsOutOfRangeDayCounts() {
        assertFailsWith<IllegalArgumentException> { buildBibleYearRefs(0) }
        assertFailsWith<IllegalArgumentException> { buildBibleYearRefs(BIBLE_CHAPTER_COUNT + 1) }
    }
}
