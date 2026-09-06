package com.sanctum.core.feature.zakat.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZakatCalculatorTest {

    private val calculator = ZakatCalculator()

    @Test
    fun `calculate returns not eligible when wealth is below nisab threshold`() {
        // Gold nisab is 85g. At $65/g, threshold = $5,525
        val portfolio = ZakatPortfolio(
            cash = 1000.0,
            goldValue = 2000.0,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(3000.0, result.totalWealth)
        assertEquals(5525.0, result.nisabValue)
        assertFalse(result.isEligible)
        assertEquals(0.0, result.zakatPayable)
    }

    @Test
    fun `calculate returns eligible and correct zakat when wealth is exactly at nisab threshold`() {
        val portfolio = ZakatPortfolio(
            cash = 5525.0,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(5525.0, result.totalWealth)
        assertEquals(5525.0, result.nisabValue)
        assertTrue(result.isEligible)
        assertEquals(5525.0 * 0.025, result.zakatPayable)
    }

    @Test
    fun `calculate returns eligible and correct zakat when wealth is above nisab threshold`() {
        val portfolio = ZakatPortfolio(
            cash = 10000.0,
            investments = 5000.0,
            liabilities = 2000.0,
            selectedNisabStandard = NisabStandard.GOLD,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(13000.0, result.totalWealth) // 10k + 5k - 2k
        assertTrue(result.isEligible)
        assertEquals(13000.0 * 0.025, result.zakatPayable)
    }

    @Test
    fun `calculate uses silver nisab standard correctly`() {
        // Silver nisab is 595g. At $0.75/g, threshold = $446.25
        val portfolio = ZakatPortfolio(
            cash = 500.0,
            selectedNisabStandard = NisabStandard.SILVER,
        )

        val result = calculator.calculate(portfolio, 65.0, 0.75)

        assertEquals(500.0, result.totalWealth)
        assertEquals(446.25, result.nisabValue)
        assertTrue(result.isEligible)
        assertEquals(500.0 * 0.025, result.zakatPayable)
    }
}
