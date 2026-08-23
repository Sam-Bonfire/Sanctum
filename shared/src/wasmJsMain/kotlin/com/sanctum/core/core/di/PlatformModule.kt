package com.sanctum.core.core.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import com.sanctum.core.feature.compass.WasmPlatformSensors
import com.sanctum.core.feature.compass.domain.PlatformSensors
import com.sanctum.core.feature.scripture.data.ScriptureRepository
import com.sanctum.core.feature.scripture.data.WasmScriptureRepository
import com.sanctum.core.feature.sync.domain.ByocSyncManager
import com.sanctum.core.feature.sync.domain.WasmByocSyncManager
import kotlinx.browser.window
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // Real localStorage-backed settings — survives page refresh
    single<Settings> { StorageSettings(window.localStorage) }

    // Real Ktor-backed scripture repository — loads JSON assets from the server
    single<ScriptureRepository> { WasmScriptureRepository(get()) }

    // Real compass via the Web DeviceOrientation & Geolocation APIs
    single<PlatformSensors> { WasmPlatformSensors() }

    // BYOC sync is intentionally not implemented on web (no native cloud provider)
    single<ByocSyncManager> { WasmByocSyncManager() }

    // Resource-backed Duas repository since WasmJs lacks Room SQLite support
    single<com.sanctum.core.feature.duas.data.DuasRepository> { com.sanctum.core.feature.duas.data.ResourceDuasRepository() }

    // Wasm stub for JournalRepository since Room isn't supported yet
    single<com.sanctum.core.feature.journal.data.JournalRepository> { com.sanctum.core.feature.journal.data.WasmJournalRepository() }
}
