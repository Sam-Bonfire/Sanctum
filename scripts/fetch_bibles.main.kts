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
 * Unified Bible & Tanakh fetcher from bible-api.com (KJV)
 *
 * Fetches each unique book ONCE, then splits output into:
 *   - assets/christianity/scripture.json  (66 books, 1189 chapters — Protestant Bible)
 *   - assets/jewish/scripture.json        (24 books, 929 chapters — Tanakh)
 *
 * Features:
 * - Rate limiting with 2s delay between requests
 * - Handles "Retry later" by waiting and retrying
 * - Saves progress incrementally (resumable)
 * - Shared progress file — no duplicate API calls
 *
 * Usage:
 *   kotlin scripts/fetch_bibles.main.kts
 *   kotlin scripts/fetch_bibles.main.kts --christianity   # Christianity only
 *   kotlin scripts/fetch_bibles.main.kts --jewish          # Jewish only
 */

// ==================== DATA MODEL ====================

data class Book(
    val name: String,           // bible-api.com URL slug
    val canonical: String,      // Display name
    val chapters: Int,          // Chapter count
    val section: String,        // "ot" or "nt"
    val jewishName: String? = null  // Non-null = included in Tanakh
)

// All unique books needed across both Christianity and Judaism.
// Some books appear in both (OT books), fetched only once.
val ALL_BOOKS = listOf(
    // ---- TORAH / Pentateuch (shared) ----
    Book("genesis",       "Genesis",       50, "torah", "genesis"),
    Book("exodus",        "Exodus",        40, "torah", "exodus"),
    Book("leviticus",     "Leviticus",     27, "torah", "leviticus"),
    Book("numbers",       "Numbers",       36, "torah", "numbers"),
    Book("deuteronomy",   "Deuteronomy",   34, "torah", "deuteronomy"),

    // ---- NEVI'IM / Prophets (shared — Christianity uses individual books, Tanakh groups some) ----
    Book("joshua",        "Joshua",        24, "prophets", "joshua"),
    Book("judges",        "Judges",        21, "prophets", "judges"),
    Book("ruth",          "Ruth",           4, "writings", "ruth"),
    Book("1samuel",       "1 Samuel",      31, "prophets", "samuel"),   // Jewish: combined as "Samuel"
    Book("2samuel",       "2 Samuel",      24, "prophets"),              // Christian-only split
    Book("1kings",        "1 Kings",       22, "prophets", "kings"),     // Jewish: combined as "Kings"
    Book("2kings",        "2 Kings",       25, "prophets"),
    Book("1chronicles",   "1 Chronicles",  29, "writings", "chronicles"), // Jewish: combined as "Chronicles"
    Book("2chronicles",   "2 Chronicles",  36, "writings"),
    Book("ezra",          "Ezra",          10, "writings", "ezra"),      // Jewish: Ezra-Nehemiah combined
    Book("nehemiah",      "Nehemiah",      13, "writings"),
    Book("esther",        "Esther",        10, "writings", "esther"),
    Book("job",           "Job",           42, "writings", "job"),
    Book("psalms",        "Psalms",       150, "writings", "psalms"),
    Book("proverbs",      "Proverbs",      31, "writings", "proverbs"),
    Book("ecclesiastes",  "Ecclesiastes",  12, "writings", "ecclesiastes"),
    Book("songofsolomon", "Song of Solomon", 8, "writings", "songofsongs"),
    Book("isaiah",        "Isaiah",        66, "prophets", "isaiah"),
    Book("jeremiah",      "Jeremiah",      52, "prophets", "jeremiah"),
    Book("lamentations",  "Lamentations",   5, "writings", "lamentations"),
    Book("ezekiel",       "Ezekiel",       48, "prophets", "ezekiel"),
    Book("daniel",        "Daniel",        12, "writings", "daniel"),
    Book("hosea",         "Hosea",         14, "prophets", "hosea"),
    Book("joel",          "Joel",           3, "prophets", "joel"),
    Book("amos",          "Amos",           9, "prophets", "amos"),
    Book("obadiah",       "Obadiah",        1, "prophets", "obadiah"),
    Book("jonah",         "Jonah",          4, "prophets", "jonah"),
    Book("micah",         "Micah",          7, "prophets", "micah"),
    Book("nahum",         "Nahum",          3, "prophets", "nahum"),
    Book("habakkuk",      "Habakkuk",       3, "prophets", "habakkuk"),
    Book("zephaniah",     "Zephaniah",      3, "prophets", "zephaniah"),
    Book("haggai",        "Haggai",         2, "prophets", "haggai"),
    Book("zechariah",     "Zechariah",     14, "prophets", "zechariah"),
    Book("malachi",       "Malachi",        4, "prophets", "malachi"),

    // ---- NEW TESTAMENT (Christianity only) ----
    Book("matthew",       "Matthew",       28, "nt"),
    Book("mark",          "Mark",          16, "nt"),
    Book("luke",          "Luke",          24, "nt"),
    Book("john",          "John",          21, "nt"),
    Book("acts",          "Acts",          28, "nt"),
    Book("romans",        "Romans",        16, "nt"),
    Book("1corinthians",  "1 Corinthians", 16, "nt"),
    Book("2corinthians",  "2 Corinthians", 13, "nt"),
    Book("galatians",     "Galatians",      6, "nt"),
    Book("ephesians",     "Ephesians",      6, "nt"),
    Book("philippians",   "Philippians",    4, "nt"),
    Book("colossians",    "Colossians",     4, "nt"),
    Book("1thessalonians","1 Thessalonians", 5, "nt"),
    Book("2thessalonians","2 Thessalonians", 3, "nt"),
    Book("1timothy",      "1 Timothy",      6, "nt"),
    Book("2timothy",      "2 Timothy",      4, "nt"),
    Book("titus",         "Titus",          3, "nt"),
    Book("philemon",      "Philemon",       1, "nt"),
    Book("hebrews",       "Hebrews",       13, "nt"),
    Book("james",         "James",          5, "nt"),
    Book("1peter",        "1 Peter",        5, "nt"),
    Book("2peter",        "2 Peter",        3, "nt"),
    Book("1john",         "1 John",         5, "nt"),
    Book("2john",         "2 John",         1, "nt"),
    Book("3john",         "3 John",         1, "nt"),
    Book("jude",          "Jude",           1, "nt"),
    Book("revelation",    "Revelation",    22, "nt"),
)

