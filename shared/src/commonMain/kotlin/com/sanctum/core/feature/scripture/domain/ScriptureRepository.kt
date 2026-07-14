package com.sanctum.core.feature.scripture.domain

import kotlinx.coroutines.flow.Flow

interface ScriptureRepository {
    fun getVersesBySurah(surahId: Int): Flow<List<Verse>>
    suspend fun insertVerses(verses: List<Verse>)
}
