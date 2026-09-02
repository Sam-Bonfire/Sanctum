package com.sanctum.core.feature.scripture.domain

import com.sanctum.core.feature.scripture.data.ScriptureRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DailyVerseManager(
    private val repository: ScriptureRepository,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    suspend fun getDailyVerse(religionId: String): ScriptureVerse? {
        val now = clock.now()
        val localDate = now.toLocalDateTime(timeZone).date
        val dayOfYear = localDate.dayOfYear

        // For deterministic rotation, fetch the first chapter (or a default set of verses)
        // Since getChapters/getChapter return Flow, we'll try to fetch chapter "1"
        val chapter = repository.getChapter("1").firstOrNull() ?: return null

        val verses = chapter.verses
        if (verses.isEmpty()) {
            return null
        }

        val verseIndex = dayOfYear % verses.size
        return verses[verseIndex]
    }
}
