package com.sanctum.core.feature.scripture.data

import com.sanctum.core.core.database.ScriptureDao
import com.sanctum.core.core.database.VerseEntity
import com.sanctum.core.feature.scripture.domain.ScriptureBook
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * Room-backed ScriptureRepository for Android and iOS.
 *
 * The database is pre-populated via `createFromAsset("prayer.db")` at build time.
 * No seeding or mock data is performed here — if the DB is empty it means the
 * asset was not bundled correctly, which is a build-time error, not a runtime one
 * to silently paper over.
 */
class RoomScriptureRepository(
    private val scriptureDao: ScriptureDao,
    private val userDataDao: com.sanctum.core.core.database.UserDataDao,
) : ScriptureRepository {

    override suspend fun getDailyVerse(religionId: String): ScriptureVerse {
        val verses = scriptureDao.getVersesByChapter(1).firstOrNull()
        val verse = verses?.firstOrNull()
            ?: throw NoSuchElementException(
                "No verses found in chapter 1. Ensure prayer.db is bundled in mobileMain/assets.",
            )
        return verse.toDomain()
    }

    override suspend fun getBook(religionId: String, bookId: String): ScriptureBook {
        val chapterIds = scriptureDao.getChapterIds().firstOrNull() ?: emptyList()
        if (chapterIds.isEmpty()) {
            throw NoSuchElementException(
                "No chapters found. Ensure prayer.db is bundled in mobileMain/assets.",
            )
        }
        val chapters = chapterIds.map { id ->
            val verses = scriptureDao.getVersesByChapter(id).firstOrNull() ?: emptyList()
            ScriptureChapter(
                id = id.toString(),
                number = id,
                title = null,
                verses = verses.map { it.toDomain() },
            )
        }
        return ScriptureBook(id = bookId, title = bookId, subtitle = null, chapters = chapters)
    }

    override fun getChapters(): Flow<List<ScriptureChapter>> =
        scriptureDao.getChapterIds().map { chapterIds ->
            chapterIds.map { id ->
                ScriptureChapter(id = id.toString(), number = id, title = null, verses = emptyList())
            }
        }

    override fun getChapter(chapterId: String): Flow<ScriptureChapter> {
        val idInt = chapterId.toIntOrNull() ?: 1
        return scriptureDao.getVersesByChapter(idInt).map { verses ->
            ScriptureChapter(
                id = chapterId,
                number = idInt,
                title = null,
                verses = verses.map { it.toDomain() },
            )
        }
    }

    private fun VerseEntity.toDomain() = ScriptureVerse(
        id = id.toString(),
        number = verseNumber,
        originalText = originalText,
        translation = translatedText,
    )

    override fun getBookmarkedVerseIds(): Flow<Set<String>> =
        userDataDao.getAllBookmarks().map { list ->
            list.map { it.verseId.toString() }.toSet()
        }

    override suspend fun toggleBookmark(verseId: String) {
        val idInt = verseId.toIntOrNull() ?: return
        val all = userDataDao.getAllBookmarks().firstOrNull() ?: emptyList()
        val exists = all.any { it.verseId == idInt }
        if (exists) {
            userDataDao.removeBookmark(idInt)
        } else {
            userDataDao.addBookmark(
                com.sanctum.core.core.database.BookmarkEntity(
                    verseId = idInt,
                    timestampMs = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }
}
