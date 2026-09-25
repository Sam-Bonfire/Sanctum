package com.sanctum.core.feature.zakat.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZakatCalculatorTest {

    private val calculator = ZakatCalculator()

    @Test
    fun `calculate returns not eligible when wealth is below nisab threshold`() {
        // Gold nisab is 85g. At $65/g, threshold = $5,525 -> 552500 minor units
        val portfolio = ZakatPortfolio(
            cash = 100000,
            goldValue = 200000,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(300000L, result.totalWealth)
        assertEquals(552500L, result.nisabValue)
        assertFalse(result.isEligible)
        assertEquals(0L, result.zakatPayable)
    }

    @Test
    fun `calculate returns eligible and correct zakat when wealth is exactly at nisab threshold`() {
        val portfolio = ZakatPortfolio(
            cash = 552500,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(552500L, result.totalWealth)
        assertEquals(552500L, result.nisabValue)
        assertTrue(result.isEligible)
        // 2.5% of $5525.00 = $138.125 -> half-up rounds to 13813 minor units
        assertEquals(13813L, result.zakatPayable)
    }

    @Test
    fun `calculate returns eligible and correct zakat when wealth is above nisab threshold`() {
        val portfolio = ZakatPortfolio(
            cash = 1000000,
            investments = 500000,
            liabilities = 200000,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(1300000L, result.totalWealth) // 10k + 5k - 2k
        assertTrue(result.isEligible)
        assertEquals(32500L, result.zakatPayable) // 2.5% of $13000.00
    }

    @Test
    fun `calculate uses silver nisab standard correctly`() {
        // Silver nisab is 595g. At $0.75/g, threshold = $446.25 -> 44625 minor units
        val portfolio = ZakatPortfolio(
            cash = 50000,
            selectedNisabStandard = NisabStandard.SILVER,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(50000L, result.totalWealth)
        assertEquals(44625L, result.nisabValue)
        assertTrue(result.isEligible)
        assertEquals(1250L, result.zakatPayable) // 2.5% of $500.00
    }

    @Test
    fun `minor units stay exact where binary float drifts`() {
        // 2.5% of $10.03 = $0.25075 -> 25 minor units, no float residue
        val portfolio = ZakatPortfolio(cash = 1003)
        val result = calculator.calculate(portfolio, 0.0, 0.0)

        assertEquals(1003L, result.totalWealth)
        assertTrue(result.isEligible)
        assertEquals(25L, result.zakatPayable)
    }
}
