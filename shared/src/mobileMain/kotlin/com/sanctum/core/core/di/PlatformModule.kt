package com.sanctum.core.core.di

import com.russhwolf.settings.Settings
import com.sanctum.core.core.database.PrayerDatabase
import com.sanctum.core.core.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        getDatabaseBuilder()
            .build()
    }

    single {
        val database: PrayerDatabase = get()
        database.scriptureDao()
    }

    single {
        val database: PrayerDatabase = get()
        database.userDataDao()
    }

    single<com.sanctum.core.feature.scripture.domain.ScriptureRepository> {
        com.sanctum.core.feature.scripture.data.RoomScriptureRepository(get())
    }

    single<com.sanctum.core.feature.duas.data.DuasRepository> {
        com.sanctum.core.feature.duas.data.RoomDuasRepository(get())
    }

    single { com.sanctum.core.feature.sync.data.DataExporter(get()) }
    single<com.sanctum.core.feature.sync.domain.ByocSyncManager> { com.sanctum.core.feature.sync.data.MockSyncManager(get()) }

    single<com.sanctum.core.feature.compass.domain.PlatformSensors> {
        com.sanctum.core.feature.compass.getPlatformSensors()
    }

    single<Settings> { Settings() }
}
