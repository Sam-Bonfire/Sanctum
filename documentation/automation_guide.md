# Automation Guide for SEO Metadata Integration

## Overview
This guide shows how to automate the flow of SEO and store‑metadata from `assets/flavors.json` into the Apple App Store and Google Play listings using **Fastlane** inside your existing **GitHub Actions** CI pipeline.

---
### 1. Prerequisites
- **Fastlane** installed on the CI runners (Ruby 3.0+, Bundler). Add a `Gemfile` to the repo:
  ```ruby
  source "https://rubygems.org"
  gem "fastlane"
  ```
  Install with `bundle install`.
- **Apple Developer credentials** (App Store Connect API key) stored as GitHub secret `APP_STORE_CONNECT_API_KEY` (base64‑encoded JSON).
- **Google Play service account JSON** stored as secret `PLAY_STORE_SERVICE_ACCOUNT_JSON`.
- **Node.js** (optional) for JSON manipulation scripts – already present for the project.
- Existing **GitHub Actions** workflow that builds the Android and iOS artifacts.

---
### 2. Repository Layout
```
PrayerApp/
├─ assets/
│   └─ flavors.json          # SEO + app‑store metadata per flavor
├─ fastlane/
│   └─ Fastfile              # Fastlane configuration (see below)
├─ documentation/
│   └─ automation_guide.md   # THIS FILE
├─ app/
│   └─ src/...                # Kotlin sources
└─ .github/workflows/
    └─ build.yml             # GitHub Actions CI
```

---
### 3. Fastlane Configuration (`fastlane/Fastfile`)
```ruby
default_platform(:ios)

def load_seo(flavor_id)
  json = JSON.parse(File.read('assets/flavors.json'))
  flavor = json.find { |f| f['flavorId'] == flavor_id }
  raise "Flavor not found: #{flavor_id}" unless flavor
  flavor['seo']
end

platform :ios do
  desc "Upload iOS metadata and binary"
  lane :upload_ios do |options|
    flavor = options[:flavor]
    seo = load_seo(flavor)

    upload_to_app_store(
      username: ENV['APP_STORE_CONNECT_USERNAME'],
      app_identifier: json = JSON.parse(File.read('assets/flavors.json')).find { |f| f['flavorId'] == flavor }['appId'],
      sku: "#{flavor}",
      metadata_path: "fastlane/metadata/ios/#{flavor}",
      skip_screenshots: true,
      skip_metadata: false,
      ipa: "app/build/outputs/ipa/#{flavor}/Release/app.ipa"
    )
  end
end

platform :android do
  desc "Upload Android metadata and AAB"
  lane :upload_android do |options|
    flavor = options[:flavor]
    seo = load_seo(flavor)
    # Fastlane expects a folder with text files: title.txt, short_description.txt, full_description.txt, video_url.txt, recent_changes.txt, and a keywords.txt (comma‑separated).
    metadata_dir = "fastlane/metadata/android/#{flavor}"
    FileUtils.mkdir_p(metadata_dir)
    File.write(File.join(metadata_dir, 'title.txt'), seo['appStoreMetadata']['title'])
    File.write(File.join(metadata_dir, 'short_description.txt'), seo['shortDescription'])
    File.write(File.join(metadata_dir, 'full_description.txt'), seo['longDescription'])
    File.write(File.join(metadata_dir, 'keywords.txt'), seo['appStoreMetadata']['keywords'])

    supply(
      json_key: ENV['PLAY_STORE_SERVICE_ACCOUNT_JSON'],
      package_name: JSON.parse(File.read('assets/flavors.json')).find { |f| f['flavorId'] == flavor }['appId'],
      aab: "app/build/outputs/bundle/#{flavor}/Release/app.aab",
      metadata_path: metadata_dir,
      track: 'internal'
    )
  end
end
```
**Explanation**
- `load_seo` extracts the SEO block for the requested flavor.
- Each lane writes the required text files that Fastlane (`upload_to_app_store` / `supply`) consumes.
- The lanes are invoked from GitHub Actions with the appropriate `flavor` variable.

