#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import org.json.JSONObject
import org.json.JSONArray
import java.io.File

/**
 * Rebuild Christianity scripture.json from progress cache + API fetch for missing books.
 *
 * Reads the cached bible progress data, rebuilds with correct sequential chapter_ids,
 * and fetches any missing chapters from bible-api.com.
 */

data class BookSpec(val name: String, val canonical: String, val chapters: Int)

val ALL_BOOKS = listOf(
    // OT
    BookSpec("genesis", "Genesis", 50),
    BookSpec("exodus", "Exodus", 40),
    BookSpec("leviticus", "Leviticus", 27),
    BookSpec("numbers", "Numbers", 36),
    BookSpec("deuteronomy", "Deuteronomy", 34),
    BookSpec("joshua", "Joshua", 24),
    BookSpec("judges", "Judges", 21),
    BookSpec("ruth", "Ruth", 4),
    BookSpec("1samuel", "1 Samuel", 31),
    BookSpec("2samuel", "2 Samuel", 24),
    BookSpec("1kings", "1 Kings", 22),
    BookSpec("2kings", "2 Kings", 25),
    BookSpec("1chronicles", "1 Chronicles", 29),
    BookSpec("2chronicles", "2 Chronicles", 36),
    BookSpec("ezra", "Ezra", 10),
    BookSpec("nehemiah", "Nehemiah", 13),
    BookSpec("esther", "Esther", 10),
    BookSpec("job", "Job", 42),
    BookSpec("psalms", "Psalms", 150),
    BookSpec("proverbs", "Proverbs", 31),
    BookSpec("ecclesiastes", "Ecclesiastes", 12),
    BookSpec("songofsolomon", "Song of Solomon", 8),
    BookSpec("isaiah", "Isaiah", 66),
    BookSpec("jeremiah", "Jeremiah", 52),
    BookSpec("lamentations", "Lamentations", 5),
    BookSpec("ezekiel", "Ezekiel", 48),
    BookSpec("daniel", "Daniel", 12),
    BookSpec("hosea", "Hosea", 14),
    BookSpec("joel", "Joel", 3),
    BookSpec("amos", "Amos", 9),
    BookSpec("obadiah", "Obadiah", 1),
    BookSpec("jonah", "Jonah", 4),
    BookSpec("micah", "Micah", 7),
    BookSpec("nahum", "Nahum", 3),
    BookSpec("habakkuk", "Habakkuk", 3),
    BookSpec("zephaniah", "Zephaniah", 3),
    BookSpec("haggai", "Haggai", 2),
    BookSpec("zechariah", "Zechariah", 14),
    BookSpec("malachi", "Malachi", 4),
    // NT
    BookSpec("matthew", "Matthew", 28),
    BookSpec("mark", "Mark", 16),
    BookSpec("luke", "Luke", 24),
    BookSpec("john", "John", 21),
    BookSpec("acts", "Acts", 28),
    BookSpec("romans", "Romans", 16),
    BookSpec("1corinthians", "1 Corinthians", 16),
    BookSpec("2corinthians", "2 Corinthians", 13),
    BookSpec("galatians", "Galatians", 6),
    BookSpec("ephesians", "Ephesians", 6),
    BookSpec("philippians", "Philippians", 4),
    BookSpec("colossians", "Colossians", 4),
    BookSpec("1thessalonians", "1 Thessalonians", 5),
    BookSpec("2thessalonians", "2 Thessalonians", 3),
    BookSpec("1timothy", "1 Timothy", 6),
    BookSpec("2timothy", "2 Timothy", 4),
    BookSpec("titus", "Titus", 3),
    BookSpec("philemon", "Philemon", 1),
    BookSpec("hebrews", "Hebrews", 13),
    BookSpec("james", "James", 5),
    BookSpec("1peter", "1 Peter", 5),
    BookSpec("2peter", "2 Peter", 3),
    BookSpec("1john", "1 John", 5),
    BookSpec("2john", "2 John", 1),
    BookSpec("3john", "3 John", 1),
    BookSpec("jude", "Jude", 1),
    BookSpec("revelation", "Revelation", 22),
)

val EXPECTED_CHAPTERS = ALL_BOOKS.sumOf { it.chapters } // 1189
val OUTPUT_FILE = File("assets/christianity/scripture.json")
val PROGRESS_FILE = File("assets/christianity/.bible_progress.json")

val client = java.net.http.HttpClient.newBuilder()
    .connectTimeout(java.time.Duration.ofSeconds(10))
    .build()

fun sleep(ms: Long) = Thread.sleep(ms)

fun fetchJson(url: String): JSONObject? {
    val request = java.net.http.HttpRequest.newBuilder()
        .uri(java.net.URI.create(url))
        .timeout(java.time.Duration.ofSeconds(30))
        .header("Accept", "application/json")
        .GET()
        .build()

    val response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString())
    val body = response.body()

    if (response.statusCode() == 429) {
        println("  Rate limited, waiting 15s...")
        sleep(15000)
        return null
    }
    if (response.statusCode() != 200) {
        println("  HTTP ${response.statusCode()}")
        return null
    }

    return JSONObject(body)
}

