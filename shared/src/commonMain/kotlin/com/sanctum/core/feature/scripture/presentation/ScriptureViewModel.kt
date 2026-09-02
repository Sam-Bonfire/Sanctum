package com.sanctum.core.feature.scripture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.scripture.data.ScriptureRepository
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScrollPositionRepository
import com.sanctum.core.feature.scripture.domain.crossreference.CrossReference
import com.sanctum.core.feature.scripture.domain.crossreference.CrossReferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScriptureUiState(
    val isLoading: Boolean = true,
    val chapters: List<ScriptureChapter> = emptyList(),
    val loadedChapters: List<ScriptureChapter> = emptyList(),
    val activeChapter: ScriptureChapter? = null,
    val bookmarkedVerseIds: Set<String> = emptySet(),
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
    val isLoadingNextChapter: Boolean = false,
    val crossReferences: Map<String, List<CrossReference>> = emptyMap(),
    val availableTags: List<com.sanctum.core.feature.scripture.domain.BookmarkTag> = emptyList(),
    val verseTagsMap: Map<String, List<com.sanctum.core.feature.scripture.domain.BookmarkTag>> = emptyMap(),
)

class ScriptureViewModel(
    private val repository: ScriptureRepository,
    private val scrollPositionRepository: ScrollPositionRepository,
    private val crossReferenceRepository: CrossReferenceRepository,
    private val bookmarkRepository: com.sanctum.core.feature.scripture.data.BookmarkRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScriptureUiState())
    val uiState: StateFlow<ScriptureUiState> = _uiState.asStateFlow()

    private var chapterJob: kotlinx.coroutines.Job? = null
    private var crossReferencesJob: kotlinx.coroutines.Job? = null

    init {
        loadChapters()
        loadBookmarks()
        loadTags()
    }

    private fun loadTags() {
        viewModelScope.launch {
            bookmarkRepository.getTags().collect { tags ->
                _uiState.value = _uiState.value.copy(availableTags = tags)
            }
        }
        viewModelScope.launch {
            bookmarkRepository.getBookmarks().collect { bookmarks ->
                val map = bookmarks.associate { it.verseId.toString() to it.tags }
                _uiState.value = _uiState.value.copy(verseTagsMap = map)
            }
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            repository.getBookmarkedVerseIds().collect { ids ->
                _uiState.value = _uiState.value.copy(bookmarkedVerseIds = ids)
            }
        }
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
        chapterJob?.cancel()
        chapterJob = viewModelScope.launch {
            // Restore scroll position
            val savedIndex = scrollPositionRepository.getScrollIndex(chapterId)
            val savedOffset = scrollPositionRepository.getScrollOffset(chapterId)

            repository.getChapter(chapterId).collect { chapter ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    activeChapter = chapter,
                    loadedChapters = listOf(chapter),
                    scrollIndex = savedIndex,
                    scrollOffset = savedOffset,
                )
                loadCrossReferencesForChapter(chapter)
            }
        }
    }

    private fun loadCrossReferencesForChapter(chapter: ScriptureChapter) {
        crossReferencesJob?.cancel()
        crossReferencesJob = viewModelScope.launch {
            val verseIds = chapter.verses.map { it.id }.toSet()
            crossReferenceRepository.getCrossReferencesForVerses(verseIds).collect { references ->
                _uiState.value = _uiState.value.copy(crossReferences = references)
            }
        }
    }

    fun loadNextChapter(chapterId: String) {
        if (_uiState.value.isLoadingNextChapter) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingNextChapter = true)
            repository.getChapter(chapterId).collect { chapter ->
                val currentLoaded = _uiState.value.loadedChapters
                if (!currentLoaded.any { it.id == chapter.id }) {
                    _uiState.value = _uiState.value.copy(
                        loadedChapters = currentLoaded + chapter,
                        isLoadingNextChapter = false,
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoadingNextChapter = false)
                }
            }
        }
    }

    fun saveScrollPosition(chapterId: String, index: Int, offset: Int) {
        scrollPositionRepository.saveScrollPosition(chapterId, index, offset)
    }

    fun toggleBookmark(verseId: String) {
        viewModelScope.launch {
            repository.toggleBookmark(verseId)
        }
    }

    fun createTag(name: String, colorHex: String) {
        viewModelScope.launch {
            bookmarkRepository.createTag(name, colorHex)
        }
    }

    fun assignTag(verseId: String, tagId: Int) {
        viewModelScope.launch {
            verseId.toIntOrNull()?.let {
                bookmarkRepository.assignTag(it, tagId)
            }
        }
    }

    fun unassignTag(verseId: String, tagId: Int) {
        viewModelScope.launch {
            verseId.toIntOrNull()?.let {
                bookmarkRepository.unassignTag(it, tagId)
            }
        }
    }
}
