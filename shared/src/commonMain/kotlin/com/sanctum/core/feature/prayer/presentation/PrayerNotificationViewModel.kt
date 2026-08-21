package com.sanctum.core.feature.prayer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanctum.core.feature.prayer.domain.AudioPlayer
import com.sanctum.core.feature.prayer.domain.MuezzinVoice
import com.sanctum.core.feature.prayer.domain.NotificationAlertType
import com.sanctum.core.feature.prayer.domain.PrayerNotificationSetting
import com.sanctum.core.feature.prayer.domain.PrayerNotificationSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PrayerNotificationUiState(
    val prayerSettings: List<PrayerNotificationSetting> = emptyList(),
    val playingVoice: MuezzinVoice? = null,
)

class PrayerNotificationViewModel(
    private val settingsRepository: PrayerNotificationSettingsRepository,
    private val audioPlayer: AudioPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerNotificationUiState())
    val uiState: StateFlow<PrayerNotificationUiState> = _uiState.asStateFlow()

    private val defaultPrayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha", "Shacharit", "Mincha", "Arvit", "Morning", "Noon")

    fun loadSettings(prayers: List<String> = defaultPrayers) {
        viewModelScope.launch {
            val settings = prayers.map { prayerName ->
                settingsRepository.getSetting(prayerName)
            }
            _uiState.update { it.copy(prayerSettings = settings) }
        }
    }

    fun updateAlertType(prayerName: String, alertType: NotificationAlertType) {
        viewModelScope.launch {
            val currentSetting = settingsRepository.getSetting(prayerName)
            val newSetting = currentSetting.copy(alertType = alertType)
            settingsRepository.saveSetting(newSetting)
            loadSettings(_uiState.value.prayerSettings.map { it.prayerName })
        }
    }

    fun updateMuezzinVoice(prayerName: String, voice: MuezzinVoice) {
        viewModelScope.launch {
            val currentSetting = settingsRepository.getSetting(prayerName)
            val newSetting = currentSetting.copy(muezzinVoice = voice)
            settingsRepository.saveSetting(newSetting)
            loadSettings(_uiState.value.prayerSettings.map { it.prayerName })
        }
    }

    fun toggleAudioPreview(voice: MuezzinVoice) {
        viewModelScope.launch {
            if (_uiState.value.playingVoice == voice) {
                audioPlayer.stop()
                _uiState.update { it.copy(playingVoice = null) }
            } else {
                audioPlayer.stop()
                audioPlayer.play(voice.fileName)
                _uiState.update { it.copy(playingVoice = voice) }
            }
        }
    }

    override fun onCleared() {
        audioPlayer.stop()
        super.onCleared()
    }
}
