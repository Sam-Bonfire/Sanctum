package com.sanctum.core.feature.names.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.names.data.NamesRepository
import com.sanctum.core.feature.names.domain.DivineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NamesOfAllahUiState(
    val names: List<DivineName> = emptyList(),
    val filteredNames: List<DivineName> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val favorites: Set<Int> = emptySet(),
    val memorized: Set<Int> = emptySet(),
    val selectedName: DivineName? = null,
)

class NamesOfAllahViewModel(
    private val repository: NamesRepository,
    private val audioPlayer: com.sanctum.core.feature.prayer.domain.AudioPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NamesOfAllahUiState())
    val uiState: StateFlow<NamesOfAllahUiState> = _uiState.asStateFlow()

    init {
        loadNames()
    }

    fun loadNames() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val names = repository.getNames()
            val favorites = names.filter { repository.isFavorited(it.id) }.map { it.id }.toSet()
            val memorized = names.filter { repository.isMemorized(it.id) }.map { it.id }.toSet()
            _uiState.update {
                it.copy(
                    names = names,
                    filteredNames = names,
                    favorites = favorites,
                    memorized = memorized,
                    isLoading = false,
                )
            }
        }
    }

    fun search(query: String) {
        val filtered = repository.searchNames(query)
        _uiState.update { it.copy(searchQuery = query, filteredNames = filtered) }
    }

    fun toggleFavorite(nameId: Int) {
        val current = _uiState.value.favorites
        val newState = if (nameId in current) current - nameId else current + nameId
        repository.setFavorited(nameId, nameId !in current)
        _uiState.update { it.copy(favorites = newState) }
    }

    fun toggleMemorized(nameId: Int) {
        val current = _uiState.value.memorized
        val newState = if (nameId in current) current - nameId else current + nameId
        repository.setMemorized(nameId, nameId !in current)
        _uiState.update { it.copy(memorized = newState) }
    }

    fun playAudio(audioFileName: String) {
        if (audioFileName.isNotEmpty()) audioPlayer.play(audioFileName)
    }

    fun stopAudio() {
        audioPlayer.stop()
    }

    fun selectName(name: DivineName?) {
        _uiState.update { it.copy(selectedName = name) }
    }
}
