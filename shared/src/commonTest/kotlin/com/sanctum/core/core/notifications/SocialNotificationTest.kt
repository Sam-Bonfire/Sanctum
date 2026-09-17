package com.sanctum.core.core.notifications

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SocialNotificationTest {

    @Test
    fun `formats like and comment titles`() {
        assertEquals(
            "Sara liked your post",
            SocialNotification(SocialEvent.LIKE, "Sara", "Verse").title(),
        )
        assertEquals(
            "Ali commented on your post",
            SocialNotification(SocialEvent.COMMENT, "Ali", "Verse").title(),
        )
    }

    @Test
    fun `trims body and validates`() {
        assertEquals(120, SocialNotification(SocialEvent.LIKE, "A", "x".repeat(200)).body().length)
        assertTrue(SocialNotification(SocialEvent.LIKE, "A", "B").isValid())
        assertFalse(SocialNotification(SocialEvent.LIKE, "", "B").isValid())
    }
}
