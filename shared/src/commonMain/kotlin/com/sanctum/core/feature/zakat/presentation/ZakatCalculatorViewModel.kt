package com.sanctum.core.feature.zakat.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import com.sanctum.core.feature.zakat.data.ZakatSettingsRepository
import com.sanctum.core.feature.zakat.domain.NisabStandard
import com.sanctum.core.feature.zakat.domain.ZakatCalculationResult
import com.sanctum.core.feature.zakat.domain.ZakatCalculator
import com.sanctum.core.feature.zakat.domain.ZakatPortfolio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ZakatCalculatorState(
    val portfolio: ZakatPortfolio = ZakatPortfolio(),
    val rawCash: String = "",
    val rawGoldValue: String = "",
    val rawSilverValue: String = "",
    val rawInvestments: String = "",
    val rawBusinessInventory: String = "",
    val rawLiabilities: String = "",
    val rawGoldPrice: String = "65.0",
    val rawSilverPrice: String = "0.75",
    val goldPrice: Double = 65.0,
    val silverPrice: Double = 0.75,
    val currency: String = "USD",
    val result: ZakatCalculationResult = ZakatCalculationResult(0.0, 0.0, false, 0.0),
)

class ZakatCalculatorViewModel(
    private val calculator: ZakatCalculator,
    private val repository: ZakatSettingsRepository,
) : ScreenModel {

    private val _state = MutableStateFlow(ZakatCalculatorState())
    val state: StateFlow<ZakatCalculatorState> = _state.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _state.update { currentState ->
            val goldPrice = repository.getGoldPrice()
            val silverPrice = repository.getSilverPrice()
            val currency = repository.getCurrency()
            val nisabStandard = repository.getNisabStandard()

            val portfolio = currentState.portfolio.copy(selectedNisabStandard = nisabStandard)

            val result = calculator.calculate(portfolio, goldPrice, silverPrice)

            currentState.copy(
                goldPrice = goldPrice,
                rawGoldPrice = goldPrice.toString(),
                silverPrice = silverPrice,
                rawSilverPrice = silverPrice.toString(),
                currency = currency,
                portfolio = portfolio,
                result = result,
            )
        }
    }

    fun updateGoldPrice(rawVal: String) {
        val price = rawVal.toDoubleOrNull() ?: 0.0
        repository.setGoldPrice(price)
        _state.update { currentState ->
            val result = calculator.calculate(currentState.portfolio, price, currentState.silverPrice)
            currentState.copy(rawGoldPrice = rawVal, goldPrice = price, result = result)
        }
    }

    fun updateSilverPrice(rawVal: String) {
        val price = rawVal.toDoubleOrNull() ?: 0.0
        repository.setSilverPrice(price)
        _state.update { currentState ->
            val result = calculator.calculate(currentState.portfolio, currentState.goldPrice, price)
            currentState.copy(rawSilverPrice = rawVal, silverPrice = price, result = result)
        }
    }

    fun updateCurrency(currency: String) {
        repository.setCurrency(currency)
        _state.update { it.copy(currency = currency) }
    }

    fun updateNisabStandard(standard: NisabStandard) {
        repository.setNisabStandard(standard)
        _state.update { currentState ->
            val newPortfolio = currentState.portfolio.copy(selectedNisabStandard = standard)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(portfolio = newPortfolio, result = result)
        }
    }

    fun updateCash(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(cash = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawCash = rawVal, portfolio = newPortfolio, result = result)
        }
    }

    fun updateGoldValue(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(goldValue = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawGoldValue = rawVal, portfolio = newPortfolio, result = result)
        }
    }

    fun updateSilverValue(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(silverValue = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawSilverValue = rawVal, portfolio = newPortfolio, result = result)
        }
    }

    fun updateInvestments(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(investments = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawInvestments = rawVal, portfolio = newPortfolio, result = result)
        }
    }

    fun updateBusinessInventory(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(businessInventory = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawBusinessInventory = rawVal, portfolio = newPortfolio, result = result)
        }
    }

    fun updateLiabilities(rawVal: String) {
        _state.update { currentState ->
            val value = rawVal.toDoubleOrNull() ?: 0.0
            val newPortfolio = currentState.portfolio.copy(liabilities = value)
            val result = calculator.calculate(newPortfolio, currentState.goldPrice, currentState.silverPrice)
            currentState.copy(rawLiabilities = rawVal, portfolio = newPortfolio, result = result)
        }
    }
}
