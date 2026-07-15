package com.sanctum.core.feature.sync.domain

actual suspend fun saveDatabaseUpdateBytes(bytes: ByteArray): Boolean {
    // OTA updates via FileSystem not supported natively on Wasm/Web.
    // In a real app we'd use IndexedDB via Kotlin/JS or WebCrypto.
    println("OTA database update is not supported on Wasm.")
    return false
}

actual fun generateSha256(bytes: ByteArray): String {
    // WebCrypto API needed for actual SHA-256 in Wasm
    return "unsupported_on_wasm"
}