fun loadProgress(): MutableMap<String, MutableList<JSONObject>> {
    val result = mutableMapOf<String, MutableList<JSONObject>>()
    if (!PROGRESS_FILE.exists()) return result

    try {
        val json = JSONObject(PROGRESS_FILE.readText())
        var loaded = 0
        for (key in json.keys()) {
            val arr = json.getJSONArray(key)
            val list = mutableListOf<JSONObject>()
            for (i in 0 until arr.length()) {
                list.add(arr.getJSONObject(i))
            }
            result[key] = list
            loaded++
        }
        println("Loaded $loaded cached chapters from progress file")
    } catch (e: Exception) {
        println("Warning: Could not load progress: ${e.message}")
    }
    return result
}

fun saveProgress(progress: Map<String, List<JSONObject>>) {
    val json = JSONObject()
    for ((key, verses) in progress) {
        val arr = JSONArray()
        verses.forEach { arr.put(it) }
        json.put(key, arr)
    }
    PROGRESS_FILE.parentFile.mkdirs()
    PROGRESS_FILE.writeText(json.toString())
}

fun main() {
    println("=".repeat(60))
    println("Rebuild Christianity scripture.json")
    println("=".repeat(60))

    val progress = loadProgress()
    val allData = mutableMapOf<String, MutableList<JSONObject>>()
    var totalFetched = 0
    var totalFromCache = 0
    var totalMissing = 0

    // Step 1: Load from cache or fetch from API
    for (book in ALL_BOOKS) {
        println("\n--- ${book.canonical} (${book.chapters} chapters) ---")
        var bookFetched = 0

        for (ch in 1..book.chapters) {
            val key = "${book.name}_$ch"

            if (progress.containsKey(key)) {
                // Use cached data
                if (!allData.containsKey(book.name)) allData[book.name] = mutableListOf()
                allData[book.name]!!.addAll(progress[key]!!)
                totalFromCache++
                continue
            }

            // Fetch from API
            try {
                val data = fetchJson("https://bible-api.com/${book.name}+$ch?translation=kjv")
                if (data == null || !data.has("verses") || data.getJSONArray("verses").length() == 0) {
                    println("  ${book.canonical} $ch... NO VERSES")
                    totalMissing++
                    continue
                }

                val verses = data.getJSONArray("verses")
                val chapterVerses = mutableListOf<JSONObject>()
                for (i in 0 until verses.length()) {
                    val v = verses.getJSONObject(i)
                    val text = v.getString("text").trim()
                    chapterVerses.add(JSONObject().apply {
                        put("chapter_id", ch)
                        put("verse_number", v.getInt("verse"))
                        put("original_text", text)
                        put("translated_text", text)
                    })
                }

                if (!allData.containsKey(book.name)) allData[book.name] = mutableListOf()
                allData[book.name]!!.addAll(chapterVerses)

                progress[key] = chapterVerses
                saveProgress(progress)

                bookFetched++
                totalFetched++
                println("  ${book.canonical} $ch... OK (${verses.length()} verses)")
            } catch (e: Exception) {
                println("  ${book.canonical} $ch... FAILED (${e.message})")
                totalMissing++
            }

            sleep(2000)
        }
        if (bookFetched > 0) println("  Fetched $bookFetched chapters from API")
    }

    println("\n${"=".repeat(60)}")
    println("Build complete! Now writing output...")
    println("  From cache: $totalFromCache chapters")
    println("  Fetched: $totalFetched chapters")
    println("  Missing: $totalMissing chapters")

    // Step 2: Write output with sequential chapter_ids
    val allVerses = mutableListOf<JSONObject>()
    var offset = 0

    for (book in ALL_BOOKS) {
        val verses = allData[book.name] ?: emptyList()
        for (verse in verses) {
            allVerses.add(JSONObject().apply {
                put("chapter_id", verse.getInt("chapter_id") + offset)
                put("verse_number", verse.getInt("verse_number"))
                put("original_text", verse.getString("original_text"))
                put("translated_text", verse.getString("translated_text"))
            })
        }
        offset += book.chapters
    }

    OUTPUT_FILE.parentFile.mkdirs()
    val arr = JSONArray()
    allVerses.forEach { arr.put(it) }
    OUTPUT_FILE.writeText(arr.toString(2))

    val sizeMB = OUTPUT_FILE.length() / (1024.0 * 1024.0)
    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()

    println("\nWritten: ${OUTPUT_FILE.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${allVerses.size} verses)")
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")
    println("Expected: $EXPECTED_CHAPTERS chapters")
    println("Contiguous: ${chapters == (1..chapters.last()).toList()}")

    // Per-book summary
    offset = 0
    for (book in ALL_BOOKS) {
        val verses = allData[book.name] ?: emptyList()
        val bookChapters = verses.map { it.getInt("chapter_id") }.distinct().size
        val status = if (bookChapters == book.chapters) "OK" else "INCOMPLETE ($bookChapters/${book.chapters})"
        println("  ${book.canonical}: ${verses.size} verses in $bookChapters chapters [$status]")
        offset += book.chapters
    }

    println("\nDone!")
}

main()
