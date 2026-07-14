#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject
import kotlin.system.exitProcess

fun main() {
    val file = File("assets/flavors.json")
    if (!file.exists()) {
        println("ERROR: assets/flavors.json does not exist!")
        exitProcess(1)
    }

    try {
        val jsonArray = JSONArray(file.readText())
        println("Validating SEO metadata for ${jsonArray.length()} flavors...")

        var errorsCount = 0

        for (i in 0 until jsonArray.length()) {
            val flavor = jsonArray.getJSONObject(i)
            val flavorId = flavor.optString("flavorId", "UNKNOWN")
            
            println("Checking flavor: $flavorId")

            if (!flavor.has("seo")) {
                println("  [ERROR] Missing 'seo' object")
                errorsCount++
                continue
            }

            val seo = flavor.getJSONObject("seo")
            val requiredFields = listOf("shortDescription", "longDescription", "category", "contentRating")
            for (field in requiredFields) {
                if (!seo.has(field) || seo.getString(field).trim().isEmpty()) {
                    println("  [ERROR] Missing or empty field '$field'")
                    errorsCount++
                }
            }

            // Verify arrays
            val requiredArrays = listOf("primaryKeywords", "secondaryKeywords", "longTailKeywords")
            for (arr in requiredArrays) {
                if (!seo.has(arr)) {
                    println("  [ERROR] Missing array field '$arr'")
                    errorsCount++
                } else {
                    val jsonArr = seo.getJSONArray(arr)
                    if (jsonArr.length() == 0) {
                        println("  [WARNING] Empty array field '$arr'")
                    }
                }
            }

            // Verify metaTags
            if (!seo.has("metaTags")) {
                println("  [ERROR] Missing 'metaTags' object")
                errorsCount++
            } else {
                val metaTags = seo.getJSONObject("metaTags")
                for (tag in listOf("title", "description", "keywords")) {
                    if (!metaTags.has(tag) || metaTags.getString(tag).trim().isEmpty()) {
                        println("  [ERROR] metaTags: Missing or empty '$tag'")
                        errorsCount++
                    }
                }
            }

            // Verify appStoreMetadata
            if (!seo.has("appStoreMetadata")) {
                println("  [ERROR] Missing 'appStoreMetadata' object")
                errorsCount++
            } else {
                val appStoreMetadata = seo.getJSONObject("appStoreMetadata")
                for (meta in listOf("title", "subtitle", "keywords")) {
                    if (!appStoreMetadata.has(meta) || appStoreMetadata.getString(meta).trim().isEmpty()) {
                        println("  [ERROR] appStoreMetadata: Missing or empty '$meta'")
                        errorsCount++
                    }
                }
                
                // Apple keywords length check (100 characters max)
                if (appStoreMetadata.has("keywords")) {
                    val keywordsStr = appStoreMetadata.getString("keywords")
                    if (keywordsStr.length > 100) {
                        println("  [WARNING] appStoreMetadata: keywords length (${keywordsStr.length}) exceeds Apple's 100 character limit!")
                    }
                }
            }
        }

        if (errorsCount > 0) {
            println("\nValidation failed with $errorsCount errors.")
            exitProcess(1)
        } else {
            println("\nValidation succeeded! All flavors contain valid SEO metadata.")
        }
    } catch (e: Exception) {
        println("ERROR: Failed to parse assets/flavors.json as JSON array: ${e.message}")
        e.printStackTrace()
        exitProcess(1)
    }
}

main()
