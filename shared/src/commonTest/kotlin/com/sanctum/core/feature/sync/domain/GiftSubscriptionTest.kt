package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GiftSubscriptionTest {

    @Test
    fun `validates gift flow`() {
        assertTrue(GiftSubscription("a@b.com", 3).isValid())
        assertFalse(GiftSubscription("not-an-email", 3).isValid())
        assertFalse(GiftSubscription("a@b.com", 2).isValid())
    }

    @Test
    fun `prices by duration`() {
        assertEquals(3_000L, GiftSubscription("a@b.com", 6).priceCents(500))
    }
}
