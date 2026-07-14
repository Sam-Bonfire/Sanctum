import groovy.json.JsonSlurper

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

val flavorProp = project.findProperty("flavor")?.toString() ?: "islam"
val flavorsFile = rootProject.file("assets/flavors.json")
val parsedFlavors = JsonSlurper().parse(flavorsFile) as List<Map<String, Any>>
val currentFlavor = parsedFlavors.find { it["flavorId"] == flavorProp } ?: parsedFlavors.first()

val flavorAppId = currentFlavor["appId"].toString()
val flavorAppName = currentFlavor["appName"].toString()

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/source/buildConfig/commonMain")
    inputs.file(flavorsFile)
    outputs.dir(outputDir)
    doLast {
        val colors = currentFlavor["colors"] as Map<String, String>
        val term = currentFlavor["terminology"] as Map<String, String>
        val copy = currentFlavor["copy"] as Map<String, String>
        val features = (currentFlavor["features"] as? Map<String, Boolean>) ?: emptyMap()
        val hasCompass = features["hasCompass"] ?: false

        val file = file("${outputDir.get().asFile}/com/sanctum/app/BuildConfig.kt")
        file.parentFile.mkdirs()
        file.writeText(
            """
            package com.sanctum.app
            
            object BuildConfig {
                const val FLAVOR_ID = "${currentFlavor["flavorId"]}"
                const val APP_ID = "$flavorAppId"
                const val APP_NAME = "$flavorAppName"
                const val BRAND_NAME = "${currentFlavor["brandName"]}"
                const val BRAND_SUBTITLE = "${currentFlavor["brandSubtitle"]}"
                
                // Colors
                const val COLOR_PRIMARY = "${colors["primary"]}"
                const val COLOR_PRIMARY_VARIANT = "${colors["primaryVariant"]}"
                const val COLOR_BACKGROUND_LIGHT = "${colors["backgroundLight"]}"
                const val COLOR_BACKGROUND_DARK = "${colors["backgroundDark"]}"
                
                // Terminology
                const val TERM_SCRIPTURE_TITLE = "${term["scripture_title"]}"
                const val TERM_CHAPTER_UNIT = "${term["chapter_unit"]}"
                const val TERM_VERSE_UNIT = "${term["verse_unit"]}"
                const val TERM_DAILY_DEVOTION = "${term["daily_devotion"]}"
                const val TERM_SCHEDULE_TITLE = "${term["schedule_title"]}"
                
                // Copy
                const val COPY_WELCOME_MESSAGE = "${copy["welcome_message"]}"
                const val COPY_DAILY_MOTIVATION = "${copy["daily_motivation"]}"
                
                // Features
                const val HAS_COMPASS = $hasCompass
            }
            """.trimIndent(),
        )

        // Copy Assets to composeResources
        val assetDir = rootProject.file("assets/${currentFlavor["flavorId"]}")
        val drawableDir = file("src/commonMain/composeResources/drawable")
        val filesDir = file("src/commonMain/composeResources/files")
        drawableDir.mkdirs()
        filesDir.mkdirs()

        assetDir.listFiles { file -> file.name.endsWith(".png") }?.forEach { f ->
            f.copyTo(File(drawableDir, f.name), overwrite = true)
        }
        assetDir.listFiles { file -> file.name.endsWith(".db") }?.forEach { f ->
            f.copyTo(File(filesDir, f.name), overwrite = true)
        }
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "ComposeApp.js"
                devServer =
                    (devServer ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer()).apply {
                        open = false
                    }
            }
        }
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.configure {
            kotlin.srcDir(generateBuildConfig.map { it.outputs.files.singleFile })
        }
        commonMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.koin.compose)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.multiplatform.settings)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = "com.sanctum.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        applicationId = flavorAppId
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        resValue("string", "app_name", flavorAppName)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
