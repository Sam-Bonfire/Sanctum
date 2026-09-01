package com.sanctum.core.feature.sync.data

import com.sanctum.core.feature.sync.domain.ByocSyncManager
import kotlinx.coroutines.delay

class MockSyncManager(private val dataExporter: DataExporter) : ByocSyncManager {

    // In a real implementation this is the literal user's Google Drive / iCloud Ubiquity Container.
    private var mockCloudStorage: String? = null

    private var currentProvider: String = "Default"
    private var isAutoBackupEnabled: Boolean = false

    override suspend fun authenticateSilently(): Boolean {
        delay(500) // Simulate API handshake
        return true
    }

    override suspend fun backupDataToCloud(): Result<Unit> {
        delay(1500) // Simulate file upload
        return try {
            mockCloudStorage = dataExporter.exportDataToJson()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreDataFromCloud(): Result<Unit> {
        delay(1500) // Simulate file download
        return try {
            mockCloudStorage?.let { json ->
                dataExporter.importDataFromJson(json)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun setCloudProvider(provider: String) {
        currentProvider = provider
    }

    override fun setAutomaticBackup(enabled: Boolean) {
        isAutoBackupEnabled = enabled
    }
}
