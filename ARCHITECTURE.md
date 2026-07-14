# Architecture Overview

> Sanctum (PrayerApp) leverages a Clean Architecture approach within a Kotlin Multiplatform context, prioritizing code reuse and white-label modularity.

## Clean Architecture Layers

The `shared` KMP module isolates the business logic from platform specifics, organized into standard clean architecture layers within `com.sanctum.core`:

- **Data Layer** (`data`): Implementations for networking (Ktor), offline storage (Room), and platform sensors.
- **Domain Layer** (`domain`): Pure Kotlin business logic, including models (e.g., `ScriptureVerse`), core use cases, and abstract interfaces.
- **Presentation Layer** (`presentation`): Compose Multiplatform UI screens and platform-agnostic ViewModels.

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

The application acts as a shell that dynamically adapts its branding, wording, and enabled features depending on the `WhiteLabelConfig` provided.

- **Config Inject**: The `App.kt` component initializes a `WhiteLabelConfig` object (e.g. NUR for Islamic, SELAH for Jewish). 
- **Propagation**: This configuration is injected deep into the Compose tree via a `CompositionLocalProvider` (`LocalWhiteLabelConfig`).
- **Adaptation**: UI components (e.g. `MainLayout` Bottom Navigation Bar) and screens read this configuration to paint brand colors, display appropriate tab icons, and modify title strings dynamically.

## Data Strategy & BYOC Sync

- **Room KMP & SQLite Seeding**: The app relies on pre-populated SQLite databases (`prayer.db`) for scripture datasets to enable offline reading. This database is prepared using a two-step Kotlin script pipeline:
  1. `fetch_scriptures.main.kts` pulls data from APIs and generates local JSON datasets.
  2. `db_seeder.main.kts` parses the JSON, structures it into an SQLite schema matching Room's table specifications (including the `room_master_table`), and outputs the final `prayer.db`.
  At compile time, this database is bundled as an asset and read via Room KMP.
- **Bring Your Own Cloud (BYOC)**: Instead of deploying a custom backend to store user preferences or bookmarks, the `ByocSyncManager` interfaces with native OS clouds (Google Drive / iCloud) to seamlessly backup/restore the user's local Room database (`UserDataDao`).
