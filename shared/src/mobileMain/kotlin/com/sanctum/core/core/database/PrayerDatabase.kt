package com.sanctum.core.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VerseEntity::class, BookmarkEntity::class, DuaEntity::class, JournalEntryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun scriptureDao(): ScriptureDao
    abstract fun userDataDao(): UserDataDao
    abstract fun duasDao(): DuasDao
    abstract fun journalDao(): JournalDao
}
