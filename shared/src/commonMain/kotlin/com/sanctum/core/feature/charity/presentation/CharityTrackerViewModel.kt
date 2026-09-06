package com.sanctum.core.feature.charity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.charity.domain.CharityCategory
import com.sanctum.core.feature.charity.domain.CharityGoal
import com.sanctum.core.feature.charity.domain.CharityRecord
import com.sanctum.core.feature.charity.domain.CharityRepository
import com.sanctum.core.feature.charity.domain.CharitySummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class CharityUiState(
    val records: List<CharityRecord> = emptyList(),
    val summary: CharitySummary = CharitySummary(0.0, 0.0, 0f),
    val isLoading: Boolean = true,
)

class CharityTrackerViewModel(
    private val repository: CharityRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharityUiState())
    val uiState: StateFlow<CharityUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val records = repository.getMonthlyRecords(now.year, now.monthNumber)
            val goal = repository.getMonthlyGoal()

            val totalGiven = records.sumOf { it.amount }

            val percentage = if (goal.monthlyGoalAmount > 0) {
                (totalGiven / goal.monthlyGoalAmount).toFloat().coerceIn(0f, 1f)
            } else {
                0f
            }

            _uiState.update {
                it.copy(
                    // Simple descending sort
                    records = records.sortedByDescending { it.dateIso },
                    summary = CharitySummary(totalGiven, goal.monthlyGoalAmount, percentage),
                    isLoading = false,
                )
            }
        }
    }

    fun addRecord(amount: Double, category: CharityCategory, notes: String?) {
        viewModelScope.launch {
            val record = CharityRecord(
                id = com.sanctum.core.feature.charity.domain.generateUUID(),
                amount = amount,
                dateIso = Clock.System.now().toString(),
                categoryId = category,
                privateNotes = notes,
            )
            repository.recordDonation(record)
            loadData()
        }
    }

    fun updateRecord(id: String, amount: Double, category: CharityCategory, notes: String?, dateIso: String) {
        viewModelScope.launch {
            val record = CharityRecord(
                id = id,
                amount = amount,
                dateIso = dateIso,
                categoryId = category,
                privateNotes = notes,
            )
            repository.updateRecord(record)
            loadData()
        }
    }

    fun setGoal(amount: Double) {
        viewModelScope.launch {
            repository.setMonthlyGoal(CharityGoal(amount))
            loadData()
        }
    }

    fun deleteRecord(id: String) {
        viewModelScope.launch {
            repository.deleteRecord(id)
            loadData()
        }
    }
}
