package com.sanctum.core.core.database

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<PrayerDatabase>
