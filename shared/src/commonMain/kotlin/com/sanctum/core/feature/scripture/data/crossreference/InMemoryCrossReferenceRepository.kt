package com.sanctum.core.feature.scripture.data.crossreference

import com.sanctum.core.feature.scripture.domain.crossreference.CrossReference
import com.sanctum.core.feature.scripture.domain.crossreference.CrossReferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InMemoryCrossReferenceRepository : CrossReferenceRepository {

    private val references = mutableMapOf<String, List<CrossReference>>()

    fun addCrossReference(verseId: String, crossReference: CrossReference) {
        val currentList = references[verseId] ?: emptyList()
        references[verseId] = currentList + crossReference
    }

    override fun getCrossReferencesForVerse(verseId: String): Flow<List<CrossReference>> {
        return flowOf(references[verseId] ?: emptyList())
    }

    override fun getCrossReferencesForVerses(verseIds: Set<String>): Flow<Map<String, List<CrossReference>>> {
        val result = verseIds.associateWith { verseId ->
            references[verseId] ?: emptyList()
        }
        return flowOf(result)
    }
}
