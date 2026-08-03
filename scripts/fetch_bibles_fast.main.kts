#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fast parallel Bible/Tanakh fetcher — fetches all chapters concurrently
 * using a thread pool, with rate limiting and retry logic.
 *
 * Outputs:
 *   assets/christianity/scripture.json (1189 chapters, 66 books)
 *   assets/jewish/scripture.json       (929 chapters, 24 Tanakh books)
 *
 * Usage:
 *   kotlin scripts/fetch_bibles_fast.main.kts
 *   kotlin scripts/fetch_bibles_fast.main.kts --parallel=10
 */

data class Book(
    val name: String,
    val canonical: String,
    val chapters: Int,
    val section: String,
    val jewishName: String? = null
)

val ALL_BOOKS = listOf(
    Book("genesis", "Genesis", 50, "torah", "genesis"),
    Book("exodus", "Exodus", 40, "torah", "exodus"),
    Book("leviticus", "Leviticus", 27, "torah", "leviticus"),
    Book("numbers", "Numbers", 36, "torah", "numbers"),
    Book("deuteronomy", "Deuteronomy", 34, "torah", "deuteronomy"),
    Book("joshua", "Joshua", 24, "prophets", "joshua"),
    Book("judges", "Judges", 21, "prophets", "judges"),
    Book("ruth", "Ruth", 4, "writings", "ruth"),
    Book("1samuel", "1 Samuel", 31, "prophets", "samuel"),
    Book("2samuel", "2 Samuel", 24, "prophets"),
    Book("1kings", "1 Kings", 22, "prophets", "kings"),
    Book("2kings", "2 Kings", 25, "prophets"),
    Book("1chronicles", "1 Chronicles", 29, "writings", "chronicles"),
    Book("2chronicles", "2 Chronicles", 36, "writings"),
    Book("ezra", "Ezra", 10, "writings", "ezra"),
    Book("nehemiah", "Nehemiah", 13, "writings"),
    Book("esther", "Esther", 10, "writings", "esther"),
    Book("job", "Job", 42, "writings", "job"),
    Book("psalms", "Psalms", 150, "writings", "psalms"),
    Book("proverbs", "Proverbs", 31, "writings", "proverbs"),
    Book("ecclesiastes", "Ecclesiastes", 12, "writings", "ecclesiastes"),
    Book("songofsolomon", "Song of Solomon", 8, "writings", "songofsongs"),
    Book("isaiah", "Isaiah", 66, "prophets", "isaiah"),
    Book("jeremiah", "Jeremiah", 52, "prophets", "jeremiah"),
    Book("lamentations", "Lamentations", 5, "writings", "lamentations"),
    Book("ezekiel", "Ezekiel", 48, "prophets", "ezekiel"),
    Book("daniel", "Daniel", 12, "writings", "daniel"),
    Book("hosea", "Hosea", 14, "prophets", "hosea"),
    Book("joel", "Joel", 3, "prophets", "joel"),
    Book("amos", "Amos", 9, "prophets", "amos"),
    Book("obadiah", "Obadiah", 1, "prophets", "obadiah"),
    Book("jonah", "Jonah", 4, "prophets", "jonah"),
    Book("micah", "Micah", 7, "prophets", "micah"),
    Book("nahum", "Nahum", 3, "prophets", "nahum"),
    Book("habakkuk", "Habakkuk", 3, "prophets", "habakkuk"),
    Book("zephaniah", "Zephaniah", 3, "prophets", "zephaniah"),
    Book("haggai", "Haggai", 2, "prophets", "haggai"),
    Book("zechariah", "Zechariah", 14, "prophets", "zechariah"),
    Book("malachi", "Malachi", 4, "prophets", "malachi"),
    Book("matthew", "Matthew", 28, "nt"),
    Book("mark", "Mark", 16, "nt"),
    Book("luke", "Luke", 24, "nt"),
    Book("john", "John", 21, "nt"),
    Book("acts", "Acts", 28, "nt"),
    Book("romans", "Romans", 16, "nt"),
    Book("1corinthians", "1 Corinthians", 16, "nt"),
    Book("2corinthians", "2 Corinthians", 13, "nt"),
    Book("galatians", "Galatians", 6, "nt"),
    Book("ephesians", "Ephesians", 6, "nt"),
    Book("philippians", "Philippians", 4, "nt"),
    Book("colossians", "Colossians", 4, "nt"),
    Book("1thessalonians", "1 Thessalonians", 5, "nt"),
    Book("2thessalonians", "2 Thessalonians", 3, "nt"),
    Book("1timothy", "1 Timothy", 6, "nt"),
    Book("2timothy", "2 Timothy", 4, "nt"),
    Book("titus", "Titus", 3, "nt"),
    Book("philemon", "Philemon", 1, "nt"),
    Book("hebrews", "Hebrews", 13, "nt"),
    Book("james", "James", 5, "nt"),
    Book("1peter", "1 Peter", 5, "nt"),
    Book("2peter", "2 Peter", 3, "nt"),
    Book("1john", "1 John", 5, "nt"),
    Book("2john", "2 John", 1, "nt"),
    Book("3john", "3 John", 1, "nt"),
    Book("jude", "Jude", 1, "nt"),
    Book("revelation", "Revelation", 22, "nt"),
)

