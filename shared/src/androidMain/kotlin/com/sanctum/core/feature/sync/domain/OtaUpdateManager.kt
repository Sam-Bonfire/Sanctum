package com.sanctum.core.feature.sync.domain

import com.sanctum.core.core.database.applicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

actual suspend fun saveDatabaseUpdateBytes(bytes: ByteArray): Boolean = withContext(Dispatchers.IO) {
    try {
        val dbPath = applicationContext.getDatabasePath("prayer_update.db")
        dbPath.outputStream().use { output ->
            output.write(bytes)
        }
        true
    } catch (e: Exception) {
        false
    }
}

actual fun generateSha256(bytes: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(bytes)
    return hash.joinToString("") { "%02x".format(it) }
}
