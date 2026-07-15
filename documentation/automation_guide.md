# Automation Guide for SEO Metadata Integration

This guide outlines how the Sanctum project automates the flow of SEO and store metadata from `assets/flavors.json` into Apple App Store and Google Play listings. It leverages **Mise** for consistent local/CI tool versioning, pure **Kotlin scripts** for verification/metadata generation, and **GitHub Actions** + **Fastlane** for the deployment pipeline.

---

## 1. Cadence & Branching Strategy

To keep the development pipeline fast and release costs low, Sanctum separates verification from deployment using a two-tiered cadence:

* **Development (Verification)**: Pushes and Pull Requests targeting the `dev` branch trigger verification checks (Spotless format linter, SEO syntax validation, and compilation of a single debug target flavor). *No release builds are built, and no App Store deployments occur.*
* **Production (Release)**: Merges or pushes to the `main` branch require validation and dev verification to pass first. Once verified, the runner uses a matrix build to compile release builds (AAB and iOS framework) for all 9 flavors and upload them to the respective stores via Fastlane.

### Managing this Cadence in Jujutsu (`jj`)

Jujutsu uses bookmarks which map 1-to-1 with Git branches. You can manage your release cadence using these commands:

1. **Develop Features on `dev`**:
   ```bash
   # Switch/create the local dev bookmark at your current commit
   jj bookmark set dev -r '@'
   
   # Write code, format, verify logic locally, then push to trigger CI verification
   jj git push --bookmark dev
   ```

2. **Deploy to Production (`main`)**:
   Once all features on the `dev` bookmark are verified and ready for release:
   ```bash
   # Move the main bookmark to the tip of dev
   jj bookmark set main -r dev
   
   # Push main to trigger the production deployment pipeline
   jj git push --bookmark main
   ```

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
* `mise run format:check`: Validates formatting without applying changes.
* `mise run seo:validate`: Validates that `flavors.json` meets all SEO requirements.
* `mise run seo:generate-metadata`: Generates local metadata files for App Store/Play Store upload.
* `mise run run:islam:web`: Starts the WasmJS web development server for the Islam flavor.
* `mise run build:islam:android`: Builds a debug APK locally for the Islam flavor.

---

## 4. GitHub Actions Workflow (`.github/workflows/build.yml`)

The pipeline runs on `ubuntu-latest` for verification steps (which is fast and cost-effective) and switches to `macos-latest` only for the release deployment job (required to compile iOS targets).

```yaml
name: Multi-Flavor CI/CD Pipeline

on:
  push:
    branches: [main, dev]
  pull_request:
    branches: [main, dev]

jobs:
  validate:
    name: Code Verification & SEO Validation
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup Mise (Java, Gradle, Kotlin)
        uses: jdx/mise-action@v4

      - name: Run Spotless Linter Checks
        run: gradle spotlessCheck

      - name: Run Kotlin SEO Verification Script
        run: kotlin scripts/validate_seo.main.kts

  build_dev:
    name: Dev Verification Builds
    runs-on: ubuntu-latest
    needs: validate
    if: github.ref == 'refs/heads/dev' || github.ref == 'refs/heads/main' || github.event_name == 'pull_request'
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup Mise (Java, Gradle, Kotlin)
        uses: jdx/mise-action@v4

      - name: Build Dev APK (Islam Flavor)
        run: gradle :app:assembleDebug -Pflavor=islam

      - name: Compile WASM Web Classes
        run: gradle :app:wasmJsMainClasses

  deploy_release:
    name: Production Release Deployment
    runs-on: macos-latest
    needs: [validate, build_dev]
    if: github.ref == 'refs/heads/main'
    strategy:
      matrix:
        flavor: [islam, christianity, hinduism, buddhism, jewish, sikhism, jainism, shinto, taoism]
      fail-fast: false
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup Mise (Java, Gradle, Kotlin)
        uses: jdx/mise-action@v4

      - name: Set up Ruby
        uses: actions/setup-ruby@v1
        with:
          ruby-version: '3.2.2'

      - name: Install Bundler & Fastlane
        run: |
          gem install bundler
          bundle install
        working-directory: fastlane

      - name: Generate App Store Metadata Files
        run: kotlin scripts/generate_fastlane_metadata.main.kts

      - name: Build Android Release AAB
        run: gradle :app:bundleRelease -Pflavor=${{ matrix.flavor }}

      - name: Build iOS Release Artifact
        run: gradle :app:assembleRelease -Pflavor=${{ matrix.flavor }}

      - name: Run Fastlane Google Play Deploy
        env:
          PLAY_STORE_SERVICE_ACCOUNT_JSON: ${{ secrets.PLAY_STORE_SERVICE_ACCOUNT_JSON }}
        run: bundle exec fastlane android upload_android flavor:${{ matrix.flavor }}
        working-directory: fastlane
        continue-on-error: true

      - name: Run Fastlane Apple App Store Deploy
        env:
          APP_STORE_CONNECT_USERNAME: ${{ secrets.APP_STORE_CONNECT_USERNAME }}
          APP_STORE_CONNECT_API_KEY: ${{ secrets.APP_STORE_CONNECT_API_KEY }}
        run: bundle exec fastlane ios upload_ios flavor:${{ matrix.flavor }}
        working-directory: fastlane
        continue-on-error: true
```

---

## 5. Fastlane Implementation (`fastlane/Fastfile`)

Fastlane handles store integration without duplicating extraction code. It relies on the metadata files pre-generated by your Kotlin scripts:

```ruby
default_platform(:ios)

platform :ios do
  desc "Upload iOS metadata and binary"
  lane :upload_ios do |options|
    flavor = options[:flavor]
    app_id = JSON.parse(File.read('assets/flavors.json')).find { |f| f['flavorId'] == flavor }['appId']
    upload_to_app_store(
      username: ENV['APP_STORE_CONNECT_USERNAME'],
      app_identifier: app_id,
      ipa: "app/build/outputs/ipa/#{flavor}/Release/app.ipa",
      metadata_path: "fastlane/metadata/ios/#{flavor}",
      skip_screenshots: true,
      skip_metadata: false
    )
  end
end

platform :android do
  desc "Upload Android metadata and AAB"
  lane :upload_android do |options|
    flavor = options[:flavor]
    app_id = JSON.parse(File.read('assets/flavors.json')).find { |f| f['flavorId'] == flavor }['appId']
    supply(
      json_key: ENV['PLAY_STORE_SERVICE_ACCOUNT_JSON'],
      package_name: app_id,
      aab: "app/build/outputs/bundle/#{flavor}/Release/app.aab",
      metadata_path: "fastlane/metadata/android/#{flavor}",
      track: 'internal'
    )
  end
end
```

---

## 6. Local Troubleshooting Summary

| Symptoms | Root Cause | Solution |
|---|---|---|
| Linter errors on Windows / CI | Text files use Windows `CRLF` instead of Unix `LF`. | `mise run format` will automatically convert all line endings to Unix format. |
| Local build fails due to Android SDK | `ANDROID_HOME` or `local.properties` is missing. | Clean or run Wasm compilation tasks (`gradle :app:compileKotlinWasmJs`) which do not require the Android SDK. |
| Kotlin script dependencies fail | Offline environment or maven download fail. | Verify internet access; maven packages are cached after the first execution. |
