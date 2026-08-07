# Sanctum (PrayerApp)

> A modular, white-labeled multi-religion prayer application built with Kotlin Multiplatform.

Sanctum is designed as a foundational architecture capable of generating multiple distinct religious apps (e.g., Islamic "NUR", Jewish "SELAH") from a single shared codebase. By defining a dynamic `WhiteLabelConfig`, the app adapts its theme, terminology, and UI assets natively.

## Quick Start

### Prerequisites
- **Java**: JDK 21+
- **Mise**: [mise](https://mise.jdx.dev/) for task running and environment management (Install via `curl https://mise.run | sh` or standard OS package managers)
- **IDE**: Android Studio Koala / IntelliJ IDEA with KMP plugin
- **Kotlin**: Kotlin CLI/compiler (for running developer scripts)

### Getting Started
1. **Download Assets**
   Run the font downloader via mise to fetch required Google Fonts:
   ```bash
   mise run fonts:download
   ```

2. **Seed the Database**
   The application uses a pre-populated SQLite database for offline scriptures. Seed the initial Room database for the flavor you want to test (e.g., `islam` or `christianity`):
   ```bash
   mise run db:generate islam
   ```

3. **Build and Run via Mise (Recommended)**
   The project uses `mise` and a dynamic `flavors.json` configuration to simplify building and running different religious flavors of the app (islam, christianity, hinduism, buddhism, jewish, sikhism, jainism, shinto, taoism). You can list all available commands by running `mise tasks`. 
   
   To build the Android app for a specific religion:
   ```bash
   mise run build:islam:android
   # or
   mise run build:christianity:android
   ```
   
   To run the Web (Wasm) target locally:
   ```bash
   mise run run:hinduism:web
   ```
   
   *Alternatively, you can build manually by passing the flavor property:*
   ```bash
   ./gradlew :app:assembleDebug -Pflavor=islam
   ```

## Features

- **Dynamic White-Labeling**: Flavor injection via `WhiteLabelConfig` providing distinct branding and navigation tabs per religion.
- **Compass Integration**: Cross-platform sensor integration for Qibla/Mizrah direction.
- **Scripture Reader**: Offline-first reading experience utilizing pre-populated SQLite databases.
- **Bring Your Own Cloud (BYOC) Sync**: Native OS cloud syncing strategy for user data (Bookmarks, Settings) minimizing the need for a central backend server.
- **OTA Updates**: Over-the-air updates for scripture content via Ktor.

## Tech Stack

- **Framework**: Kotlin Multiplatform (KMP)
- **UI**: Compose Multiplatform (Android, iOS, Wasm/JS)
- **Navigation**: [Voyager](https://voyager.adriel.cafe/)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database / Offline Data**: Room KMP & SQLite Bundled
- **Networking**: Ktor Client

## CI/CD Pipeline & Deployments

The project leverages a robust GitHub Actions workflow combined with Fastlane to automate building, testing, and store deployments for all flavors. For a comprehensive overview of the branch cadence and how to interact with the automation scripts, refer to the [Automation Guide](./documentation/automation_guide.md).

## Documentation

- [Architecture Overview](./ARCHITECTURE.md)
- [Automation Guide](./documentation/automation_guide.md)

## Utility Scripts

The `scripts/` directory contains helpful Kotlin scripts (`.main.kts`) to automate development tasks. These are executed seamlessly via `mise`:

| Script | Purpose |
|--------|---------|
| `fonts:download` | Fetches standard TTF Google Fonts into `composeResources`. |
| `db:generate:*` | Generates a pre-populated SQLite database schema matching Room entities with sample scripture data. |
| `app:rename` | Utility to rename Android/iOS packages rapidly when producing a new white-label application. |

## License

MIT
