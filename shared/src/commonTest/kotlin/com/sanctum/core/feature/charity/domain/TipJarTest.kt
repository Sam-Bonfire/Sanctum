package com.sanctum.core.feature.charity.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

public class TipJarTest {

    @Test
    public fun `isValidCustomAmount returns true for valid amounts`() {
        val tipJar = TipJar(currencyCode = "USD", presetAmounts = listOf(1.0, 5.0, 10.0), maxCustomAmount = 100.0)
        assertTrue(tipJar.isValidCustomAmount(50.0))
        assertTrue(tipJar.isValidCustomAmount(100.0))
        assertTrue(tipJar.isValidCustomAmount(0.01))
    }

    @Test
    public fun `isValidCustomAmount returns false for zero or negative amounts`() {
        val tipJar = TipJar(currencyCode = "USD", presetAmounts = listOf(1.0, 5.0, 10.0), maxCustomAmount = 100.0)
        assertFalse(tipJar.isValidCustomAmount(0.0))
        assertFalse(tipJar.isValidCustomAmount(-5.0))
    }

    @Test
    public fun `isValidCustomAmount returns false for amounts exceeding maxCustomAmount`() {
        val tipJar = TipJar(currencyCode = "USD", presetAmounts = listOf(1.0, 5.0, 10.0), maxCustomAmount = 100.0)
        assertFalse(tipJar.isValidCustomAmount(100.01))
        assertFalse(tipJar.isValidCustomAmount(200.0))
    }
}
