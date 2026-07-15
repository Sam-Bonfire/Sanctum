package com.sanctum.core.feature.duas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.sanctum.core.feature.duas.data.DuasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Dua(
    val id: String,
    val title: String,
    val originalText: String,
    val translation: String,
    val transliteration: String? = null,
)

data class DuasCatalogUiState(
    val duas: List<Dua> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

class DuasCatalogViewModel(
    private val repository: DuasRepository,
    private val settings: Settings,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DuasCatalogUiState())
    val uiState: StateFlow<DuasCatalogUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val religionId = settings.getString("religion_id", "")
            if (religionId.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, duas = emptyList()) }
                return@launch
            }
            try {
                val duas = repository.getDuas(religionId)
                _uiState.update { it.copy(duas = duas, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load supplications.") }
            }
        }
    }
}
