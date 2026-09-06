package com.sanctum.core.feature.zakat.domain

class ZakatCalculator {
    companion object {
        const val GOLD_NISAB_GRAMS = 85.0
        const val SILVER_NISAB_GRAMS = 595.0
        const val ZAKAT_RATE = 0.025
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
            NisabStandard.GOLD -> GOLD_NISAB_GRAMS * goldPricePerGram
            NisabStandard.SILVER -> SILVER_NISAB_GRAMS * silverPricePerGram
        }

        val isEligible = totalWealth >= nisabValue
        val zakatPayable = if (isEligible) totalWealth * ZAKAT_RATE else 0.0

        return ZakatCalculationResult(
            totalWealth = totalWealth,
            nisabValue = nisabValue,
            isEligible = isEligible,
            zakatPayable = zakatPayable,
        )
    }
}
