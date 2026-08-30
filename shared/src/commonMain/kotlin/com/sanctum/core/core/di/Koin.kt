package com.sanctum.core.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val presentationModule = module {
    single { com.sanctum.core.feature.scripture.presentation.ScriptureViewModel(get(), get(), get()) }
    single { com.sanctum.core.feature.sync.presentation.SyncViewModel(get()) }
    single { com.sanctum.core.feature.scripture.presentation.DashboardViewModel(get(), get(), get(), get(), get(), get()) }
    single { com.sanctum.core.feature.duas.presentation.DuasCatalogViewModel(get(), get()) }
    single { com.sanctum.core.feature.journal.presentation.JournalViewModel(get()) }
    single { com.sanctum.core.feature.prayer.presentation.PrayerNotificationViewModel(get(), get()) }
}

val domainModule = module {
    single<com.sanctum.core.feature.scripture.domain.PrayerEngine> { com.sanctum.core.feature.scripture.domain.BaselinePrayerEngine() }
    factory { com.sanctum.core.feature.scripture.domain.PrayerScheduleUseCase(get()) }
    single { com.sanctum.core.feature.prayer.domain.PrayerNotificationSettingsRepository(get()) }
    single { com.sanctum.core.feature.prayer.domain.getAudioPlayer() }
    single { com.sanctum.core.feature.scripture.domain.DailyVerseManager(get()) }
}

val dataModule = module {
    single { com.sanctum.core.core.notifications.getPlatformNotificationManager() }
    single { com.sanctum.core.feature.compass.data.GeocodingRepository() }
    single<com.sanctum.core.feature.scripture.domain.ScrollPositionRepository> { com.sanctum.core.feature.scripture.data.SettingsScrollPositionRepository(get()) }
    single<com.sanctum.core.feature.scripture.domain.crossreference.CrossReferenceRepository> { com.sanctum.core.feature.scripture.data.crossreference.InMemoryCrossReferenceRepository() }
    // ScriptureRepository is intentionally NOT registered here.
    // Each platform module (mobileMain, wasmJsMain) provides its own real implementation.
}

fun initKoin() {
    startKoin {
        modules(presentationModule, domainModule, dataModule, platformModule)
    }
}
