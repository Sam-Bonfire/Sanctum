package com.sanctum.core.feature.sync.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SupporterBadgeTest {

    @Test
    fun `resolves known tiers`() {
        assertEquals("Gold Supporter", badgeForTier("gold")?.label)
        assertNull(badgeForTier("platinum"))
    }

    @Test
    fun `ranks tiers`() {
        val gold = badgeForTier("gold")!!
        val bronze = badgeForTier("bronze")!!
        assertTrue(gold.outranks(bronze))
        assertFalse(bronze.outranks(gold))
        assertFalse(gold.outranks(gold))
    }
}
