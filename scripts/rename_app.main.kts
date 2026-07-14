#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")

import java.io.File

val srcDirs = listOf(
    File("app/src/androidMain/kotlin/com/sanctum/selah"),
    File("app/src/commonMain/kotlin/com/sanctum/selah"),
    File("app/src/iosMain/kotlin/com/sanctum/selah")
)

val destDirs = listOf(
    File("app/src/androidMain/kotlin/com/sanctum/app"),
    File("app/src/commonMain/kotlin/com/sanctum/app"),
    File("app/src/iosMain/kotlin/com/sanctum/app")
)

for ((src, dest) in srcDirs.zip(destDirs)) {
    if (src.exists()) {
        dest.parentFile.mkdirs()
        src.renameTo(dest)
    }
}

val appSrcDir = File("app/src")
if (appSrcDir.exists()) {
    appSrcDir.walkTopDown().forEach { file ->
        if (file.isFile && (file.extension == "kt" || file.extension == "xml" || file.extension == "html")) {
            var content = file.readText(Charsets.UTF_8)
            content = content.replace("com.sanctum.selah.app", "com.sanctum.app")
            content = content.replace("com.sanctum.selah", "com.sanctum.app")
            file.writeText(content, Charsets.UTF_8)
        }
    }
}