val CHRISTIANITY_BOOKS = ALL_BOOKS
val JEWISH_BOOKS_ORDERED = ALL_BOOKS.filter { it.jewishName != null }

val ASSETS_DIR = File(System.getProperty("user.dir")).let { dir ->
    if (dir.name == "scripts") File(dir.parentFile, "assets") else File(dir, "assets")
}
val CHRISTIANITY_OUTPUT = File(ASSETS_DIR, "christianity/scripture.json")
val JEWISH_OUTPUT = File(ASSETS_DIR, "jewish/scripture.json")
val PROGRESS_FILE = File(ASSETS_DIR, ".bible_progress.json")

val PARALLELISM = args.firstOrNull { it.startsWith("--parallel=") }
    ?.split("=")?.get(1)?.toIntOrNull() ?: 4

val client = HttpClient.newBuilder()
    .connectTimeout(java.time.Duration.ofSeconds(30))
    .build()

val fetched = ConcurrentHashMap<String, List<JSONObject>>()
val completed = AtomicInteger(0)
val failed = AtomicInteger(0)
val totalToFetch = AtomicInteger(0)

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
                val waitTime = 3000L * (attempt + 1)
                Thread.sleep(waitTime)
                return@repeat
            }

            if (response.statusCode() == 200) {
                return JSONObject(body)
            }
        } catch (e: Exception) {
            Thread.sleep(2000L * (attempt + 1))
        }
    }
    return null
}

fun saveProgress() {
    val json = JSONObject()
    for ((key, verses) in fetched) {
        val arr = JSONArray()
        verses.forEach { arr.put(it) }
        json.put(key, arr)
    }
    PROGRESS_FILE.writeText(json.toString())
}

fun loadProgress(): MutableMap<String, List<JSONObject>> {
    val result = mutableMapOf<String, List<JSONObject>>()

    // ONLY load from our own progress file — never from legacy files
    // because legacy files may contain global chapter IDs that are incompatible
    if (PROGRESS_FILE.exists()) {
        try {
            val json = JSONObject(PROGRESS_FILE.readText())
            for (key in json.keys()) {
                val arr = json.getJSONArray(key)
                val list = mutableListOf<JSONObject>()
                for (i in 0 until arr.length()) list.add(arr.getJSONObject(i))
                result[key] = list
            }
            println("Loaded ${result.size} cached chapters from ${PROGRESS_FILE.name}")
        } catch (e: Exception) {
            println("Warning: Could not load cache: ${e.message}")
        }
    }

    // Detect and reject stale cache entries that have global chapter IDs
    // Our cache should have local chapter_ids (1..N per book), not global offsets
    val validated = mutableMapOf<String, List<JSONObject>>()
    val maxLocalChapter = ALL_BOOKS.maxOf { it.chapters } // 150 for Psalms
    for ((key, verses) in result) {
        val bookName = key.substringBeforeLast("_")
        val chapterNum = key.substringAfterLast("_").toIntOrNull() ?: 0
        val book = ALL_BOOKS.find { it.name == bookName }

        if (book == null) {
            println("  REJECT cache: unknown book in key '$key'")
            continue
        }

        // Verify that stored chapter_id matches expected local chapter number
        val storedChapterId = verses.firstOrNull()?.getInt("chapter_id") ?: 0
        if (storedChapterId != chapterNum || storedChapterId > book.chapters) {
            println("  REJECT stale cache: $key (stored chapter_id=$storedChapterId, expected=$chapterNum)")
            continue
        }

        validated[key] = verses
    }

    if (validated.size < result.size) {
        println("  Rejected ${result.size - validated.size} stale cache entries")
    }

    return validated
}

