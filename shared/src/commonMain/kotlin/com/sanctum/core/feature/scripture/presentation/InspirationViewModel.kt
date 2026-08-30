package com.sanctum.core.feature.scripture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.scripture.domain.InspirationVerseEngine
import com.sanctum.core.feature.scripture.domain.MoodTag
import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InspirationUiState(
    val currentVerse: ScriptureVerse? = null,
    val selectedTag: MoodTag? = null,
)

class InspirationViewModel(
    private val verseEngine: InspirationVerseEngine = InspirationVerseEngine(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspirationUiState())
    val uiState: StateFlow<InspirationUiState> = _uiState.asStateFlow()

    init {
        shuffleVerse()
    }

    fun selectTag(tag: MoodTag) {
        val newTag = if (_uiState.value.selectedTag == tag) null else tag
        _uiState.update { it.copy(selectedTag = newTag) }
        shuffleVerse()
    }

    fun shuffleVerse() {
        viewModelScope.launch {
            val nextVerse = verseEngine.getRandomVerse(_uiState.value.selectedTag)
            _uiState.update { it.copy(currentVerse = nextVerse) }
        }
    }
}
