package com.sanctum.core.feature.reading.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.reading.domain.GetAvailablePlansUseCase
import com.sanctum.core.feature.reading.domain.GetDailyReadingTargetUseCase
import com.sanctum.core.feature.reading.domain.GetEnrolledPlansUseCase
import com.sanctum.core.feature.reading.domain.ReadingPlan
import com.sanctum.core.feature.reading.domain.ReadingPlanRepository
import com.sanctum.core.feature.reading.domain.ReadingPlanState
import com.sanctum.core.feature.reading.domain.ToggleCheckpointCompletedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReadingPlanUiState(
    val enrolledPlans: List<ReadingPlanState> = emptyList(),
    val availablePlans: List<ReadingPlan> = emptyList(),
    val selectedPlanId: String? = null,
    val isLoading: Boolean = true,
)

class ReadingPlanViewModel(
    private val repository: ReadingPlanRepository,
    private val getEnrolledPlans: GetEnrolledPlansUseCase,
    private val getAvailablePlans: GetAvailablePlansUseCase,
    private val getDailyTarget: GetDailyReadingTargetUseCase,
    private val toggleCheckpoint: ToggleCheckpointCompletedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadingPlanUiState())
    val uiState: StateFlow<ReadingPlanUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val enrolled = getEnrolledPlans()
            val available = getAvailablePlans().filter { plan ->
                enrolled.none { it.plan.id == plan.id }
            }
            _uiState.update {
                it.copy(
                    enrolledPlans = enrolled,
                    availablePlans = available,
                    isLoading = false,
                )
            }
        }
    }

    fun enroll(planId: String) {
        repository.enroll(planId)
        load()
    }

    fun unenroll(planId: String) {
        repository.unenroll(planId)
        load()
    }

    fun toggleCheckpoint(planId: String, checkpointKey: String, completed: Boolean) {
        toggleCheckpoint(planId, checkpointKey, completed)
        load()
    }
}
