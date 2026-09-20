package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

public class AnonymousPrayerTest {

    @Test
    public fun testValidCreationPublic() {
        val prayer: AnonymousPrayer = AnonymousPrayer(
            id = "123",
            requestText = "Please pray for my exams",
            isAnonymous = false,
            name = "John Doe",
            contact = "john@example.com",
        )
        assertEquals("123", prayer.id)
        assertEquals("Please pray for my exams", prayer.requestText)
        assertEquals(false, prayer.isAnonymous)
        assertEquals("John Doe", prayer.name)
        assertEquals("john@example.com", prayer.contact)
    }

    @Test
    public fun testValidCreationAnonymous() {
        val prayer: AnonymousPrayer = AnonymousPrayer(
            id = "124",
            requestText = "Please pray for my health",
            isAnonymous = true,
            name = null,
            contact = null,
        )
        assertEquals("124", prayer.id)
        assertEquals(true, prayer.isAnonymous)
        assertNull(prayer.name)
        assertNull(prayer.contact)
    }

    @Test
    public fun testInvalidBlankId() {
        assertFailsWith<IllegalArgumentException> {
            AnonymousPrayer(
                id = "   ",
                requestText = "Valid text",
                isAnonymous = true,
                name = null,
                contact = null,
            )
        }
    }

    @Test
    public fun testInvalidBlankRequestText() {
        assertFailsWith<IllegalArgumentException> {
            AnonymousPrayer(
                id = "125",
                requestText = "",
                isAnonymous = false,
                name = "Jane",
                contact = null,
            )
        }
    }

    @Test
    public fun testInvalidAnonymousWithName() {
        assertFailsWith<IllegalArgumentException> {
            AnonymousPrayer(
                id = "126",
                requestText = "Text",
                isAnonymous = true,
                name = "Jane",
                contact = null,
            )
        }
    }

    @Test
    public fun testToggleAnonymousTrue() {
        val original: AnonymousPrayer = AnonymousPrayer(
            id = "127",
            requestText = "Help",
            isAnonymous = false,
            name = "Peter",
            contact = "12345",
        )

        val toggled: AnonymousPrayer = original.toggleAnonymous(true)
        assertEquals(true, toggled.isAnonymous)
        assertNull(toggled.name)
        assertNull(toggled.contact)
        assertEquals("127", toggled.id)
    }

    @Test
    public fun testToggleAnonymousFalse() {
        val original: AnonymousPrayer = AnonymousPrayer(
            id = "128",
            requestText = "Help",
            isAnonymous = true,
            name = null,
            contact = null,
        )

        val toggled: AnonymousPrayer = original.toggleAnonymous(false)
        assertEquals(false, toggled.isAnonymous)
        assertNull(toggled.name)
        assertNull(toggled.contact)
    }
}