// Christianity books (all 66, in order)
val CHRISTIANITY_BOOKS = ALL_BOOKS // all books

// Jewish/Tanakh books (24 books in canonical order, using combined names)
// We build this from the shared books that have jewishName set
val JEWISH_BOOKS_ORDERED = ALL_BOOKS.filter { it.jewishName != null }

val ASSETS_DIR = File(System.getProperty("user.dir")).let { dir ->
    // If run from scripts/ dir, go up one level to find assets/
    if (dir.name == "scripts") File(dir.parentFile, "assets") else File(dir, "assets")
}
val CHRISTIANITY_OUTPUT = File(ASSETS_DIR, "christianity/scripture.json")
val JEWISH_OUTPUT = File(ASSETS_DIR, "jewish/scripture.json")
val PROGRESS_FILE = File(ASSETS_DIR, ".bible_progress.json")
val LEGACY_CHRIS_PROGRESS = File(ASSETS_DIR, "christianity/.bible_progress.json")
val LEGACY_JEWISH_PROGRESS = File(ASSETS_DIR, "jewish/.jewish_progress.json")

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
            println("    HTTP ${response.statusCode()} for $url")
        } catch (e: Exception) {
            println("    Attempt ${attempt + 1} failed: ${e.message}")
            sleep(5000L * (attempt + 1))
        }
    }
    return null
}

