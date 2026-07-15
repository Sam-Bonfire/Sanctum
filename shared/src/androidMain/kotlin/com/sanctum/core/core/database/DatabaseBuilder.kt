package com.sanctum.core.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

lateinit var applicationContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<PrayerDatabase> {
    val dbFile = applicationContext.getDatabasePath("prayer.db")
    val updateFile = applicationContext.getDatabasePath("prayer_update.db")

    if (updateFile.exists()) {
        if (dbFile.exists()) {
            dbFile.delete()
        }
        updateFile.renameTo(dbFile)
    }

    return Room.databaseBuilder<PrayerDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath,
    )
}
