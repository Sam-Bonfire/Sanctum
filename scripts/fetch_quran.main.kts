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
 * Fetch the complete Quran from api.alquran.cloud
 * Editions: quran-uthmani (Arabic) + en.sahih (English)
 * Outputs: assets/islam/scripture.json
 */

val EXPECTED = mapOf(
    1 to 7, 2 to 286, 3 to 200, 4 to 176, 5 to 120, 6 to 165, 7 to 206, 8 to 75, 9 to 129, 10 to 109,
    11 to 123, 12 to 111, 13 to 43, 14 to 52, 15 to 99, 16 to 128, 17 to 111, 18 to 110, 19 to 98,
    20 to 135, 21 to 112, 22 to 78, 23 to 118, 24 to 64, 25 to 77, 26 to 227, 27 to 93, 28 to 88,
    29 to 69, 30 to 60, 31 to 34, 32 to 30, 33 to 73, 34 to 54, 35 to 45, 36 to 83, 37 to 182,
    38 to 88, 39 to 75, 40 to 85, 41 to 54, 42 to 53, 43 to 89, 44 to 59, 45 to 37, 46 to 35,
    47 to 38, 48 to 29, 49 to 18, 50 to 45, 51 to 60, 52 to 49, 53 to 62, 54 to 55, 55 to 78,
    56 to 96, 57 to 29, 58 to 22, 59 to 24, 60 to 13, 61 to 14, 62 to 11, 63 to 11, 64 to 18,
    65 to 12, 66 to 12, 67 to 30, 68 to 52, 69 to 52, 70 to 44, 71 to 28, 72 to 28, 73 to 20,
    74 to 56, 75 to 40, 76 to 31, 77 to 50, 78 to 40, 79 to 46, 80 to 42, 81 to 29, 82 to 19,
    83 to 36, 84 to 25, 85 to 22, 86 to 17, 87 to 19, 88 to 26, 89 to 30, 90 to 20, 91 to 15,
    92 to 21, 93 to 11, 94 to 8, 95 to 8, 96 to 19, 97 to 5, 98 to 8, 99 to 8, 100 to 11,
    101 to 11, 102 to 8, 103 to 3, 104 to 9, 105 to 5, 106 to 4, 107 to 7, 108 to 3, 109 to 6,
    110 to 3, 111 to 5, 112 to 4, 113 to 5, 114 to 6
)

val OUTPUT = File("assets/islam/scripture.json")
val BASE_URL = "https://api.alquran.cloud/v1/surah"

val client = HttpClient.newBuilder()
    .connectTimeout(java.time.Duration.ofSeconds(30))
    .build()

fun fetchJson(url: String, retries: Int = 3): String? {
    repeat(retries) { attempt ->
        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "PrayerApp/1.0")
                .timeout(java.time.Duration.ofSeconds(30))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() == 200) {
                return response.body()
            }
            println("  WARNING: HTTP ${response.statusCode()} for $url")
        } catch (e: Exception) {
            println("  Attempt ${attempt + 1} failed: ${e.message}")
            Thread.sleep(2000L * (attempt + 1))
        }
    }
    return null
}

fun sleep(ms: Long) = Thread.sleep(ms)

fun main() {
    println("=".repeat(60))
    println("Fetching complete Quran from api.alquran.cloud")
    println("Editions: quran-uthmani (Arabic) + en.sahih (English)")
    println("=".repeat(60))

    val allVerses = JSONArray()
    val totalExpected = EXPECTED.values.sum()
    println("Expected total: $totalExpected verses across 114 surahs\n")

    for (surah in 1..114) {
        print("Fetching surah $surah/114... ")
        val body = fetchJson("$BASE_URL/$surah/editions/quran-uthmani,en.sahih")
        if (body == null) {
            println("FAILED")
            continue
        }

        val json = JSONObject(body)
        if (json.getInt("code") != 200) {
            println("API error code ${json.getInt("code")}")
            continue
        }

        val data = json.getJSONArray("data")
        val arabicAyahs = data.getJSONObject(0).getJSONArray("ayahs")
        val englishAyahs = data.getJSONObject(1).getJSONArray("ayahs")

        val expectedCount = EXPECTED[surah] ?: 0
        if (arabicAyahs.length() != expectedCount) {
            print("WARNING: Expected $expectedCount, got ${arabicAyahs.length()} ")
        }

        for (i in 0 until arabicAyahs.length()) {
            val verse = JSONObject().apply {
                put("chapter_id", surah)
                put("verse_number", i + 1)
                put("original_text", arabicAyahs.getJSONObject(i).getString("text"))
                put("translated_text", englishAyahs.getJSONObject(i).getString("text"))
            }
            allVerses.put(verse)
        }
        println("OK (${arabicAyahs.length()} verses)")

        if (surah % 10 == 0) sleep(1000) else sleep(300)
    }

    println("\nTotal verses collected: ${allVerses.length()} (expected: $totalExpected)")

    if (allVerses.length() == 0) {
        println("ERROR: No verses collected. Aborting.")
        kotlin.system.exitProcess(1)
    }

    OUTPUT.parentFile.mkdirs()
    OUTPUT.writeText(allVerses.toString(2))
    val sizeMB = OUTPUT.length() / (1024.0 * 1024.0)
    println("\nWritten: assets/islam/scripture.json (${String.format("%.1f", sizeMB)} MB)")

    val chapters = allVerses.toList().map { (it as JSONObject).getInt("chapter_id") }.distinct().sorted()
    println("Surahs: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    val missing = (1..114).filter { it !in chapters }
    if (missing.isNotEmpty()) println("MISSING surahs: ${missing.joinToString(", ")}")
    else println("All 114 surahs present!")

    println("\nDone!")
}

main()
