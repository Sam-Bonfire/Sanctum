#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

// ── Book list: 66 Protestant OT+NT books in canonical order ──
data class BookSpec(val name: String, val chapters: Int)

val CHRISTIANITY_BOOKS = listOf(
    BookSpec("genesis", 50),
    BookSpec("exodus", 40),
    BookSpec("leviticus", 27),
    BookSpec("numbers", 36),
    BookSpec("deuteronomy", 34),
    BookSpec("joshua", 24),
    BookSpec("judges", 21),
    BookSpec("ruth", 4),
    BookSpec("1samuel", 31),
    BookSpec("2samuel", 24),
    BookSpec("1kings", 22),
    BookSpec("2kings", 25),
    BookSpec("1chronicles", 29),
    BookSpec("2chronicles", 36),
    BookSpec("ezra", 10),
    BookSpec("nehemiah", 13),
    BookSpec("esther", 10),
    BookSpec("job", 42),
    BookSpec("psalms", 150),
    BookSpec("proverbs", 31),
    BookSpec("ecclesiastes", 12),
    BookSpec("songofsolomon", 8),
    BookSpec("isaiah", 66),
    BookSpec("jeremiah", 52),
    BookSpec("lamentations", 5),
    BookSpec("ezekiel", 48),
    BookSpec("daniel", 12),
    BookSpec("hosea", 14),
    BookSpec("joel", 3),
    BookSpec("amos", 9),
    BookSpec("obadiah", 1),
    BookSpec("jonah", 4),
    BookSpec("micah", 7),
    BookSpec("nahum", 3),
    BookSpec("habakkuk", 3),
    BookSpec("zephaniah", 3),
    BookSpec("haggai", 2),
    BookSpec("zechariah", 14),
    BookSpec("malachi", 4),
    // ── New Testament ──
    BookSpec("matthew", 28),
    BookSpec("mark", 16),
    BookSpec("luke", 24),
    BookSpec("john", 21),
    BookSpec("acts", 28),
    BookSpec("romans", 16),
    BookSpec("1corinthians", 16),
    BookSpec("2corinthians", 13),
    BookSpec("galatians", 6),
    BookSpec("ephesians", 6),
    BookSpec("philippians", 4),
    BookSpec("colossians", 4),
    BookSpec("1thessalonians", 5),
    BookSpec("2thessalonians", 3),
    BookSpec("1timothy", 6),
    BookSpec("2timothy", 4),
    BookSpec("titus", 3),
    BookSpec("philemon", 1),
    BookSpec("hebrews", 13),
    BookSpec("james", 5),
    BookSpec("1peter", 5),
    BookSpec("2peter", 3),
    BookSpec("1john", 5),
    BookSpec("2john", 1),
    BookSpec("3john", 1),
    BookSpec("jude", 1),
    BookSpec("revelation", 22),
)

val EXPECTED_TOTAL_CHAPTERS = CHRISTIANITY_BOOKS.sumOf { it.chapters } // 1189

val christianityFile = File("assets/christianity/scripture.json")
val jewishFile = File("assets/jewish/scripture.json")

