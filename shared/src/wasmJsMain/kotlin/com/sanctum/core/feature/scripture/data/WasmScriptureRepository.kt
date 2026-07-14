package com.sanctum.core.feature.scripture.data

import com.sanctum.core.feature.scripture.domain.ScriptureBook
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sanctum.shared.generated.resources.Res

@Serializable
private data class VerseJson(
    val chapter_id: Int,
    val verse_number: Int,
    val original_text: String,
    val translated_text: String,
)

private sealed class ScriptureLoadState {
    data object Loading : ScriptureLoadState()
    data object Empty : ScriptureLoadState()
    data class Error(val cause: Throwable) : ScriptureLoadState()
    data class Success(val chapters: Map<Int, List<VerseJson>>) : ScriptureLoadState()
}

/**
 * Real web implementation of ScriptureRepository for WasmJs.
 * Fetches the bundled scripture.json asset at runtime using Compose Resources,
 * parses it, and serves it from an in-memory StateFlow.
 * On any parse failure the state transitions to Error —
 * no hardcoded fallback content is used.
 */
class WasmScriptureRepository : ScriptureRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val loadState = MutableStateFlow<ScriptureLoadState>(ScriptureLoadState.Loading)

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun ensureLoaded(religionId: String) {
        if (loadState.value is ScriptureLoadState.Success) return
        loadState.value = ScriptureLoadState.Loading
        try {
            val bytes = Res.readBytes("files/$religionId/scripture.json")
            val text = bytes.decodeToString()
            val verses = json.decodeFromString<List<VerseJson>>(text)
            if (verses.isEmpty()) {
                loadState.value = ScriptureLoadState.Empty
            } else {
                loadState.value = ScriptureLoadState.Success(verses.groupBy { it.chapter_id })
            }
        } catch (e: Exception) {
            loadState.value = ScriptureLoadState.Error(e)
        }
    }

    override suspend fun getDailyVerse(religionId: String): ScriptureVerse {
        ensureLoaded(religionId)
        val state = loadState.value
        if (state is ScriptureLoadState.Success) {
            val firstVerse = state.chapters[1]?.firstOrNull()
                ?: throw NoSuchElementException("No verses available in chapter 1 for $religionId.")
            return ScriptureVerse(
                id = "${firstVerse.chapter_id}_${firstVerse.verse_number}",
                number = firstVerse.verse_number,
                originalText = firstVerse.original_text,
                translation = firstVerse.translated_text,
            )
        }
        throw IllegalStateException("Scripture data unavailable: $state")
    }

    override suspend fun getBook(religionId: String, bookId: String): ScriptureBook {
        ensureLoaded(religionId)
        val state = loadState.value
        if (state is ScriptureLoadState.Success) {
            val chapters = state.chapters.map { (chapterId, verses) ->
                ScriptureChapter(
                    id = chapterId.toString(),
                    number = chapterId,
                    title = null,
                    verses = verses.map { v ->
                        ScriptureVerse(
                            id = "${v.chapter_id}_${v.verse_number}",
                            number = v.verse_number,
                            originalText = v.original_text,
                            translation = v.translated_text,
                        )
                    },
                )
            }.sortedBy { it.number }
            return ScriptureBook(id = bookId, title = religionId, subtitle = null, chapters = chapters)
        }
        throw IllegalStateException("Scripture data unavailable: $state")
    }

    override fun getChapters(): Flow<List<ScriptureChapter>> =
        loadState.asStateFlow().map { state ->
            if (state is ScriptureLoadState.Success) {
                state.chapters.keys.sorted().map { chapterId ->
                    ScriptureChapter(id = chapterId.toString(), number = chapterId, title = null, verses = emptyList())
                }
            } else {
                emptyList()
            }
        }

    override fun getChapter(chapterId: String): Flow<ScriptureChapter> {
        val idInt = chapterId.toIntOrNull() ?: 1
        return loadState.asStateFlow().map { state ->
            if (state is ScriptureLoadState.Success) {
                val verses = state.chapters[idInt] ?: emptyList()
                ScriptureChapter(
                    id = chapterId,
                    number = idInt,
                    title = null,
                    verses = verses.map { v ->
                        ScriptureVerse(
                            id = "${v.chapter_id}_${v.verse_number}",
                            number = v.verse_number,
                            originalText = v.original_text,
                            translation = v.translated_text,
                        )
                    },
                )
            } else {
                ScriptureChapter(id = chapterId, number = idInt, title = null, verses = emptyList())
            }
        }
    }
}
