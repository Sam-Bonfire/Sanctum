package com.sanctum.core.core.database

import java.io.FileOutputStream

actual suspend fun populateDatabaseIfNotExists() {
    val dbFile = applicationContext.getDatabasePath("prayer.db")
    if (!dbFile.exists()) {
        try {
            dbFile.parentFile?.mkdirs()
            applicationContext.assets.open("composeResources/files/prayer.db").use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
            println("Database copied from assets to \${dbFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to copy database: \${e.message}")
        }
    }
}
