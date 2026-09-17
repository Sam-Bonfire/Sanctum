package com.sanctum.core.feature.calendar.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MitzvahCalculatorTest {

    @Test
    fun `computes mitzvah dates`() {
        val birth = LocalDate(2013, 5, 4)
        assertEquals(LocalDate(2026, 5, 4), mitzvahDate(birth, MitzvahType.BAR))
        assertEquals(LocalDate(2025, 5, 4), mitzvahDate(birth, MitzvahType.BAT))
    }

    @Test
    fun `assigns portion in range`() {
        val portion = torahPortionIndex(LocalDate(2026, 5, 4))
        assertTrue(portion in 1..54)
        assertEquals(portion, torahPortionIndex(LocalDate(2026, 5, 4)))
    }
}