fun loadProgress(): MutableMap<String, MutableList<JSONObject>> {
    val result = mutableMapOf<String, MutableList<JSONObject>>()

    // Load from all known progress files and merge
    // The unified script uses assets/.bible_progress.json
    // The old separate scripts used assets/christianity/.bible_progress.json
    // and assets/jewish/.jewish_progress.json
    val progressFiles = listOf(
        PROGRESS_FILE,
        LEGACY_CHRIS_PROGRESS,
        LEGACY_JEWISH_PROGRESS
    )

    for (file in progressFiles) {
        if (file.exists()) {
            try {
                println("Loading cached data from: ${file.absolutePath}")
                val json = JSONObject(file.readText())
                var loaded = 0
                for (key in json.keys()) {
                    if (result.containsKey(key)) continue // skip duplicates
                    val arr = json.getJSONArray(key)
                    val list = mutableListOf<JSONObject>()
                    for (i in 0 until arr.length()) list.add(arr.getJSONObject(i))
                    result[key] = list
                    loaded++
                }
                println("  -> Loaded $loaded unique chapter entries")
            } catch (e: Exception) {
                println("Warning: Could not load ${file.name}: ${e.message}")
            }
        }
    }

    println("Total cached chapters across all sources: ${result.size}\n")

    // Save merged progress to unified file so future runs start fast
    if (result.isNotEmpty() && !PROGRESS_FILE.exists()) {
        saveProgress(result)
        println("Saved merged cache to ${PROGRESS_FILE.absolutePath}\n")
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
    PROGRESS_FILE.writeText(json.toString())
}

fun writeOutput(outputFile: File, bookOffsets: Map<String, Int>, books: List<Book>, allData: Map<String, List<JSONObject>>) {
    val allVerses = mutableListOf<JSONObject>()

    for (book in books) {
        val bookOffset = bookOffsets[book.name] ?: 0
        val verses = allData[book.name] ?: emptyList()
        for (verse in verses) {
            allVerses.add(JSONObject().apply {
                put("chapter_id", verse.getInt("chapter_id") + bookOffset)
                put("verse_number", verse.getInt("verse_number"))
                put("original_text", verse.getString("original_text"))
                put("translated_text", verse.getString("translated_text"))
            })
        }
    }

    outputFile.parentFile.mkdirs()
    val arr = JSONArray()
    allVerses.forEach { arr.put(it) }
    outputFile.writeText(arr.toString(2))

    val sizeMB = outputFile.length() / (1024.0 * 1024.0)
    println("\nWritten: ${outputFile.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${allVerses.size} verses)")

    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    // Per-book summary
    for (book in books) {
        val bookOffset = bookOffsets[book.name] ?: 0
        val bookVerses = allData[book.name] ?: emptyList()
        println("  ${book.canonical}: ${bookVerses.size} verses in ${book.chapters} chapters")
    }
}

fun main() {
    val mode = args.firstOrNull() // "--christianity" or "--jewish"

    println("=".repeat(60))
    println("Unified Bible/Tanakh Fetcher (bible-api.com KJV)")
    println("=".repeat(60))

    // Step 1: Fetch all unique books (no duplicates!)
    val progress = loadProgress()
    val allData = mutableMapOf<String, MutableList<JSONObject>>()
    var totalChaptersToFetch = 0

    for (book in ALL_BOOKS) {
        totalChaptersToFetch += book.chapters
    }
    println("Total unique books: ${ALL_BOOKS.size}")
    println("Total unique chapters to fetch: $totalChaptersToFetch\n")

    for (book in ALL_BOOKS) {
        println("\n--- ${book.canonical} (${book.chapters} chapters) ---")

        for (ch in 1..book.chapters) {
            val key = "${book.name}_$ch"

            if (progress.containsKey(key)) {
                val cached = progress[key]!!
                if (!allData.containsKey(book.name)) allData[book.name] = mutableListOf()
                allData[book.name]!!.addAll(cached)
                continue
            }

            try {
                val data = fetchJson("https://bible-api.com/${book.name}+$ch?translation=kjv")
                if (data == null || !data.has("verses") || data.getJSONArray("verses").length() == 0) {
                    println("  ${book.canonical} $ch... NO VERSES")
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

                println("  ${book.canonical} $ch... OK (${verses.length()} verses)")
            } catch (e: Exception) {
                println("  ${book.canonical} $ch... FAILED (${e.message})")
            }

            sleep(2000)
        }
    }

    println("\n${"=".repeat(60)}")
    println("All unique books fetched! Now splitting into outputs...\n")

    // Step 2: Build Christianity output (66 books)
    if (mode == null || mode == "--christianity") {
        println("=== Building Christianity (66 books, 1189 chapters) ===")
        val chrisOffsets = mutableMapOf<String, Int>()
        var offset = 0
        for (book in CHRISTIANITY_BOOKS) {
            chrisOffsets[book.name] = offset
            offset += book.chapters
        }
        writeOutput(CHRISTIANITY_OUTPUT, chrisOffsets, CHRISTIANITY_BOOKS, allData)
    }

    // Step 3: Build Jewish/Tanakh output (24 books)
    if (mode == null || mode == "--jewish") {
        println("\n=== Building Jewish/Tanakh (24 books, 929 chapters) ===")
        // For combined books (Samuel, Kings, Chronicles, Ezra), merge chapters
        val jewishData = mutableMapOf<String, MutableList<JSONObject>>()

        // Build combined book data
        // Samuel = 1samuel + 2samuel
        // Kings = 1kings + 2kings
        // Chronicles = 1chronicles + 2chronicles
        // Ezra-Nehemiah = ezra + nehemiah
        val combinedBooks = mapOf(
            "samuel" to listOf("1samuel", "2samuel"),
            "kings" to listOf("1kings", "2kings"),
            "chronicles" to listOf("1chronicles", "2chronicles"),
            "ezra" to listOf("ezra", "nehemiah"),
        )

        // Process non-combined books first (those with unique jewishName not in combinedBooks)
        for (book in JEWISH_BOOKS_ORDERED) {
            if (book.jewishName in combinedBooks.keys) continue

            val verses = allData[book.name] ?: emptyList()
            jewishData[book.jewishName ?: book.name] = verses.toMutableList()
        }

        // Process combined books
        for ((combinedName, sourceBooks) in combinedBooks) {
            val merged = mutableListOf<JSONObject>()
            var chapterOffset = 0
            for (sourceBookName in sourceBooks) {
                val sourceBook = ALL_BOOKS.find { it.name == sourceBookName } ?: continue
                val verses = allData[sourceBookName] ?: emptyList()
                // Remap chapter_ids with offset
                for (verse in verses) {
                    merged.add(JSONObject().apply {
                        put("chapter_id", verse.getInt("chapter_id") + chapterOffset)
                        put("verse_number", verse.getInt("verse_number"))
                        put("original_text", verse.getString("original_text"))
                        put("translated_text", verse.getString("translated_text"))
                    })
                }
                chapterOffset += sourceBook.chapters
            }
            jewishData[combinedName] = merged
        }

        // Write Jewish output with proper offsets
        val jewishOffsets = mutableMapOf<String, Int>()
        var offset = 0
        for (book in JEWISH_BOOKS_ORDERED) {
            jewishOffsets[book.jewishName ?: book.name] = offset
            val combinedName = book.jewishName ?: book.name
            offset += (jewishData[combinedName]?.size?.let {
                // Calculate chapters from verses
                jewishData[combinedName]!!.map { it.getInt("chapter_id") }.distinct().size
            } ?: 0)
        }

        // Write output
        JEWISH_OUTPUT.parentFile.mkdirs()
        val allVerses = mutableListOf<JSONObject>()
        for (book in JEWISH_BOOKS_ORDERED) {
            val combinedName = book.jewishName ?: book.name
            val verses = jewishData[combinedName] ?: emptyList()
            val bookOffset = jewishOffsets[combinedName] ?: 0
            // Remap chapter_ids with the proper offset
            for (verse in verses) {
                allVerses.add(JSONObject().apply {
                    put("chapter_id", verse.getInt("chapter_id") + bookOffset)
                    put("verse_number", verse.getInt("verse_number"))
                    put("original_text", verse.getString("original_text"))
                    put("translated_text", verse.getString("translated_text"))
                })
            }
        }

        val arr = JSONArray()
        allVerses.forEach { arr.put(it) }
        JEWISH_OUTPUT.writeText(arr.toString(2))

        val sizeMB = JEWISH_OUTPUT.length() / (1024.0 * 1024.0)
        val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
        println("\nWritten: ${JEWISH_OUTPUT.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${allVerses.size} verses)")
        println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

        // Per-book summary
        offset = 0
        for (book in JEWISH_BOOKS_ORDERED) {
            val combinedName = book.jewishName ?: book.name
            val verses = jewishData[combinedName] ?: emptyList()
            val bookChapters = verses.map { it.getInt("chapter_id") }.distinct().size
            println("  ${book.canonical}: ${verses.size} verses in $bookChapters chapters")
            offset += bookChapters
        }
    }

    // Cleanup progress file
    if (PROGRESS_FILE.exists()) {
        PROGRESS_FILE.delete()
        println("\nProgress file cleaned up.")
    }

    println("\nDone!")
}

main()
