package com.sanctum.core.feature.scripture.data.crossreference

import com.sanctum.core.feature.scripture.domain.crossreference.CrossReference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrossReferenceRepositoryTest {

    @Test
    fun `getCrossReferencesForVerse returns expected references`() = runTest {
        val repository = InMemoryCrossReferenceRepository()
        val reference = CrossReference(
            id = "1",
            sourceVerseId = "verse_1",
            targetVerseId = "verse_2",
            targetChapterName = "Genesis",
            targetVerseNumber = 2,
            previewText = "In the beginning...",
        )

        repository.addCrossReference("verse_1", reference)

        val references = repository.getCrossReferencesForVerse("verse_1").first()

        assertEquals(1, references.size)
        assertEquals(reference, references.first())
    }

    @Test
    fun `getCrossReferencesForVerses returns map of references for given verse ids`() = runTest {
        val repository = InMemoryCrossReferenceRepository()
        val ref1 = CrossReference("1", "verse_1", "verse_2", "Genesis", 2, "In the beginning...")
        val ref2 = CrossReference("2", "verse_2", "verse_3", "Exodus", 1, "These are the names...")

        repository.addCrossReference("verse_1", ref1)
        repository.addCrossReference("verse_2", ref2)

        val result = repository.getCrossReferencesForVerses(setOf("verse_1", "verse_2", "verse_3")).first()

        assertEquals(3, result.size)
        assertEquals(listOf(ref1), result["verse_1"])
        assertEquals(listOf(ref2), result["verse_2"])
        assertTrue(result["verse_3"]!!.isEmpty())
    }
}
