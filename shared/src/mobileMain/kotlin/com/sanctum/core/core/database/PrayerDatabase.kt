package com.sanctum.core.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [
        VerseEntity::class,
        BookmarkEntity::class,
        DuaEntity::class,
        JournalEntryEntity::class,
        NoteEntity::class,
        HighlightEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@ConstructedBy(PrayerDatabaseConstructor::class)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun scriptureDao(): ScriptureDao
    abstract fun userDataDao(): UserDataDao
    abstract fun duasDao(): DuasDao
    abstract fun journalDao(): JournalDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PrayerDatabaseConstructor : RoomDatabaseConstructor<PrayerDatabase> {
    override fun initialize(): PrayerDatabase
}