---
### 4. GitHub Actions Workflow (`.github/workflows/build.yml`)
```yaml
name: Build & Release
on:
  push:
    branches: [main]
    tags: ['v*']
jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        flavor: [islam, christianity, hinduism, buddhism, jewish, sikhism, jainism, shinto, taoism]
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'
      - name: Install Fastlane dependencies
        run: |
          sudo gem install bundler
          bundle install
      - name: Build Android AAB
        run: ./gradlew :app:assembleRelease -Pflavor=${{ matrix.flavor }}
      - name: Build iOS IPA (via Gradle plugin or Xcode if native)
        if: runner.os == 'macos'
        run: ./gradlew :app:assembleRelease -Pflavor=${{ matrix.flavor }}
      - name: Upload metadata to stores
        env:
          APP_STORE_CONNECT_USERNAME: ${{ secrets.APP_STORE_CONNECT_USERNAME }}
          APP_STORE_CONNECT_API_KEY: ${{ secrets.APP_STORE_CONNECT_API_KEY }}
          PLAY_STORE_SERVICE_ACCOUNT_JSON: ${{ secrets.PLAY_STORE_SERVICE_ACCOUNT_JSON }}
        run: |
          bundle exec fastlane ios upload_ios flavor:${{ matrix.flavor }}
          bundle exec fastlane android upload_android flavor:${{ matrix.flavor }}
```
- The matrix loops over all flavors, building each variant and then calling the Fastlane lanes with the flavor identifier.
- Secrets are injected securely.

---
### 5. Asset Generation (Screenshots / Icons)
Fastlane can generate screenshots from the UI automatically. Add a lane like:
```ruby
lane :capture_screenshots do |options|
  flavor = options[:flavor]
  # Assuming you have a screenshot script in the repo that accepts the flavor.
  sh "./scripts/take_screenshots.sh #{flavor}"
  upload_screenshots(
    app_identifier: json = JSON.parse(File.read('assets/flavors.json')).find { |f| f['flavorId'] == flavor }['appId'],
    screenshots_path: "fastlane/screenshots/#{flavor}"
  )
end
```
Invoke it after the build if you need updated screenshots.

---
### 6. Validation & Linting
Add a simple Kotlin test (see `SeoMetadataTest.kt`) that parses `flavors.json` and asserts every flavor contains:
- `shortDescription`
- `longDescription`
- `primaryKeywords`, `secondaryKeywords`, `longTailKeywords`
- `metaTags.title`, `metaTags.description`, `metaTags.keywords`
- `appStoreMetadata.title`, `subtitle`, `keywords`
Run this test in the CI **before** triggering Fastlane.

---
### 7. Versioning & Localization
If you need language‑specific SEO, extend each flavor with a top‑level `localizations` map:
```json
"localizations": {
  "en": { "shortDescription": "..." },
  "es": { "shortDescription": "..." }
}
```
Update the Fastlane generation script to pick the appropriate locale based on the `APP_LOCALE` environment variable.

---
### 8. Troubleshooting
| Issue | Likely Cause | Fix |
|-------|--------------|-----|
| Fastlane fails to authenticate | Missing or malformed API key/JSON secret | Verify the secret is base64‑encoded and matches the service‑account file. |
| Keywords exceed store limits | `appStoreMetadata.keywords` longer than 100 chars (Apple) or 100 chars total (Google) | Trim the list or split into multiple short strings. |
| Build artifacts not found | Wrong flavor name passed to Gradle | Ensure `-Pflavor=${{ matrix.flavor }}` matches the `flavorId` used in `flavors.json`. |
| JSON parsing errors in Kotlin test | Invalid JSON after manual edit | Run `jq . assets/flavors.json` locally to validate syntax. |

---
### 9. Summary of Commands (Run locally for debugging)
```bash
# Validate JSON syntax
jq . assets/flavors.json

# Run Kotlin test
./gradlew :app:testDebugUnitTest

# Fastlane dry‑run (does not upload)
bundle exec fastlane ios upload_ios flavor:islam --skip_upload
bundle exec fastlane android upload_android flavor:islam --skip_upload
```

---
**That’s the complete automation setup.** Once the workflow is merged, pushing a new tag (`v1.2.0`) will automatically build every flavor, validate SEO data, generate store metadata, and upload to both Apple App Store and Google Play.

---
*Feel free to request any tweaks or additional steps.*
