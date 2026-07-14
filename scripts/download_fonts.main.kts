#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")

import java.io.File
import java.net.URL

val fontDir = File("shared/src/commonMain/composeResources/font")
fontDir.mkdirs()

val fonts = mapOf(
    "playfair_regular.ttf" to "https://github.com/google/fonts/raw/main/ofl/playfairdisplay/PlayfairDisplay-Regular.ttf",
    "playfair_bold.ttf" to "https://github.com/google/fonts/raw/main/ofl/playfairdisplay/PlayfairDisplay-Bold.ttf",
    "inter_regular.ttf" to "https://github.com/google/fonts/raw/main/ofl/inter/static/Inter-Regular.ttf",
    "inter_medium.ttf" to "https://github.com/google/fonts/raw/main/ofl/inter/static/Inter-Medium.ttf",
    "inter_bold.ttf" to "https://github.com/google/fonts/raw/main/ofl/inter/static/Inter-Bold.ttf",
    "amiri_regular.ttf" to "https://github.com/google/fonts/raw/main/ofl/amiri/Amiri-Regular.ttf"
)

for ((filename, urlString) in fonts) {
    println("Downloading $filename...")
    val file = File(fontDir, filename)
    try {
        val url = URL(urlString)
        url.openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        println("Saved ${file.absolutePath}")
    } catch (e: Exception) {
        println("Failed to download $filename: ${e.message}")
    }
}
