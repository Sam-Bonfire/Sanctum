package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.scripture.data.ScriptureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DailyVerseManagerTest {

    private val mockVerses = listOf(
        ScriptureVerse(id = "1", number = 1, originalText = "Original 1", translation = "Translation 1"),
        ScriptureVerse(id = "2", number = 2, originalText = "Original 2", translation = "Translation 2"),
        ScriptureVerse(id = "3", number = 3, originalText = "Original 3", translation = "Translation 3"),
    )

    private val mockChapter = ScriptureChapter(
        id = "1",
        number = 1,
        title = "Mock Chapter",
        verses = mockVerses,
    )

    private val mockRepository = object : ScriptureRepository {
        override suspend fun getDailyVerse(religionId: String): ScriptureVerse = mockVerses.first()
        override suspend fun getBook(religionId: String, bookId: String): ScriptureBook = ScriptureBook("1", "1", null, listOf(mockChapter))
        override fun getChapters(): Flow<List<ScriptureChapter>> = flowOf(listOf(mockChapter))
        override fun getChapter(chapterId: String): Flow<ScriptureChapter> = if (chapterId == "1") flowOf(mockChapter) else flowOf(ScriptureChapter("2", 2, null, emptyList()))
        override fun getBookmarkedVerseIds(): Flow<Set<String>> = flowOf(emptySet())
        override suspend fun toggleBookmark(verseId: String) {}
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }

    @Test
    fun `getDailyVerse returns a deterministic verse based on day of year`() = runTest {
        // January 1, 2023 12:00:00 UTC - Day of Year = 1
        val clockDay1 = FixedClock(Instant.parse("2023-01-01T12:00:00Z"))
        val managerDay1 = DailyVerseManager(mockRepository, clockDay1, TimeZone.UTC)
        val verseDay1 = managerDay1.getDailyVerse("flavor")
        assertNotNull(verseDay1)
        // 1 % 3 = 1 -> index 1 -> verse 2
        assertEquals(mockVerses[1].id, verseDay1.id)

        // January 2, 2023 12:00:00 UTC - Day of Year = 2
        val clockDay2 = FixedClock(Instant.parse("2023-01-02T12:00:00Z"))
        val managerDay2 = DailyVerseManager(mockRepository, clockDay2, TimeZone.UTC)
        val verseDay2 = managerDay2.getDailyVerse("flavor")
        assertNotNull(verseDay2)
        // 2 % 3 = 2 -> index 2 -> verse 3
        assertEquals(mockVerses[2].id, verseDay2.id)

        // January 3, 2023 12:00:00 UTC - Day of Year = 3
        val clockDay3 = FixedClock(Instant.parse("2023-01-03T12:00:00Z"))
        val managerDay3 = DailyVerseManager(mockRepository, clockDay3, TimeZone.UTC)
        val verseDay3 = managerDay3.getDailyVerse("flavor")
        assertNotNull(verseDay3)
        // 3 % 3 = 0 -> index 0 -> verse 1
        assertEquals(mockVerses[0].id, verseDay3.id)
    }

    @Test
    fun `getDailyVerse returns null if no verses found`() = runTest {
        val emptyRepository = object : ScriptureRepository {
            override suspend fun getDailyVerse(religionId: String): ScriptureVerse = mockVerses.first()
            override suspend fun getBook(religionId: String, bookId: String): ScriptureBook = ScriptureBook("1", "1", null, emptyList())
            override fun getChapters(): Flow<List<ScriptureChapter>> = flowOf(emptyList())
            override fun getChapter(chapterId: String): Flow<ScriptureChapter> = flowOf(ScriptureChapter("1", 1, null, emptyList()))
            override fun getBookmarkedVerseIds(): Flow<Set<String>> = flowOf(emptySet())
            override suspend fun toggleBookmark(verseId: String) {}
        }
        val clock = FixedClock(Instant.parse("2023-01-01T12:00:00Z"))
        val manager = DailyVerseManager(emptyRepository, clock, TimeZone.UTC)
        val verse = manager.getDailyVerse("flavor")
        assertNull(verse)
    }
}