fun main() {
    println("=".repeat(60))
    println("Fix Christianity chapter_id numbering")
    println("=".repeat(60))

    // ── Step 1: Verify Jewish scripture (sanity check) ──
    if (jewishFile.exists()) {
        val jewishArr = JSONArray(jewishFile.readText())
        val jewishChapters = mutableSetOf<Int>()
        for (i in 0 until jewishArr.length()) {
            jewishChapters.add(jewishArr.getJSONObject(i).getInt("chapter_id"))
        }
        val sortedJewish = jewishChapters.sorted()
        val jewishRangeOk = sortedJewish.first() == 1 && sortedJewish.last() == sortedJewish.size
        println("\nJewish scripture.json: ${jewishArr.length()} verses, ${jewishChapters.size} chapters")
        println("  Range: ${sortedJewish.first()}-${sortedJewish.last()}, contiguous: $jewishRangeOk")
        if (!jewishRangeOk) {
            println("  WARNING: Jewish chapter_ids are not contiguous 1-N. Check separately.")
        }
    } else {
        println("\nJewish scripture.json not found — skipping verification.")
    }

    // ── Step 2: Read and diagnose Christianity scripture ──
    if (!christianityFile.exists()) {
        println("\nERROR: ${christianityFile.absolutePath} not found.")
        return
    }

    val arr = JSONArray(christianityFile.readText())
    val totalVerses = arr.length()
    println("\nChristianity scripture.json: $totalVerses verses")

    // Collect original chapter_ids
    val originalChapters = mutableSetOf<Int>()
    for (i in 0 until totalVerses) {
        originalChapters.add(arr.getJSONObject(i).getInt("chapter_id"))
    }
    val sortedOriginal = originalChapters.sorted()
    println("Original chapter_ids: ${originalChapters.size} distinct values")
    println("  Range: ${sortedOriginal.first()}-${sortedOriginal.last()}")

    // Detect collisions (multiple books sharing same chapter_id range)
    var collisions = 0
    var prevChapter = 0
    var bookBoundaries = mutableListOf<String>()
    var bookIndex = 0
    var chaptersInCurrentBook = 0
    var expectedChapters = CHRISTIANITY_BOOKS[0].chapters

    for (i in 0 until totalVerses) {
        val ch = arr.getJSONObject(i).getInt("chapter_id")
        if (ch < prevChapter || (i > 0 && ch == 1 && prevChapter > 1)) {
            // Book boundary detected
            if (bookIndex < CHRISTIANITY_BOOKS.size) {
                val book = CHRISTIANITY_BOOKS[bookIndex]
                bookBoundaries.add("${book.name}: $chaptersInCurrentBook chapters (expected ${book.chapters})")
                if (chaptersInCurrentBook != book.chapters) {
                    println("  WARNING: ${book.name} has $chaptersInCurrentBook chapters, expected ${book.chapters}")
                }
                bookIndex++
                expectedChapters = if (bookIndex < CHRISTIANITY_BOOKS.size) CHRISTIANITY_BOOKS[bookIndex].chapters else 0
            }
            chaptersInCurrentBook = 0
            // Count collision
            if (ch != 1) collisions++
        }
        chaptersInCurrentBook++
        prevChapter = ch
    }
    // Last book
    if (bookIndex < CHRISTIANITY_BOOKS.size) {
        val book = CHRISTIANITY_BOOKS[bookIndex]
        bookBoundaries.add("${book.name}: $chaptersInCurrentBook chapters (expected ${book.chapters})")
    }

    println("\nDetected ${bookIndex + 1} books from chapter_id patterns")
    if (collisions > 0) {
        println("Found $collisions overlapping chapter_id values (books sharing same IDs)")
    }

    // ── Step 3: Rebuild chapter_ids sequentially ──
    println("\nRemapping chapter_ids...")

    // Build offset map: book name -> cumulative chapter offset
    val offsets = mutableMapOf<String, Int>()
    var cumulativeOffset = 0
    for (book in CHRISTIANITY_BOOKS) {
        offsets[book.name] = cumulativeOffset
        cumulativeOffset += book.chapters
    }

    // Since data is ordered by book, detect book boundaries by chapter_id drops
    val fixedArr = JSONArray()
    var currentBookIdx = 0
    var offset = 0
    var prevCh = Int.MAX_VALUE
    var chaptersInBook = 0
    var bookStartIdx = 0 // index in fixedArr where current book starts

    for (i in 0 until totalVerses) {
        val verse = arr.getJSONObject(i)
        val origChapter = verse.getInt("chapter_id")

        // Detect book boundary: chapter_id drops (new book starts)
        if (origChapter < prevCh && i > 0) {
            // We've finished a book — update stats
            if (currentBookIdx < CHRISTIANITY_BOOKS.size) {
                val book = CHRISTIANITY_BOOKS[currentBookIdx]
                if (chaptersInBook != book.chapters) {
                    println("  NOTE: ${book.name} had $chaptersInBook chapters in data, expected ${book.chapters}")
                }
            }
            offset += if (currentBookIdx < CHRISTIANITY_BOOKS.size) CHRISTIANITY_BOOKS[currentBookIdx].chapters else chaptersInBook
            currentBookIdx++
            chaptersInBook = 0
        }

        val newChapterId = origChapter + offset
        fixedArr.put(JSONObject().apply {
            put("chapter_id", newChapterId)
            put("verse_number", verse.getInt("verse_number"))
            put("original_text", verse.getString("original_text"))
            put("translated_text", verse.getString("translated_text"))
        })

        chaptersInBook++
        prevCh = origChapter
    }
    // Account for last book
    if (currentBookIdx < CHRISTIANITY_BOOKS.size) {
        offset += CHRISTIANITY_BOOKS[currentBookIdx].chapters
    }

    // ── Step 4: Validate and write ──
    val fixedChapters = mutableSetOf<Int>()
    for (i in 0 until fixedArr.length()) {
        fixedChapters.add(fixedArr.getJSONObject(i).getInt("chapter_id"))
    }
    val sortedFixed = fixedChapters.sorted()

    println("\nFixed chapter_ids: ${fixedChapters.size} distinct values")
    println("  Range: ${sortedFixed.first()}-${sortedFixed.last()}")
    println("  Contiguous 1-N: ${sortedFixed.first() == 1 && sortedFixed.last() == sortedFixed.size}")

    if (sortedFixed.size != EXPECTED_TOTAL_CHAPTERS) {
        println("  WARNING: Expected $EXPECTED_TOTAL_CHAPTERS chapters, got ${sortedFixed.size}")
    }

    // Write back
    christianityFile.writeText(fixedArr.toString(2))

    val sizeMB = christianityFile.length() / (1024.0 * 1024.0)
    println("\nWritten: ${christianityFile.absolutePath} (${String.format("%.1f", sizeMB)} MB, ${fixedArr.length()} verses)")
    println("Chapters: ${sortedFixed.size} (range: ${sortedFixed.first()}-${sortedFixed.last()})")

    // Print per-book summary
    println("\nPer-book summary:")
    currentBookIdx = 0
    offset = 0
    prevCh = Int.MAX_VALUE
    chaptersInBook = 0
    var bookVerseCount = 0

    for (i in 0 until fixedArr.length()) {
        val ch = fixedArr.getJSONObject(i).getInt("chapter_id")
        if (ch < prevCh && i > 0) {
            // Print previous book summary
            if (currentBookIdx < CHRISTIANITY_BOOKS.size) {
                val book = CHRISTIANITY_BOOKS[currentBookIdx]
                println("  ${book.name}: $bookVerseCount verses in $chaptersInBook chapters")
            }
            currentBookIdx++
            chaptersInBook = 0
            bookVerseCount = 0
        }
        chaptersInBook++
        bookVerseCount++
        prevCh = ch
    }
    // Print last book
    if (currentBookIdx < CHRISTIANITY_BOOKS.size) {
        val book = CHRISTIANITY_BOOKS[currentBookIdx]
        println("  ${book.name}: $bookVerseCount verses in $chaptersInBook chapters")
    }

    println("\nDone!")
}

main()
