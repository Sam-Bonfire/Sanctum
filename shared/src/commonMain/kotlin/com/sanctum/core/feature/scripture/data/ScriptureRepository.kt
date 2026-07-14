package com.sanctum.core.feature.scripture.data

import com.sanctum.core.feature.scripture.domain.ScriptureBook
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import kotlinx.coroutines.flow.Flow

interface ScriptureRepository {
    suspend fun getDailyVerse(religionId: String): ScriptureVerse
    suspend fun getBook(religionId: String, bookId: String): ScriptureBook
    fun getChapters(): Flow<List<ScriptureChapter>>
    fun getChapter(chapterId: String): Flow<ScriptureChapter>
}
