package com.sanctum.core.feature.sync.domain

/**
 * Bring Your Own Cloud (BYOC) Sync Manager.
 * Handles backing up and restoring the user's personal data (bookmarks, settings)
 * to their native cloud provider (Google Drive on Android, iCloud on iOS)
 * without requiring a central backend server.
 */
interface ByocSyncManager {
    /**
     * Authenticates the user with their native OS cloud provider.
     * @return true if successful, false if user denied permission or offline.
     */
    suspend fun authenticateSilently(): Boolean

    /**
     * Uploads the current UserData Room Database to a hidden app-data folder.
     */
    suspend fun backupDataToCloud(): Result<Unit>

    /**
     * Downloads the latest backup from the cloud and overwrites the local database.
     */
    suspend fun restoreDataFromCloud(): Result<Unit>
}
