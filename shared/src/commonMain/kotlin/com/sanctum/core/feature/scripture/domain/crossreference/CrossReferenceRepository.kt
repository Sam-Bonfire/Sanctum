package com.sanctum.core.feature.scripture.domain.crossreference

import kotlinx.coroutines.flow.Flow

interface CrossReferenceRepository {
    fun getCrossReferencesForVerse(verseId: String): Flow<List<CrossReference>>
    fun getCrossReferencesForVerses(verseIds: Set<String>): Flow<Map<String, List<CrossReference>>>
}
