package com.sanctum.core.core.designsystem.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HolidayThemeTest {

    private val themes = listOf(
        HolidayTheme("ramadan", "islam", "#0B3D2E", "#C9A227", 3, 1, 30),
        HolidayTheme("diwali", "hinduism", "#4A148C", "#FFB300", 10, 20, 25),
    )

    @Test
    fun `resolves active holiday theme`() {
        assertEquals("ramadan", themeForHoliday(themes, "islam", 3, 15)?.holidayId)
        assertNull(themeForHoliday(themes, "islam", 4, 1))
        assertNull(themeForHoliday(themes, "jewish", 3, 15))
    }

    @Test
    fun `respects day window`() {
        assertNull(themeForHoliday(themes, "hinduism", 10, 19))
        assertEquals("diwali", themeForHoliday(themes, "hinduism", 10, 20)?.holidayId)
    }
}
