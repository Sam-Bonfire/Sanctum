package com.sanctum.core.feature.scripture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.scripture.data.BookmarkRepository
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.BookmarkTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookmarkUiState(
    val isLoading: Boolean = true,
    val bookmarks: List<Bookmark> = emptyList(),
    val availableTags: List<BookmarkTag> = emptyList(),
    val selectedTagId: Int? = null,
)

class BookmarkViewModel(private val repository: BookmarkRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getTags().collect { tags ->
                _uiState.value = _uiState.value.copy(availableTags = tags)
            }
        }
        viewModelScope.launch {
            repository.getBookmarks().collect { bookmarks ->
                _uiState.value = _uiState.value.copy(isLoading = false, bookmarks = bookmarks)
            }
        }
    }

    fun selectTag(tagId: Int?) {
        _uiState.value = _uiState.value.copy(selectedTagId = tagId)
    }

    fun deleteTag(tagId: Int) {
        viewModelScope.launch {
            repository.deleteTag(tagId)
        }
    }

    fun createTag(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.createTag(name, colorHex)
        }
    }

    fun renameTag(tagId: Int, newName: String) {
        viewModelScope.launch {
            repository.renameTag(tagId, newName)
        }
    }
}
