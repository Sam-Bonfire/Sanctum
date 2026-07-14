package com.sanctum.core.feature.sync.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import io.ktor.http.isSuccess

/**
 * Platform specific save logic to write the database staging file safely.
 */
expect suspend fun saveDatabaseUpdateBytes(bytes: ByteArray): Boolean

/**
 * Platform specific SHA-256 hash generator.
 */
expect fun generateSha256(bytes: ByteArray): String

/**
 * OtaUpdateManager handles downloading and verifying database updates via Ktor.
 */
class OtaUpdateManager(private val httpClient: HttpClient = HttpClient()) {

    suspend fun checkForAndDownloadDatabaseUpdate(cdnUrl: String, localVersion: Int, expectedChecksum: String): Boolean {
        try {
            val response: HttpResponse = httpClient.get(cdnUrl)

            if (response.status.isSuccess()) {
                val bytes = response.readRawBytes()

                // Security Step: Checksum Validation (MD5/SHA-256)
                val downloadedChecksum = generateSha256(bytes)
                if (downloadedChecksum.equals(expectedChecksum, ignoreCase = true)) {
                    return saveDatabaseUpdateBytes(bytes)
                }
            }
        } catch (e: Exception) {
            // Log networking exception silently
        }
        return false
    }
}
