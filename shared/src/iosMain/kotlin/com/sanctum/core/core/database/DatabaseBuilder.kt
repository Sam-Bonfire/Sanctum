package com.sanctum.core.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun getDatabaseBuilder(): RoomDatabase.Builder<PrayerDatabase> {
    val fileManager = NSFileManager.defaultManager()
    val documentDirectory = fileManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val dbUrl = documentDirectory?.URLByAppendingPathComponent("prayer.db")

    val dbFilePath = dbUrl?.path ?: throw IllegalStateException("Database path could not be resolved.")
    return Room.databaseBuilder<PrayerDatabase>(
        name = dbFilePath,
        factory = { PrayerDatabaseConstructor.initialize() },
    ).fallbackToDestructiveMigration(dropAllTables = true)
}
