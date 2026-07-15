#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fetch ALL 66 books of the Protestant Bible from bible-api.com (KJV)
 * Outputs: assets/christianity/scripture.json
 *
 * Features:
 * - Rate limiting with 2s delay between requests
 * - Handles "Retry later" by waiting and retrying
 * - Saves progress incrementally to avoid losing data on timeout
 * - Can resume from where it left off
 */

data class Book(val name: String, val canonical: String, val chapters: Int, val offset: Int)

// All 66 books of the Protestant Bible (KJV), 1189 total chapters
val BOOKS = listOf(
    // Old Testament (39 books, 929 chapters)
    Book("genesis",       "Genesis",       50, 0),
    Book("exodus",        "Exodus",        40, 50),
    Book("leviticus",     "Leviticus",     27, 90),
    Book("numbers",       "Numbers",       36, 117),
    Book("deuteronomy",   "Deuteronomy",   34, 153),
    Book("joshua",        "Joshua",        24, 187),
    Book("judges",        "Judges",        21, 211),
    Book("ruth",          "Ruth",           4, 232),
    Book("1samuel",       "1 Samuel",      31, 236),
    Book("2samuel",       "2 Samuel",      24, 267),
    Book("1kings",        "1 Kings",       22, 291),
    Book("2kings",        "2 Kings",       25, 313),
    Book("1chronicles",   "1 Chronicles",  29, 338),
    Book("2chronicles",   "2 Chronicles",  36, 367),
    Book("ezra",          "Ezra",          10, 403),
    Book("nehemiah",      "Nehemiah",      13, 413),
    Book("esther",        "Esther",        10, 426),
    Book("job",           "Job",           42, 436),
    Book("psalms",        "Psalms",       150, 478),
    Book("proverbs",      "Proverbs",      31, 628),
    Book("ecclesiastes",  "Ecclesiastes",  12, 659),
    Book("songofsolomon", "Song of Solomon", 8, 671),
    Book("isaiah",        "Isaiah",        66, 679),
    Book("jeremiah",      "Jeremiah",      52, 745),
    Book("lamentations",  "Lamentations",   5, 797),
    Book("ezekiel",       "Ezekiel",       48, 802),
    Book("daniel",        "Daniel",        12, 850),
    Book("hosea",         "Hosea",         14, 862),
    Book("joel",          "Joel",           3, 876),
    Book("amos",          "Amos",           9, 879),
    Book("obadiah",       "Obadiah",        1, 888),
    Book("jonah",         "Jonah",          4, 889),
    Book("micah",         "Micah",          7, 893),
    Book("nahum",         "Nahum",          3, 900),
    Book("habakkuk",      "Habakkuk",       3, 903),
    Book("zephaniah",     "Zephaniah",      3, 906),
    Book("haggai",        "Haggai",         2, 909),
    Book("zechariah",     "Zechariah",     14, 911),
    Book("malachi",       "Malachi",        4, 925),
    // New Testament (27 books, 260 chapters)
    Book("matthew",       "Matthew",       28, 929),
    Book("mark",          "Mark",          16, 957),
    Book("luke",          "Luke",          24, 973),
    Book("john",          "John",          21, 997),
    Book("acts",          "Acts",          28, 1018),
    Book("romans",        "Romans",        16, 1046),
    Book("1corinthians",  "1 Corinthians", 16, 1062),
    Book("2corinthians",  "2 Corinthians", 13, 1078),
    Book("galatians",     "Galatians",      6, 1091),
    Book("ephesians",     "Ephesians",      6, 1097),
    Book("philippians",   "Philippians",    4, 1103),
    Book("colossians",    "Colossians",     4, 1107),
    Book("1thessalonians","1 Thessalonians",5, 1111),
    Book("2thessalonians","2 Thessalonians",3, 1116),
    Book("1timothy",      "1 Timothy",      6, 1119),
    Book("2timothy",      "2 Timothy",      4, 1125),
    Book("titus",         "Titus",          3, 1129),
    Book("philemon",      "Philemon",       1, 1132),
    Book("hebrews",       "Hebrews",       13, 1133),
    Book("james",         "James",          5, 1146),
    Book("1peter",        "1 Peter",        5, 1151),
    Book("2peter",        "2 Peter",        3, 1156),
    Book("1john",         "1 John",         5, 1159),
    Book("2john",         "2 John",         1, 1164),
    Book("3john",         "3 John",         1, 1165),
    Book("jude",          "Jude",           1, 1166),
    Book("revelation",    "Revelation",    22, 1167),
)

val TOTAL_CHAPTERS = BOOKS.sumOf { it.chapters } // 1189
val OUTPUT = File("assets/christianity/scripture.json")
val PROGRESS_FILE = File("assets/christianity/.bible_progress.json")

val client = HttpClient.newBuilder()
    .connectTimeout(java.time.Duration.ofSeconds(30))
    .build()

fun sleep(ms: Long) = Thread.sleep(ms)

