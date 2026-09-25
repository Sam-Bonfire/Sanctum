# Automation Guide for SEO Metadata Integration

This guide outlines how the Sanctum project automates the flow of SEO and store metadata from `assets/flavors.json` into Apple App Store and Google Play listings. It leverages **Mise** for consistent local/CI tool versioning, pure **Kotlin scripts** for verification/metadata generation, and **GitHub Actions** + **Fastlane** for the deployment pipeline.

---

## 1. Cadence & Branching Strategy

To keep the development pipeline fast and release costs low, Sanctum separates verification from deployment using a two-tiered cadence:

* **Development (Verification)**: Pushes and Pull Requests targeting the `dev` branch trigger verification checks (Spotless format linter, SEO syntax validation, per-flavor staging builds). *No release builds are built, and no store deployments occur.*
* **Production (Release)**: Releases are cut by pushing a `v*` tag (via `tag-release.yml` manual dispatch). The tag push is the single trigger for `production-release.yml`, which builds per-flavor Android release bundles, iOS frameworks, and WASM production distributions, then publishes one GitHub Release. There is no Fastlane store deployment in CI and no `main`-branch build matrix.

### Managing this Cadence in Jujutsu (`jj`) with Mise

Jujutsu uses bookmarks which map 1-to-1 with Git branches. You can manage your development, release, and emergency hotfix cadence entirely using the automated `mise` tasks:

1. **Develop Features on `dev`**:
   Write code and verify it locally. When you are ready to push it to trigger the CI verification workflow:
   ```bash
   mise run push:dev
   ```
   *(Executes `jj git push --bookmark dev` under the hood).*

2. **Deploy a Batch Release to Production (`main`)**:
   Once all features accumulated on the `dev` branch are verified and ready for release, run:
   ```bash
   mise run release
   ```
   *(Executes `jj bookmark set main -r dev && jj git push --bookmark main` under the hood. Note: pushing `main` alone does not cut a release — releases are published from `v*` tags, see section 4).*

3. **Deploy an Emergency Hotfix**:
   If you need to push a fix directly to production without deploying incomplete features currently sitting in `dev`:
   * Create a new commit off `main` and write your hotfix:
     ```bash
     jj new main
     ```
   * Deploy the hotfix immediately:
     ```bash
     mise run push:main
     ```
     *(Executes `jj git push --bookmark main` under the hood).*
   * Sync the hotfix changes back into your `dev` branch to prevent divergence:
     ```bash
     mise run hotfix:sync
     ```
     *(Executes `jj rebase -b dev -d main` under the hood).*

---

## 2. Core Automation Scripts (Pure Kotlin)

To avoid local Node/JS/Ruby dependencies for developers, all build-time configuration tasks are written as lightweight Kotlin scripts (`.main.kts`). These run natively using the Kotlin environment configured by Mise.

### A. SEO Validation (`scripts/validate_seo.main.kts`)
Parses `assets/flavors.json` and asserts that every flavor contains complete, correct metadata fields. It performs warning checks if Apple's 100-character keyword limit is exceeded:
```bash
# Run locally
mise run seo:validate
```

### B. Fastlane Metadata Generator (`scripts/generate_fastlane_metadata.main.kts`)
Extracts the configuration values for each flavor and outputs the native directory structures and text files (e.g. `name.txt`, `subtitle.txt`, `description.txt`) required by Fastlane:
```bash
# Run locally
mise run seo:generate-metadata
```

---

## 3. Mise Configurations (`mise.toml`)

Mise manages environment consistency between local development machines and GitHub Actions runners. It ensures you use the same Java, Kotlin, Gradle, and Jujutsu versions.

### Predefined Shortcuts
The `mise.toml` file contains the following custom task shortcuts:
* `mise run format`: Automatically formats your Kotlin codebase with Spotless (`gradle spotlessApply`).
* `mise run lint`: Validates formatting without applying changes (`gradle spotlessCheck`). There is no `format:check` task.
* `mise run test`: Runs unit, integration, and verification suites (`gradle check`).
* `mise run seo:validate`: Validates that `flavors.json` meets all SEO requirements.
* `mise run seo:generate-metadata`: Generates local metadata files for App Store/Play Store upload.
* `mise run run:islam:web`: Starts the WasmJS web development server for the Islam flavor.
* `mise run build:islam:android`: Builds a debug APK locally for the Islam flavor.
* `mise run push:dev`: Pushes the current dev bookmark to origin for CI verification.
* `mise run push:main`: Pushes the main bookmark (e.g. for emergency hotfixes) to origin to deploy immediately.
* `mise run release`: Promotes the verified `dev` commit to `main` and pushes it to trigger production release.
* `mise run hotfix:sync`: Rebase the `dev` branch on top of `main` to synchronize changes after a hotfix.

---

## 4. GitHub Actions Workflows (`.github/workflows/`)

There is no `build.yml`. The six real workflows are:

* `ci-dev.yml` — Staging Pipeline. Triggers on push to `dev` (plus manual dispatch). Runs change detection, verification (`lint`, `test`, SEO), per-flavor Android/iOS/WASM staging builds. Promotion to production is manual (see `mise run release`); no automation pushes `dev` to `main`.
* `ci-main.yml` — Production branch verification on push to `main`.
* `pr-preview.yml` — Pull-request checks (lint/test summaries and annotations).
* `labeler.yml` — PR label automation.
* `tag-release.yml` — Manual dispatch only (`version_type` input). Calculates the next SemVer tag and pushes it. Pushing the tag is the single trigger for the release workflow below; there is no explicit workflow dispatch.
* `production-release.yml` — Triggers only on pushed `v*` tags. Builds per-flavor Android release bundles (AAB only, no debug APK), iOS frameworks, and WASM production distributions (fails loudly if `productionExecutable` is absent), then publishes one GitHub Release with generated-or-fallback notes.

No workflow executes Fastlane store deployment; the Fastfile lanes are local-only helpers (see section 5). There are no docs-only runtime or deployment steps beyond what these files contain.

---

## 5. Fastlane Implementation (`fastlane/Fastfile`)

Fastlane lanes are local-only helpers; no workflow invokes them. The Android lane uploads the real release AAB (`app/build/outputs/bundle/release/*.aab`) and fails loudly when none exists. There is no iOS upload lane until real IPA packaging exists — the release workflow produces a `ComposeApp.framework` zip, which is not an App Store IPA, so the previous `upload_ios` lane pointed at a phantom path and was removed.

---

## 6. Local Troubleshooting Summary

| Symptoms | Root Cause | Solution |
|---|---|---|
| Linter errors on Windows / CI | Text files use Windows `CRLF` instead of Unix `LF`. | `mise run format` will automatically convert all line endings to Unix format. |
| Local build fails due to Android SDK | `ANDROID_HOME` or `local.properties` is missing. | Clean or run Wasm compilation tasks (`gradle :app:compileKotlinWasmJs`) which do not require the Android SDK. |
| Kotlin script dependencies fail | Offline environment or maven download fail. | Verify internet access; maven packages are cached after the first execution. |
