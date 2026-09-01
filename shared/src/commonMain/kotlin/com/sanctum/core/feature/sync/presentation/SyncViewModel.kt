package com.sanctum.core.feature.sync.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.sync.domain.ByocSyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val message: String) : SyncState()
    data class Error(val message: String) : SyncState()
}

class SyncViewModel(private val syncManager: ByocSyncManager) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _cloudProvider = MutableStateFlow("Google Drive")
    val cloudProvider: StateFlow<String> = _cloudProvider.asStateFlow()

    private val _isAutoBackupEnabled = MutableStateFlow(false)
    val isAutoBackupEnabled: StateFlow<Boolean> = _isAutoBackupEnabled.asStateFlow()

    fun backupNow() {
        viewModelScope.launch {
            _syncState.value = SyncState.Syncing
            val result = syncManager.backupDataToCloud()
            if (result.isSuccess) {
                _syncState.value = SyncState.Success("Data safely backed up to Cloud.")
            } else {
                _syncState.value = SyncState.Error("Failed to backup: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun restoreNow() {
        viewModelScope.launch {
            _syncState.value = SyncState.Syncing
            val result = syncManager.restoreDataFromCloud()
            if (result.isSuccess) {
                _syncState.value = SyncState.Success("Data successfully restored from Cloud.")
            } else {
                _syncState.value = SyncState.Error("Failed to restore: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun setProvider(provider: String) {
        _cloudProvider.value = provider
        syncManager.setCloudProvider(provider)
    }

    fun toggleAutoBackup(enabled: Boolean) {
        _isAutoBackupEnabled.value = enabled
        syncManager.setAutomaticBackup(enabled)
    }
}