fun writeChristianityOutput(allData: Map<String, List<JSONObject>>) {
    val offsets = mutableMapOf<String, Int>()
    var offset = 0
    for (book in CHRISTIANITY_BOOKS) {
        offsets[book.name] = offset
        offset += book.chapters
    }

    val allVerses = mutableListOf<JSONObject>()
    for (book in CHRISTIANITY_BOOKS) {
        val bookOffset = offsets[book.name] ?: 0
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

    CHRISTIANITY_OUTPUT.parentFile.mkdirs()
    val arr = JSONArray()
    allVerses.forEach { arr.put(it) }
    CHRISTIANITY_OUTPUT.writeText(arr.toString(2))

    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
    val sizeMB = CHRISTIANITY_OUTPUT.length() / (1024.0 * 1024.0)
    println("\nWritten: ${CHRISTIANITY_OUTPUT.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${allVerses.size} verses)")
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    for (book in CHRISTIANITY_BOOKS) {
        val verses = allData[book.name] ?: emptyList()
        println("  ${book.canonical}: ${verses.size} verses in ${book.chapters} chapters")
    }
}

fun writeJewishOutput(allData: Map<String, List<JSONObject>>) {
    // Build combined book data for Tanakh
    val combinedBooks = mapOf(
        "samuel" to listOf("1samuel", "2samuel"),
        "kings" to listOf("1kings", "2kings"),
        "chronicles" to listOf("1chronicles", "2chronicles"),
        "ezra" to listOf("ezra", "nehemiah"),
    )

    val jewishData = mutableMapOf<String, MutableList<JSONObject>>()

    // Non-combined books
    for (book in JEWISH_BOOKS_ORDERED) {
        if (book.jewishName in combinedBooks.keys) continue
        val verses = allData[book.name] ?: emptyList()
        jewishData[book.jewishName ?: book.name] = verses.toMutableList()
    }

    // Combined books
    for ((combinedName, sourceBooks) in combinedBooks) {
        val merged = mutableListOf<JSONObject>()
        var chapterOffset = 0
        for (sourceBookName in sourceBooks) {
            val sourceBook = ALL_BOOKS.find { it.name == sourceBookName } ?: continue
            val verses = allData[sourceBookName] ?: emptyList()
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

    // Calculate offsets for Jewish books
    val jewishOffsets = mutableMapOf<String, Int>()
    var offset = 0
    for (book in JEWISH_BOOKS_ORDERED) {
        val combinedName = book.jewishName ?: book.name
        jewishOffsets[combinedName] = offset
        val chapters = jewishData[combinedName]?.map { it.getInt("chapter_id") }?.distinct()?.size ?: 0
        offset += chapters
    }

    // Write output
    JEWISH_OUTPUT.parentFile.mkdirs()
    val allVerses = mutableListOf<JSONObject>()
    for (book in JEWISH_BOOKS_ORDERED) {
        val combinedName = book.jewishName ?: book.name
        val verses = jewishData[combinedName] ?: emptyList()
        val bookOffset = jewishOffsets[combinedName] ?: 0
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

    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
    val sizeMB = JEWISH_OUTPUT.length() / (1024.0 * 1024.0)
    println("\nWritten: ${JEWISH_OUTPUT.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${allVerses.size} verses)")
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    for (book in JEWISH_BOOKS_ORDERED) {
        val combinedName = book.jewishName ?: book.name
        val verses = jewishData[combinedName] ?: emptyList()
        val bookChapters = verses.map { it.getInt("chapter_id") }.distinct().size
        println("  ${book.canonical}: ${verses.size} verses in $bookChapters chapters")
    }
}

fun main() {
    println("=".repeat(60))
    println("Fast Parallel Bible/Tanakh Fetcher (bible-api.com KJV)")
    println("Parallelism: $PARALLELISM threads")
    println("=".repeat(60))

    // Load cache
    val cache = loadProgress()
    fetched.putAll(cache)

    // Build work items (skip cached)
    data class WorkItem(val book: Book, val chapter: Int, val key: String)

    val workItems = mutableListOf<WorkItem>()
    for (book in ALL_BOOKS) {
        for (ch in 1..book.chapters) {
            val key = "${book.name}_$ch"
            if (!cache.containsKey(key)) {
                workItems.add(WorkItem(book, ch, key))
            }
        }
    }

    totalToFetch.set(workItems.size)

    if (workItems.isEmpty()) {
        println("\nAll chapters cached! Writing outputs directly.\n")
    } else {
        println("Already cached: ${cache.size} chapters")
        println("To fetch: ${workItems.size} chapters")
        println("Starting fetch...\n")

        val executor = Executors.newFixedThreadPool(PARALLELISM)
        val startTime = System.currentTimeMillis()

        for (item in workItems) {
            executor.submit {
                val url = "https://bible-api.com/${item.book.name}+${item.chapter}?translation=kjv"
                val data = fetchJson(url)

                if (data != null && data.has("verses") && data.getJSONArray("verses").length() > 0) {
                    val verses = data.getJSONArray("verses")
                    val chapterVerses = mutableListOf<JSONObject>()
                    for (i in 0 until verses.length()) {
                        val v = verses.getJSONObject(i)
                        val text = v.getString("text").trim()
                        chapterVerses.add(JSONObject().apply {
                            put("chapter_id", item.chapter)
                            put("verse_number", v.getInt("verse"))
                            put("original_text", text)
                            put("translated_text", text)
                        })
                    }
                    fetched[item.key] = chapterVerses
                    // Periodic save every 100 chapters so we don't lose progress
                    val count = completed.incrementAndGet()
                    if (count % 100 == 0) {
                        saveProgress()
                        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
                        println("  Progress: ${count}/${workItems.size} chapters fetched, saving (${String.format("%.0f", elapsed)}s elapsed)")
                    }
                } else {
                    failed.incrementAndGet()
                    println("  FAILED: ${item.book.canonical} ${item.chapter}")
                }
            }
        }

        executor.shutdown()
        val finished = executor.awaitTermination(60, TimeUnit.MINUTES)

        if (!finished) {
            println("\nWARNING: Executor did not terminate cleanly after 60 min.")
        }

        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
        println("\nFetch complete in ${String.format("%.1f", elapsed)}s")
        println("  Success: ${completed.get()}")
        println("  Failed: ${failed.get()}")
        println("  Total fetched chapters: ${fetched.size}")

        // Save progress AFTER all threads are done
        saveProgress()
        println("Progress saved to ${PROGRESS_FILE.absolutePath}")
    }

    // Group fetched data by book name
    val allData = mutableMapOf<String, MutableList<JSONObject>>()
    for (book in ALL_BOOKS) {
        allData[book.name] = mutableListOf()
    }
    for ((key, verses) in fetched) {
        val bookName = key.substringBeforeLast("_")
        allData[bookName]?.addAll(verses)
    }

    // Write outputs
    println("\n${"=".repeat(60)}")
    println("Writing Christianity output...")
    writeChristianityOutput(allData)

    println("\n${"=".repeat(60)}")
    println("Writing Jewish/Tanakh output...")
    writeJewishOutput(allData)

    // Cleanup — only remove progress after we confirm we got all chapters
    val finalCount = fetched.size
    val expectedCount = ALL_BOOKS.sumOf { it.chapters }
    if (finalCount >= expectedCount) {
        if (PROGRESS_FILE.exists()) {
            PROGRESS_FILE.delete()
            println("\nProgress file cleaned up ($finalCount/$expectedCount chapters complete).")
        }
    } else {
        println("\nKeeping progress file — $finalCount/$expectedCount chapters fetched. Re-run to continue.")
    }

    println("\nDone!")
}

main()
