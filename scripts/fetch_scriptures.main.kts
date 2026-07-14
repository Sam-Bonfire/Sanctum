#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import org.json.JSONObject
import org.json.JSONArray
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

fun fetch(urlString: String): String {
    println("Fetching: $urlString")
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5000
    connection.readTimeout = 10000

    return if (connection.responseCode == 200) {
        connection.inputStream.bufferedReader().use { it.readText() }
    } else {
        println("Error fetching $urlString: HTTP ${connection.responseCode}")
        "{}"
    }
}

fun saveJson(flavor: String, jsonArray: JSONArray) {
    val dir = File("shared/src/mobileMain/assets/$flavor")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "scripture.json")
    file.writeText(jsonArray.toString(2))
    println("Saved ${jsonArray.length()} verses to ${file.absolutePath}")
}

fun fetchIslam(): JSONArray {
    val response = fetch("https://api.alquran.cloud/v1/surah/1/editions/quran-simple,en.asad")
    val result = JSONArray()
    try {
        val root = JSONObject(response)
        val data = root.getJSONArray("data")
        val arabicAyahs = data.getJSONObject(0).getJSONArray("ayahs")
        val englishAyahs = data.getJSONObject(1).getJSONArray("ayahs")
        
        for (i in 0 until arabicAyahs.length()) {
            val ar = arabicAyahs.getJSONObject(i)
            val en = englishAyahs.getJSONObject(i)
            val verse = JSONObject().apply {
                put("chapter_id", 1)
                put("verse_number", ar.getInt("numberInSurah"))
                put("original_text", ar.getString("text"))
                put("translated_text", en.getString("text"))
            }
            result.put(verse)
        }
    } catch (e: Exception) {
        println("Failed to parse Islam API: ${e.message}")
    }
    return result
}

fun fetchChristianity(): JSONArray {
    val response = fetch("https://bible-api.com/genesis+1")
    val result = JSONArray()
    try {
        val root = JSONObject(response)
        val verses = root.getJSONArray("verses")
        for (i in 0 until verses.length()) {
            val v = verses.getJSONObject(i)
            val verse = JSONObject().apply {
                put("chapter_id", 1)
                put("verse_number", v.getInt("verse"))
                put("original_text", v.getString("text").trim())
                put("translated_text", v.getString("text").trim())
            }
            result.put(verse)
        }
    } catch (e: Exception) {
        println("Failed to parse Christianity API: ${e.message}")
    }
    return result
}

fun fetchJewish(): JSONArray {
    val response = fetch("https://www.sefaria.org/api/texts/Genesis.1")
    val result = JSONArray()
    try {
        val root = JSONObject(response)
        val textArray = root.getJSONArray("text")
        val heArray = root.getJSONArray("he")
        
        val count = minOf(textArray.length(), heArray.length())
        for (i in 0 until count) {
            val enRaw = textArray.getString(i).replace(Regex("<[^>]*>"), "")
            val heRaw = heArray.getString(i).replace(Regex("<[^>]*>"), "")
            
            val verse = JSONObject().apply {
                put("chapter_id", 1)
                put("verse_number", i + 1)
                put("original_text", heRaw.trim())
                put("translated_text", enRaw.trim())
            }
            result.put(verse)
        }
    } catch (e: Exception) {
        println("Failed to parse Jewish API: ${e.message}")
    }
    return result
}

fun fetchHinduism(): JSONArray {
    val translationResponse = fetch("https://raw.githubusercontent.com/praneshp1org/Bhagavad-Gita-JSON-data/master/translation.json")
    val verseResponse = fetch("https://raw.githubusercontent.com/gita/gita/main/data/verse.json")
    
    val result = JSONArray()
    try {
        val cleanTranslation = translationResponse.trim('\uFEFF')
        val translations = JSONArray(cleanTranslation)
        val verses = JSONArray(verseResponse.trim('\uFEFF'))
        
        val originalTextMap = mutableMapOf<Int, String>()
        for (i in 0 until verses.length()) {
            val v = verses.getJSONObject(i)
            if (v.getInt("chapter_number") == 1) {
                originalTextMap[v.getInt("verse_number")] = v.getString("text").trim()
            }
        }
        
        // Find English translations for Chapter 1
        for (i in 0 until translations.length()) {
            val item = translations.getJSONObject(i)
            // author_id 19 is Swami Gambirananda (English), verse_id corresponds to chapter 1 verse numbers
            if (item.getString("lang") == "english" && item.getInt("author_id") == 19 && item.getInt("verse_number") <= 47) {
                val verseNum = item.getInt("verse_number")
                val originalText = originalTextMap[verseNum] ?: "Text unavailable"
                result.put(JSONObject().apply {
                    put("chapter_id", 1)
                    put("verse_number", verseNum)
                    put("original_text", originalText)
                    put("translated_text", item.getString("description"))
                })
            }
        }
    } catch (e: Exception) {
        println("Failed to parse Hinduism API: ${e.message}")
    }
    return result
}

fun fetchLocal(flavor: String): JSONArray {
    val result = JSONArray()
    try {
        val file = File("scripts/data/$flavor.json")
        if (file.exists()) {
            val root = JSONArray(file.readText())
            for (i in 0 until root.length()) {
                result.put(root.getJSONObject(i))
            }
        } else {
            println("No local data found for $flavor at ${file.absolutePath}")
        }
    } catch (e: Exception) {
        println("Failed to parse local JSON for $flavor: ${e.message}")
    }
    return result
}

fun fetchBuddhism(): JSONArray = fetchLocal("buddhism")
fun fetchSikhism(): JSONArray = fetchLocal("sikhism")
fun fetchJainism(): JSONArray = fetchLocal("jainism")
fun fetchShinto(): JSONArray = fetchLocal("shinto")
fun fetchTaoism(): JSONArray = fetchLocal("taoism")

val flavors = listOf("islam", "christianity", "hinduism", "buddhism", "jewish", "sikhism", "jainism", "shinto", "taoism")

println("Starting scripture fetch for ${flavors.size} flavors...")

for (flavor in flavors) {
    val data = when (flavor) {
        "islam" -> fetchIslam()
        "christianity" -> fetchChristianity()
        "jewish" -> fetchJewish()
        "hinduism" -> fetchHinduism()
        "buddhism" -> fetchBuddhism()
        "sikhism" -> fetchSikhism()
        "jainism" -> fetchJainism()
        "shinto" -> fetchShinto()
        "taoism" -> fetchTaoism()
        else -> JSONArray()
    }
    if (data.length() > 0) {
        saveJson(flavor, data)
    } else {
        println("No data fetched for $flavor")
    }
}

println("Fetch complete.")
