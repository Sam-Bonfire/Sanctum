#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.xerial:sqlite-jdbc:3.42.0.0")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import java.sql.DriverManager
import org.json.JSONArray
import kotlin.system.exitProcess

fun seedDatabase(dbPath: String, religion: String) {
    val dbFile = File(dbPath)
    if (dbFile.exists()) {
        dbFile.delete()
    }

    dbFile.parentFile.mkdirs()

    println("Creating pre-populated SQLite database at $dbPath for $religion...")

    val url = "jdbc:sqlite:${dbFile.absolutePath}"

    DriverManager.getConnection(url).use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS verses (
                    id INTEGER PRIMARY KEY NOT NULL,
                    chapter_id INTEGER NOT NULL,
                    verse_number INTEGER NOT NULL,
                    original_text TEXT NOT NULL,
                    translated_text TEXT NOT NULL
                )
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bookmarks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    verse_id INTEGER NOT NULL,
                    timestamp_ms INTEGER NOT NULL
                )
            """.trimIndent())

            val jsonFile = File(assetsRoot, "$religion/scripture.json")
            if (!jsonFile.exists()) {
                println("Error: No scripture.json found for $religion at ${jsonFile.absolutePath}. Did you run fetch_scriptures.main.kts?")
                return
            }

            val jsonContent = jsonFile.readText()
            val versesArray = JSONArray(jsonContent)

            val insertStmt = conn.prepareStatement("INSERT INTO verses (id, chapter_id, verse_number, original_text, translated_text) VALUES (?, ?, ?, ?, ?)")
            for (i in 0 until versesArray.length()) {
                val verse = versesArray.getJSONObject(i)
                insertStmt.setInt(1, i + 1)
                insertStmt.setInt(2, verse.getInt("chapter_id"))
                insertStmt.setInt(3, verse.getInt("verse_number"))
                insertStmt.setString(4, verse.getString("original_text"))
                insertStmt.setString(5, verse.getString("translated_text"))
                insertStmt.addBatch()
            }
            insertStmt.executeBatch()

            // --- Seed Duas ---
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS duas (
                    id TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL,
                    original_text TEXT NOT NULL,
                    translated_text TEXT NOT NULL,
                    transliteration TEXT
                )
            """.trimIndent())

            val duasFile = File(assetsRoot, "$religion/duas.json")
            if (duasFile.exists()) {
                val duasContent = duasFile.readText()
                val duasArray = JSONArray(duasContent)

                val insertDuasStmt = conn.prepareStatement("INSERT INTO duas (id, title, original_text, translated_text, transliteration) VALUES (?, ?, ?, ?, ?)")
                for (i in 0 until duasArray.length()) {
                    val dua = duasArray.getJSONObject(i)
                    insertDuasStmt.setString(1, dua.getString("id"))
                    insertDuasStmt.setString(2, dua.getString("title"))
                    insertDuasStmt.setString(3, dua.getString("original_text"))
                    insertDuasStmt.setString(4, dua.getString("translated_text"))
                    if (dua.has("transliteration") && !dua.isNull("transliteration")) {
                        insertDuasStmt.setString(5, dua.getString("transliteration"))
                    } else {
                        insertDuasStmt.setNull(5, java.sql.Types.VARCHAR)
                    }
                    insertDuasStmt.addBatch()
                }
                insertDuasStmt.executeBatch()
            } else {
                println("Note: No duas.json found for $religion at ${duasFile.absolutePath}. Skipping duas seeding.")
            }

            stmt.execute("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
            stmt.execute("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b7a2d82b4ff9714856f6c91a0300a0b2')")
        }
    }

    println("Successfully seeded database at $dbPath")
}

val religions = listOf("islam", "christianity", "jewish", "hinduism", "taoism", "buddhism", "sikhism", "jainism", "shinto")

// Resolve assets directory relative to project root (not script directory)
val projectRoot = System.getProperty("user.dir").let { dir ->
    if (File(dir, "assets").exists() || dir.endsWith("scripts")) {
        if (dir.endsWith("scripts")) File(dir).parentFile else File(dir)
    } else File(dir)
}
val assetsRoot = File(projectRoot, "assets")

if (args.isEmpty()) {
    // No arguments: seed all religions to assets/{religion}/prayer.db
    println("=== Seeding ALL religions from $assetsRoot ===")
    for (religion in religions) {
        val dbPath = File(assetsRoot, "$religion/prayer.db").absolutePath
        seedDatabase(dbPath, religion)
        println()
    }
    println("=== All databases seeded! ===")
} else if (args.size == 1) {
    // Single arg: religion name (output goes to assets/{religion}/prayer.db)
    val religion = args[0]
    if (religion !in religions) {
        println("Unknown religion: $religion. Valid: ${religions.joinToString(", ")}")
        exitProcess(1)
    }
    seedDatabase(File(assetsRoot, "$religion/prayer.db").absolutePath, religion)
} else if (args.size == 2) {
    // Legacy: explicit output path + religion
    seedDatabase(args[0], args[1])
} else {
    println("Usage:")
    println("  kotlin db_seeder.main.kts                          # Seed all religions")
    println("  kotlin db_seeder.main.kts <religion>               # Seed one religion")
    println("  kotlin db_seeder.main.kts <db_path> <religion>     # Seed to specific path")
    exitProcess(1)
}
