#!/usr/bin/env kotlin

/**
 * Single owner for syncing JSON content (scripture.json, duas.json) from the
 * centralized assets/ folder to the shared source folders used by the app:
 *   - shared/src/mobileMain/assets/{religion}/
 *   - shared/src/commonMain/composeResources/files/{religion}/
 *   - shared/src/wasmJsMain/resources/assets/{religion}/
 *
 * The per-flavor prayer.db is NOT owned here: the app build's generateBuildConfig
 * task copies assets/{flavor}/prayer.db into composeResources at build time, and
 * mobileMain/assets/{religion}/prayer.db is staged below for the iOS bundle.
 * Do not add another writer for these outputs.
 *
 * Usage:
 *   kotlin scripts/sync_assets.main.kts
 *   kotlin scripts/sync_assets.main.kts <religion>   # sync a single religion
 */

import java.io.File

val projectRoot = System.getProperty("user.dir").let { dir ->
    if (File(dir, "assets").exists() || dir.endsWith("scripts")) {
        if (dir.endsWith("scripts")) File(dir).parentFile else File(dir)
    } else File(dir)
}
val assetsDir = File(projectRoot, "assets")
val religions = listOf("islam", "christianity", "jewish", "hinduism", "taoism", "buddhism", "sikhism", "jainism", "shinto")

val targetReligions = if (args.isNotEmpty()) {
    listOf(args[0])
} else {
    religions
}

fun syncFile(source: File, dest: File) {
    if (!source.exists()) {
        println("  SKIP (source missing): ${source.absolutePath}")
        return
    }
    dest.parentFile.mkdirs()
    source.copyTo(dest, overwrite = true)
    println("  SYNCED: ${source.name} -> ${dest.absolutePath} (${source.length()} bytes)")
}

for (religion in targetReligions) {
    println("\n=== $religion ===")
    val relAssetsDir = File(assetsDir, religion)

    if (!relAssetsDir.exists()) {
        println("  WARNING: assets/$religion/ does not exist, skipping")
        continue
    }

    // Sync to mobileMain
    val mobileDir = File(projectRoot, "shared/src/mobileMain/assets/$religion")
    listOf("scripture.json", "duas.json", "prayer.db").forEach { filename ->
        syncFile(File(relAssetsDir, filename), File(mobileDir, filename))
    }

    // Sync to commonMain/composeResources/files
    val composeDir = File(projectRoot, "shared/src/commonMain/composeResources/files/$religion")
    listOf("scripture.json", "duas.json").forEach { filename ->
        syncFile(File(relAssetsDir, filename), File(composeDir, filename))
    }

    // Sync to wasmJsMain/resources/assets
    val wasmDir = File(projectRoot, "shared/src/wasmJsMain/resources/assets/$religion")
    listOf("scripture.json", "duas.json").forEach { filename ->
        syncFile(File(relAssetsDir, filename), File(wasmDir, filename))
    }
}

println("\n✓ Sync complete!")
