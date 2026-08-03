#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Kotlin script to merge additional chapter data into an existing scripture.json
 *
 * Usage:
 *   kotlin scripts/merge_scripture.main.kts <religion_assets_dir> <additional_chapter_file>
 *
 * Example:
 *   kotlin scripts/merge_scripture.main.kts assets/hinduism assets/hinduism/chapter_18.json
 *
 * The additional file should be a JSON array of verse objects with keys:
 *   chapter_id, verse_number, original_text, translated_text
 *
 * The script merges, deduplicates (by chapter_id + verse_number), sorts, validates, and writes back.
 */

val religionDir = File(args[0])
val additionalFile = File(args[1])

println("=== Scripture Merger ===")
println("Religion dir: ${religionDir.absolutePath}")
println("Additional file: ${additionalFile.absolutePath}")

if (!religionDir.exists()) {
    System.err.println("Error: Religion directory does not exist: ${religionDir.absolutePath}")
    kotlin.system.exitProcess(1)
}

if (!additionalFile.exists()) {
    System.err.println("Error: Additional chapter file does not exist: ${additionalFile.absolutePath}")
    kotlin.system.exitProcess(1)
}

val scriptureFile = File(religionDir, "scripture.json")

// Read existing scripture.json
val existingVerses = mutableListOf<JSONObject>()
if (scriptureFile.exists()) {
    val existingArray = JSONArray(scriptureFile.readText())
    for (i in 0 until existingArray.length()) {
        existingVerses.add(existingArray.getJSONObject(i))
    }
    println("Existing verses: ${existingVerses.size}")
} else {
    println("No existing scripture.json found. Creating new file.")
}

// Read additional verses
val additionalArray = JSONArray(additionalFile.readText())
println("Additional verses: ${additionalArray.length()}")

// Merge and deduplicate (key: chapter_id_verse_number)
val existingKeys = existingVerses.map { "${it.getInt("chapter_id")}_${it.getInt("verse_number")}" }.toSet()
val newVerses = mutableListOf<JSONObject>()
for (i in 0 until additionalArray.length()) {
    val verse = additionalArray.getJSONObject(i)
    val key = "${verse.getInt("chapter_id")}_${verse.getInt("verse_number")}"
    if (key !in existingKeys) {
        newVerses.add(verse)
    }
}
println("New unique verses to add: ${newVerses.size}")

val allVerses = existingVerses + newVerses

// Sort by chapter_id then verse_number
val sorted = allVerses.sortedWith(compareBy<JSONObject> { it.getInt("chapter_id") }.thenBy { it.getInt("verse_number") })

// Write merged result
val mergedArray = JSONArray()
for (verse in sorted) {
    val obj = JSONObject()
    obj.put("chapter_id", verse.getInt("chapter_id"))
    obj.put("verse_number", verse.getInt("verse_number"))
    obj.put("original_text", verse.getString("original_text"))
    obj.put("translated_text", verse.getString("translated_text"))
    mergedArray.put(obj)
}

scriptureFile.writeText(mergedArray.toString(2))

println("\nTotal verses after merge: ${sorted.size}")

// Get unique chapters
val chapters = sorted.map { it.getInt("chapter_id") }.distinct().sorted()
println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

// Verify verse numbering (no gaps)
var hasErrors = false
for (ch in chapters) {
    val verses = sorted.filter { it.getInt("chapter_id") == ch }.sortedBy { it.getInt("verse_number") }
    for (i in 1..verses.size) {
        val actual = verses[i - 1].getInt("verse_number")
        if (actual != i) {
            println("ERROR: Chapter $ch has gap: expected verse $i, found $actual")
            hasErrors = true
        }
    }
}
if (!hasErrors) println("✓ All verse numbers are sequential - no gaps!")
else println("✗ Some chapters have verse numbering issues")

println("\nDone! Scripture file updated: ${scriptureFile.absolutePath}")
