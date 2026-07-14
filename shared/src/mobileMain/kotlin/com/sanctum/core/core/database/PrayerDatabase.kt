package com.sanctum.core.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VerseEntity::class, BookmarkEntity::class, DuaEntity::class],
    version = 1,
)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun scriptureDao(): ScriptureDao
    abstract fun userDataDao(): UserDataDao
    abstract fun duasDao(): DuasDao
}
