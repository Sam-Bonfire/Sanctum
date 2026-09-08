package com.sanctum.core.feature.reading.domain

/** Total Juz in the Quran. */
const val KHATAM_JUZ_COUNT = 30

/**
 * Distributes all 30 Juz across [dayCount] days for a Khatam (full-Quran
 * reading cycle). Extra Juz go to the earliest days so the plan always ends
 * exactly on Juz 30 (e.g. 30 days -> 1 Juz/day, 7 days -> 5/4 Juz/day).
 *
 * Each ref is either "Juz X" or a "Juz X-Y" range sized for one checkpoint.
 */
fun buildKhatamRefs(dayCount: Int): List<String> {
    require(dayCount in 1..KHATAM_JUZ_COUNT) { "dayCount must be 1..$KHATAM_JUZ_COUNT" }
    val refs = ArrayList<String>(dayCount)
    var juz = 1
    repeat(dayCount) { day ->
        val remaining = KHATAM_JUZ_COUNT - juz + 1
        val daysLeft = dayCount - day
        val take = (remaining + daysLeft - 1) / daysLeft
        val end = juz + take - 1
        refs.add(if (take == 1) "Juz $juz" else "Juz $juz-$end")
        juz = end + 1
    }
    return refs
}
