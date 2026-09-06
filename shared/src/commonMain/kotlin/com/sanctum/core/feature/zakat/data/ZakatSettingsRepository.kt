package com.sanctum.core.feature.zakat.data

import com.russhwolf.settings.Settings
import com.sanctum.core.feature.zakat.domain.NisabStandard

class ZakatSettingsRepository(private val settings: Settings) {
    companion object {
        private const val KEY_GOLD_PRICE = "zakat_gold_price"
        private const val KEY_SILVER_PRICE = "zakat_silver_price"
        private const val KEY_NISAB_STANDARD = "zakat_nisab_standard"
        private const val KEY_CURRENCY = "zakat_currency"

        // Default to common USD prices
        private const val DEFAULT_GOLD_PRICE = 65.0
        private const val DEFAULT_SILVER_PRICE = 0.75
        private const val DEFAULT_NISAB_STANDARD = "GOLD"
        private const val DEFAULT_CURRENCY = "USD"
    }

    fun getGoldPrice(): Double {
        return settings.getDouble(KEY_GOLD_PRICE, DEFAULT_GOLD_PRICE)
    }

    fun setGoldPrice(price: Double) {
        settings.putDouble(KEY_GOLD_PRICE, price)
    }

    fun getSilverPrice(): Double {
        return settings.getDouble(KEY_SILVER_PRICE, DEFAULT_SILVER_PRICE)
    }

    fun setSilverPrice(price: Double) {
        settings.putDouble(KEY_SILVER_PRICE, price)
    }

    fun getNisabStandard(): NisabStandard {
        val standardStr = settings.getString(KEY_NISAB_STANDARD, DEFAULT_NISAB_STANDARD)
        return try {
            NisabStandard.valueOf(standardStr)
        } catch (e: Exception) {
            NisabStandard.GOLD
        }
    }

    fun setNisabStandard(standard: NisabStandard) {
        settings.putString(KEY_NISAB_STANDARD, standard.name)
    }

    fun getCurrency(): String {
        return settings.getString(KEY_CURRENCY, DEFAULT_CURRENCY)
    }

    fun setCurrency(currency: String) {
        settings.putString(KEY_CURRENCY, currency)
    }
}
