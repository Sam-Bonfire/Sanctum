package com.sanctum.core.feature.zakat.domain

import com.sanctum.core.core.money.toMinorUnits

class ZakatCalculator {
    companion object {
        const val GOLD_NISAB_GRAMS = 85.0
        const val SILVER_NISAB_GRAMS = 595.0
        const val ZAKAT_NUMERATOR = 25L
        const val ZAKAT_DENOMINATOR = 1000L
    }

    fun calculate(
        portfolio: ZakatPortfolio,
        goldPricePerGram: Double,
        silverPricePerGram: Double,
    ): ZakatCalculationResult {
        val totalWealth = portfolio.cash +
            portfolio.goldValue +
            portfolio.silverValue +
            portfolio.investments +
            portfolio.businessInventory -
            portfolio.liabilities

        val nisabValue = when (portfolio.selectedNisabStandard) {
            NisabStandard.GOLD -> (GOLD_NISAB_GRAMS * goldPricePerGram).toMinorUnits()
            NisabStandard.SILVER -> (SILVER_NISAB_GRAMS * silverPricePerGram).toMinorUnits()
        }

        val isEligible = totalWealth >= nisabValue
        // 2.5% in integer math with half-up rounding: (w * 25 + 500) / 1000
        val zakatPayable = if (isEligible) (totalWealth * ZAKAT_NUMERATOR + ZAKAT_DENOMINATOR / 2) / ZAKAT_DENOMINATOR else 0L

        return ZakatCalculationResult(
            totalWealth = totalWealth,
            nisabValue = nisabValue,
            isEligible = isEligible,
            zakatPayable = zakatPayable,
        )
    }
}
