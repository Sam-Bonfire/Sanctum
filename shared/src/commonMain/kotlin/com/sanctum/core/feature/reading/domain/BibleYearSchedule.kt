package com.sanctum.core.feature.reading.domain

/** Global chapters (1..1189) in the bundled Christianity scripture text. */
const val BIBLE_CHAPTER_COUNT = 1189

/** Default Bible-in-a-Year pacing. */
const val BIBLE_YEAR_DAY_COUNT = 365

/**
 * Distributes all [BIBLE_CHAPTER_COUNT] chapters across [dayCount] days.
 * Extra chapters go to the earliest days so the plan always ends exactly on
 * chapter 1189 (365 days -> ~3-4 chapters/day).
 *
 * Refs use the bundled text's global chapter numbers ("Chapter X" or
 * "Chapters X-Y"); book-name grouping needs the canon decision (S-315).
 * Range refs are display pacing until reader navigation consumes them.
 */
fun buildBibleYearRefs(dayCount: Int = BIBLE_YEAR_DAY_COUNT): List<String> {
    require(dayCount in 1..BIBLE_CHAPTER_COUNT) { "dayCount must be 1..$BIBLE_CHAPTER_COUNT" }
    val refs = ArrayList<String>(dayCount)
    var chapter = 1
    repeat(dayCount) { day ->
        val remaining = BIBLE_CHAPTER_COUNT - chapter + 1
        val daysLeft = dayCount - day
        val take = (remaining + daysLeft - 1) / daysLeft
        val end = chapter + take - 1
        refs.add(if (take == 1) "Chapter $chapter" else "Chapters $chapter-$end")
        chapter = end + 1
    }
    return refs
}
