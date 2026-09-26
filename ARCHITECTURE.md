# Architecture Overview

> Sanctum (PrayerApp) leverages a Clean Architecture approach within a Kotlin Multiplatform context, prioritizing code reuse and white-label modularity.

## Clean Architecture Layers

The `shared` KMP module isolates the business logic from platform specifics. Code is organized per feature, each with its own clean-architecture slices under `com.sanctum.core`:

- **Per-feature slices** (`feature/<name>/{data,domain,presentation}`): e.g. `feature/charity/{data,domain,presentation}`, `feature/zakat/...`, `feature/reading/...`. There are no top-level `data`/`domain`/`presentation` packages — layers live inside each feature.
- **Shared core** (`core/{design,designsystem,navigation,notifications,di,...}`): white-label config, the single authoritative theme (`SanctumTheme`), navigation shell, DI modules.
- **Platform source sets**: `commonMain` (shared UI + domain), `mobileMain` (Room database + `expect` declarations), `androidMain`/`iosMain` (platform `actual`s), `wasmJsMain` (JSON-backed repositories where SQLite is unavailable).

## Project Structure

```text
PrayerApp/
├── app/                      # Application entry points (Android, iOS, Common)
│   └── src/commonMain/kotlin/com/sanctum/app/App.kt # The flavor configuration
├── shared/                   # KMP Shared Library (Core + Features)
│   └── src/
│       ├── commonMain/       # Shared UI, Domain, and standard multiplatform logic
│       ├── mobileMain/       # Mobile-specific logic (e.g. Room Database)
│       ├── androidMain/      # Android-specific platform bindings
│       ├── iosMain/          # iOS-specific platform bindings
│       └── wasmJsMain/       # Web-specific platform bindings
├── scripts/                  # Developer and deployment automation scripts
```

## White-Labeling Strategy

Flavor configuration is generated at build time: `assets/flavors.json` is the source of truth and the `generateBuildConfig` task (`app/build.gradle.kts`, selected via `-Pflavor=<id>`) emits `com.sanctum.app.BuildConfig` plus per-flavor resources.

- **Config Inject**: The `App.kt` component builds a `WhiteLabelConfig` from `BuildConfig` (brand, terminology, feature flags, per-flavor charity title).
- **Propagation**: This configuration is injected deep into the Compose tree via a `CompositionLocalProvider` (`LocalWhiteLabelConfig`).
- **Adaptation**: UI components (e.g. `MainLayout` Bottom Navigation Bar) and screens read this configuration to paint brand colors, display appropriate tab icons, and modify title strings dynamically. Navigation also falls back to dashboard for routes absent from the flavor's nav list.
- **Single theme authority**: `SanctumTheme` in the shared design system is the only theme. (The former app-shell `SelahTheme` parallel definition was deleted.)

## Data Strategy & BYOC Sync

- **Room KMP & SQLite Seeding**: The app relies on pre-populated SQLite databases (`prayer.db`) for scripture datasets to enable offline reading. This database is prepared using a two-step Kotlin script pipeline:
  1. `fetch_scriptures.main.kts` pulls data from APIs and generates local JSON datasets.
  2. `db_seeder.main.kts` parses the JSON, structures it into an SQLite schema matching Room's table specifications (including the `room_master_table`), and outputs the final `prayer.db`.
  At compile time, this database is bundled as an asset and read via Room KMP.
- **DB asset layout (one file per flavor)**: `assets/{flavor}/prayer.db` (seeder output, committed) is the single source of truth. The app build's `generateBuildConfig` copies the current flavor's DB into `composeResources/files/prayer.db` at build time (gitignored, Android runtime reads the merged asset). `shared/src/mobileMain/assets/{flavor}/prayer.db` (via `assets:sync`) stages per-flavor DBs for downstream iOS bundling. No other `*.db` copies are live.
- **Bring Your Own Cloud (BYOC)**: `ByocSyncManager` is the sync interface; check Koin bindings for the current platform implementation before demoing backup/restore. Bookmark tags are currently an in-memory stub (`dataModule` in `core/di/Koin.kt` returns empty flows with no-op tag writes) — tag UI renders but does not persist.
