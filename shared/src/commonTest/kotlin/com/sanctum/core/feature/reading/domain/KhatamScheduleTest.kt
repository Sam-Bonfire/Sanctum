package com.sanctum.core.feature.reading.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KhatamScheduleTest {

    private fun coveredJuz(refs: List<String>): List<Int> = refs.flatMap { ref ->
        val range = ref.removePrefix("Juz ").split("-").map { it.toInt() }
        if (range.size == 1) listOf(range[0]) else (range[0]..range[1]).toList()
    }

    @Test
    fun thirtyDaysOneJuzPerDay() {
        val refs = buildKhatamRefs(30)
        assertEquals(30, refs.size)
        assertEquals("Juz 1", refs.first())
        assertEquals("Juz 30", refs.last())
        assertEquals((1..30).toList(), coveredJuz(refs))
    }

    @Test
    fun shorterPlansCoverEveryJuzExactlyOnce() {
        for (days in listOf(1, 7, 10, 15, 29)) {
            val refs = buildKhatamRefs(days)
            assertEquals(days, refs.size, "dayCount $days")
            assertEquals((1..30).toList(), coveredJuz(refs), "dayCount $days")
            assertTrue(refs.first().startsWith("Juz 1"))
            assertTrue(refs.last().endsWith("30"))
        }
    }

    @Test
    fun sevenDaySplitFrontLoadsRemainder() {
        // 30 = 5+5+4+4+4+4+4: extra juz go to the earliest days.
        val refs = buildKhatamRefs(7)
        assertEquals("Juz 1-5", refs[0])
        assertEquals("Juz 6-10", refs[1])
        assertEquals("Juz 27-30", refs[6])
    }

    @Test
    fun rejectsOutOfRangeDayCounts() {
        assertFailsWith<IllegalArgumentException> { buildKhatamRefs(0) }
        assertFailsWith<IllegalArgumentException> { buildKhatamRefs(31) }
    }
}
