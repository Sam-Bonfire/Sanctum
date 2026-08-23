package com.sanctum.core.feature.journal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.journal.data.JournalRepository
import com.sanctum.core.feature.journal.domain.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val currentEntry: JournalEntry? = null,
    val isLoading: Boolean = true,
)

class JournalViewModel(
    private val repository: JournalRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    fun loadAllEntries() {
        viewModelScope.launch {
            repository.getAllEntries().collect { entries ->
                _uiState.value = _uiState.value.copy(entries = entries, isLoading = false)
            }
        }
    }

    fun loadEntry(id: Int) {
        viewModelScope.launch {
            val entry = repository.getEntryById(id)
            _uiState.value = _uiState.value.copy(currentEntry = entry)
        }
    }

    fun prepareNewEntry(verseId: Int?, chapterId: Int?) {
        val newEntry = JournalEntry(
            id = 0,
            verseId = verseId,
            chapterId = chapterId,
            title = "",
            content = "",
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            moodTags = emptyList(),
        )
        _uiState.value = _uiState.value.copy(currentEntry = newEntry)
    }

    fun saveEntry(title: String, content: String) {
        val current = _uiState.value.currentEntry ?: return
        val updated = current.copy(
            title = title,
            content = content,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
        )
        viewModelScope.launch {
            repository.saveEntry(updated)
            _uiState.value = _uiState.value.copy(currentEntry = updated)
        }
    }

    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteEntry(id)
        }
    }
}