fun fetchJson(url: String, maxRetries: Int = 5): JSONObject? {
    repeat(maxRetries) { attempt ->
        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "PrayerApp/1.0")
                .timeout(java.time.Duration.ofSeconds(30))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val body = response.body()

            if (body.trimStart().startsWith("Retry later") || response.statusCode() == 429) {
                val waitTime = 15000L * (attempt + 1)
                println("    Rate limited. Waiting ${waitTime / 1000}s...")
                sleep(waitTime)
                return@repeat
            }

            if (response.statusCode() == 200) {
                return JSONObject(body)
            }
        } catch (e: Exception) {
            println("    Attempt ${attempt + 1} failed: ${e.message}")
            sleep(5000L * (attempt + 1))
        }
    }
    return null
}

fun loadProgress(): MutableMap<String, MutableList<JSONObject>> {
    if (PROGRESS_FILE.exists()) {
        try {
            val json = JSONObject(PROGRESS_FILE.readText())
            val result = mutableMapOf<String, MutableList<JSONObject>>()
            for (key in json.keys()) {
                val arr = json.getJSONArray(key)
                val list = mutableListOf<JSONObject>()
                for (i in 0 until arr.length()) list.add(arr.getJSONObject(i))
                result[key] = list
            }
            return result
        } catch (e: Exception) {
            println("Warning: Could not load progress file: ${e.message}")
        }
    }
    return mutableMapOf()
}

fun saveProgress(progress: Map<String, List<JSONObject>>) {
    val json = JSONObject()
    for ((key, verses) in progress) {
        val arr = JSONArray()
        verses.forEach { arr.put(it) }
        json.put(key, arr)
    }
    PROGRESS_FILE.writeText(json.toString())
}

fun main() {
    println("=".repeat(60))
    println("Fetching ALL 66 Bible books from bible-api.com (KJV)")
    println("Total: $TOTAL_CHAPTERS chapters across ${BOOKS.size} books")
    println("=".repeat(60))

    val progress = loadProgress()
    val allVerses = mutableListOf<JSONObject>()

    for (book in BOOKS) {
        println("\n--- ${book.canonical} (${book.chapters} chapters, chapter_id ${book.offset + 1}-${book.offset + book.chapters}) ---")

        for (ch in 1..book.chapters) {
            val chapterId = book.offset + ch
            val key = "${book.name}_$ch"

            if (progress.containsKey(key)) {
                println("  ${book.canonical} $ch (id:$chapterId)... CACHED (${progress[key]!!.size} verses)")
                allVerses.addAll(progress[key]!!)
                continue
            }

            print("  ${book.canonical} $ch (id:$chapterId)... ")

            try {
                val data = fetchJson("https://bible-api.com/${book.name}+$ch?translation=kjv")
                if (data == null || !data.has("verses") || data.getJSONArray("verses").length() == 0) {
                    println("NO VERSES")
                    continue
                }

                val verses = data.getJSONArray("verses")
                val chapterVerses = mutableListOf<JSONObject>()
                for (i in 0 until verses.length()) {
                    val v = verses.getJSONObject(i)
                    val text = v.getString("text").trim()
                    val verse = JSONObject().apply {
                        put("chapter_id", chapterId)
                        put("verse_number", v.getInt("verse"))
                        put("original_text", text)
                        put("translated_text", text)
                    }
                    allVerses.add(verse)
                    chapterVerses.add(verse)
                }

                progress[key] = chapterVerses
                saveProgress(progress)

                println("OK (${verses.length()} verses)")
            } catch (e: Exception) {
                println("FAILED (${e.message})")
            }

            sleep(2000)
        }
    }

    println("\n${"=".repeat(60)}")
    println("Total verses collected: ${allVerses.size}")

    if (allVerses.isEmpty()) {
        println("ERROR: No verses collected. Aborting.")
        kotlin.system.exitProcess(1)
    }

    OUTPUT.parentFile.mkdirs()
    val arr = JSONArray()
    allVerses.forEach { arr.put(it) }
    OUTPUT.writeText(arr.toString(2))
    val sizeMB = OUTPUT.length() / (1024.0 * 1024.0)
    println("\nWritten: assets/christianity/scripture.json (${String.format("%.1f", sizeMB)} MB)")

    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    for (book in BOOKS) {
        val bookVerses = allVerses.filter { it.getInt("chapter_id") > book.offset && it.getInt("chapter_id") <= book.offset + book.chapters }
        val bookChapters = bookVerses.map { it.getInt("chapter_id") }.distinct()
        println("  ${book.canonical}: ${bookVerses.size} verses in ${bookChapters.size}/${book.chapters} chapters")
    }

    val missing = (1..TOTAL_CHAPTERS).filter { it !in chapters }
    if (missing.isNotEmpty()) {
        println("\nMissing chapters (${missing.size}): ${missing.joinToString(", ")}")
    } else {
        println("\nAll $TOTAL_CHAPTERS chapters present!")
    }

    if (PROGRESS_FILE.exists()) {
        PROGRESS_FILE.delete()
        println("Progress file cleaned up.")
    }

    println("\nDone!")
}

main()
