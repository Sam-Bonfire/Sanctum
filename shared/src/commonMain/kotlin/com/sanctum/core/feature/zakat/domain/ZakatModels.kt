package com.sanctum.core.feature.zakat.domain

import com.sanctum.core.core.money.MinorUnits

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
    val cash: MinorUnits = 0L,
    val goldValue: MinorUnits = 0L,
    val silverValue: MinorUnits = 0L,
    val investments: MinorUnits = 0L,
    val businessInventory: MinorUnits = 0L,
    val liabilities: MinorUnits = 0L,
    val selectedNisabStandard: NisabStandard = NisabStandard.GOLD,
)

data class ZakatCalculationResult(
    val totalWealth: MinorUnits,
    val nisabValue: MinorUnits,
    val isEligible: Boolean,
    val zakatPayable: MinorUnits,
)
