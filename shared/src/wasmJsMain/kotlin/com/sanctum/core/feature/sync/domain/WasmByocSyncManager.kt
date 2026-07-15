package com.sanctum.core.feature.sync.domain

/**
 * WasmJs does not have access to native cloud providers (Google Drive / iCloud).
 * BYOC sync is a mobile-only feature. This stub satisfies the interface contract
 * so the DI graph compiles on all targets.
 */
class WasmByocSyncManager : ByocSyncManager {
    override suspend fun authenticateSilently(): Boolean = false
    override suspend fun backupDataToCloud(): Result<Unit> =
        Result.failure(UnsupportedOperationException("BYOC sync is not available on web."))
    override suspend fun restoreDataFromCloud(): Result<Unit> =
        Result.failure(UnsupportedOperationException("BYOC sync is not available on web."))
}
