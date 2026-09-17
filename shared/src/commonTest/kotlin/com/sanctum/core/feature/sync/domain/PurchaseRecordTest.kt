package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PurchaseRecordTest {

    @Test
    fun `flags unacknowledged purchases`() {
        assertTrue(PurchaseRecord("premium_monthly", "token").needsAcknowledgement())
        assertFalse(PurchaseRecord("premium_monthly", "token", true).needsAcknowledgement())
        assertFalse(PurchaseRecord("premium_monthly", "").needsAcknowledgement())
    }

    @Test
    fun `acknowledges and validates`() {
        assertEquals(true, PurchaseRecord("p", "t").acknowledge().acknowledged)
        assertFailsWith<IllegalArgumentException> { PurchaseRecord("p", "").acknowledge() }
    }

    @Test
    fun `checks premium catalog`() {
        assertTrue(isPremiumProduct("premium_monthly", setOf("premium_monthly")))
        assertFalse(isPremiumProduct("other", setOf("premium_monthly")))
    }
}
