package com.sanctum.core.feature.scripture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.scripture.data.ScriptureRepository
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScriptureUiState(
    val isLoading: Boolean = true,
    val chapters: List<ScriptureChapter> = emptyList(),
    val activeChapter: ScriptureChapter? = null,
)

class ScriptureViewModel(private val repository: ScriptureRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ScriptureUiState())
    val uiState: StateFlow<ScriptureUiState> = _uiState.asStateFlow()

    init {
        loadChapters()
    }

    private fun loadChapters() {
        viewModelScope.launch {
            repository.getChapters().collect { chapters ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    chapters = chapters,
                )
            }
        }
    }

    fun loadChapter(chapterId: String) {
        viewModelScope.launch {
            repository.getChapter(chapterId).collect { chapter ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    activeChapter = chapter,
                )
            }
        }
    }
}
