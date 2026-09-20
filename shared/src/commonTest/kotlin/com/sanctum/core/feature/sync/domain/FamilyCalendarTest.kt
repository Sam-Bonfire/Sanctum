package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FamilyCalendarTest {

    @Test
    fun testSuccessfulInstantiation() {
        val event = FamilyCalendar.SharedEvent(title = "Dinner", atMs = 123456789L)
        val calendar = FamilyCalendar(
            id = "cal_123",
            memberFlavorIds = setOf("islam", "christianity"),
            events = listOf(event),
        )

        assertEquals("cal_123", calendar.id)
        assertEquals(2, calendar.memberFlavorIds.size)
        assertTrue(calendar.hasMember("islam"))
        assertTrue(calendar.hasMember("christianity"))
        assertEquals(1, calendar.events.size)
        assertEquals("Dinner", calendar.events[0].title)
        assertEquals(123456789L, calendar.events[0].atMs)
    }

    @Test
    fun testBlankCalendarIdThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            FamilyCalendar(
                id = "   ",
                memberFlavorIds = setOf("islam"),
                events = emptyList(),
            )
        }
    }

    @Test
    fun testEmptyMemberFlavorIdsThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            FamilyCalendar(
                id = "cal_123",
                memberFlavorIds = emptySet(),
                events = emptyList(),
            )
        }
    }

    @Test
    fun testBlankMemberFlavorIdThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            FamilyCalendar(
                id = "cal_123",
                memberFlavorIds = setOf("islam", "   "),
                events = emptyList(),
            )
        }
    }

    @Test
    fun testBlankEventTitleThrowsException() {
        assertFailsWith<IllegalArgumentException> {
            FamilyCalendar.SharedEvent(title = "  ", atMs = 0L)
        }
    }

    @Test
    fun testHasMember() {
        val calendar = FamilyCalendar(
            id = "cal_123",
            memberFlavorIds = setOf("islam", "hinduism"),
            events = emptyList(),
        )

        assertTrue(calendar.hasMember("islam"))
        assertTrue(calendar.hasMember("hinduism"))
        assertFalse(calendar.hasMember("christianity"))
        assertFalse(calendar.hasMember("buddhism"))
    }
}
