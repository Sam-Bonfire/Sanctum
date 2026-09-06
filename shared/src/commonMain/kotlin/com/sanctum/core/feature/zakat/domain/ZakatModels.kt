package com.sanctum.core.feature.zakat.domain

enum class NisabStandard {
    GOLD,
    SILVER,
}

enum class AssetCategory {
    CASH,
    GOLD,
    SILVER,
    INVESTMENT,
    BUSINESS_INVENTORY,
    LIABILITY,
}

data class ZakatPortfolio(
    val cash: Double = 0.0,
    val goldValue: Double = 0.0,
    val silverValue: Double = 0.0,
    val investments: Double = 0.0,
    val businessInventory: Double = 0.0,
    val liabilities: Double = 0.0,
    val selectedNisabStandard: NisabStandard = NisabStandard.GOLD,
)

data class ZakatCalculationResult(
    val totalWealth: Double,
    val nisabValue: Double,
    val isEligible: Boolean,
    val zakatPayable: Double,
)
