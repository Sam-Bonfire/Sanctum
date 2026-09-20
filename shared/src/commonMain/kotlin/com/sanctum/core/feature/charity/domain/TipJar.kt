package com.sanctum.core.feature.charity.domain

import kotlinx.serialization.Serializable

@Serializable
public data class TipJar(
    public val currencyCode: String,
    public val presetAmounts: List<Double>,
    public val maxCustomAmount: Double,
) {
    public fun isValidCustomAmount(amount: Double): Boolean {
        return amount > 0.0 && amount <= maxCustomAmount
    }
}
