#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

fun main() {
    val file = File("assets/flavors.json")
    if (!file.exists()) {
        println("ERROR: assets/flavors.json not found!")
        System.exit(1)
    }

    try {
        val jsonArray = JSONArray(file.readText())
        println("Generating Fastlane metadata files for ${jsonArray.length()} flavors...")

        for (i in 0 until jsonArray.length()) {
            val flavor = jsonArray.getJSONObject(i)
            val flavorId = flavor.getString("flavorId")
            
            if (!flavor.has("seo")) {
                println("  [SKIP] $flavorId has no 'seo' block")
                continue
            }

            val seo = flavor.getJSONObject("seo")
            val appStoreMetadata = seo.getJSONObject("appStoreMetadata")

            val title = appStoreMetadata.getString("title")
            val subtitle = appStoreMetadata.getString("subtitle")
            val keywords = appStoreMetadata.getString("keywords")
            val shortDesc = seo.getString("shortDescription")
            val longDesc = seo.getString("longDescription")

            // 1. Android Directory Setup & File Generation
            val androidDir = File("fastlane/metadata/android/$flavorId")
            androidDir.mkdirs()
            
            File(androidDir, "title.txt").writeText(title.trim(), Charsets.UTF_8)
            File(androidDir, "short_description.txt").writeText(shortDesc.trim(), Charsets.UTF_8)
            File(androidDir, "full_description.txt").writeText(longDesc.trim(), Charsets.UTF_8)
            File(androidDir, "keywords.txt").writeText(keywords.trim(), Charsets.UTF_8)
            
            println("  [ANDROID] Generated metadata for $flavorId in ${androidDir.path}")

            // 2. iOS Directory Setup & File Generation
            val iosDir = File("fastlane/metadata/ios/$flavorId")
            iosDir.mkdirs()
            
            File(iosDir, "name.txt").writeText(title.trim(), Charsets.UTF_8)
            File(iosDir, "subtitle.txt").writeText(subtitle.trim(), Charsets.UTF_8)
            File(iosDir, "description.txt").writeText(longDesc.trim(), Charsets.UTF_8)
            File(iosDir, "keywords.txt").writeText(keywords.trim(), Charsets.UTF_8)
            
            println("  [iOS] Generated metadata for $flavorId in ${iosDir.path}")
        }

        println("\nFastlane metadata generation completed successfully!")
    } catch (e: Exception) {
        println("ERROR: Failed to generate Fastlane metadata: ${e.message}")
        e.printStackTrace()
        System.exit(1)
    }
}

main()
