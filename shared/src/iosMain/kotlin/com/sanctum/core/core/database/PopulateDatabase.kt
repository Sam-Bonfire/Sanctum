package com.sanctum.core.core.database

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual suspend fun populateDatabaseIfNotExists() {
    val fileManager = NSFileManager.defaultManager()
    val documentDirectory = fileManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbUrl = documentDirectory?.URLByAppendingPathComponent("prayer.db")
    val dbPath = dbUrl?.path
    val updateDbUrl = documentDirectory?.URLByAppendingPathComponent("prayer_update.db")
    val updateDbPath = updateDbUrl?.path

    if (updateDbPath != null && fileManager.fileExistsAtPath(updateDbPath)) {
        if (dbPath != null && fileManager.fileExistsAtPath(dbPath)) {
            fileManager.removeItemAtPath(dbPath, null)
        }
        fileManager.moveItemAtPath(updateDbPath, dbPath ?: "", null)
    } else if (dbPath != null && !fileManager.fileExistsAtPath(dbPath)) {
        // Find the pre-populated database inside the iOS App Bundle
        val bundledDbPath = NSBundle.mainBundle().pathForResource("prayer", "db")
        if (bundledDbPath != null) {
            fileManager.copyItemAtPath(bundledDbPath, dbPath, null)
        }
    }
}
