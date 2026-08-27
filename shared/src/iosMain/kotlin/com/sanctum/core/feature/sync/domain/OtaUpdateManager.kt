package com.sanctum.core.feature.sync.domain

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
actual suspend fun saveDatabaseUpdateBytes(bytes: ByteArray): Boolean {
    try {
        val fileManager = NSFileManager.defaultManager()
        val documentDirectory = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        val updateDbUrl = documentDirectory?.URLByAppendingPathComponent("prayer_update.db")
        val updateDbPath = updateDbUrl?.path

        if (updateDbPath != null) {
            val nsData = bytes.usePinned { pinned ->
                NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
            }
            nsData.writeToFile(updateDbPath, atomically = true)
            return true
        }
    } catch (e: Exception) {
        // Log silently
    }
    return false
}

@OptIn(ExperimentalForeignApi::class)
actual fun generateSha256(bytes: ByteArray): String {
    val uBytes = bytes.asUByteArray()
    val digest = UByteArray(32) // SHA-256 produces 32 bytes
    uBytes.usePinned { pinnedBytes ->
        digest.usePinned { pinnedDigest ->
            platform.CoreCrypto.CC_SHA256(
                pinnedBytes.addressOf(0),
                bytes.size.toUInt(),
                pinnedDigest.addressOf(0),
            )
        }
    }
    return digest.joinToString("") { byte ->
        val hex = byte.toString(16)
        if (hex.length < 2) "0$hex" else hex
    }
}
