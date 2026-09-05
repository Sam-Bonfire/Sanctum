package com.sanctum.core.feature.prayer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.sanctum.core.feature.duas.domain.DailyDuaNotificationScheduler
import com.sanctum.core.feature.prayer.domain.AsrJuristicMethod
import com.sanctum.core.feature.prayer.domain.AudioPlayer
import com.sanctum.core.feature.prayer.domain.MuezzinVoice
import com.sanctum.core.feature.prayer.domain.NotificationAlertType
import com.sanctum.core.feature.prayer.domain.PrayerCalculationSettingsRepository
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
    val dailyDuaEnabled: Boolean = false,
    val dailyDuaHour: Int = 8,
    val dailyDuaMinute: Int = 0,
    val configTitle: String = "Daily Supplication",
    val asrJuristicMethod: AsrJuristicMethod = AsrJuristicMethod.STANDARD_SHAFII,
    val showAsrCalculationSetting: Boolean = false,
)

class PrayerNotificationViewModel(
    private val settingsRepository: PrayerNotificationSettingsRepository,
    private val audioPlayer: AudioPlayer,
    private val settings: Settings,
    private val scheduler: DailyDuaNotificationScheduler,
    private val calculationSettingsRepository: PrayerCalculationSettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrayerNotificationUiState())
    val uiState: StateFlow<PrayerNotificationUiState> = _uiState.asStateFlow()

    private val defaultPrayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha", "Shacharit", "Mincha", "Arvit", "Morning", "Noon")

    fun loadSettings(prayers: List<String> = defaultPrayers) {
        viewModelScope.launch {
            val settings = prayers.map { prayerName ->
                settingsRepository.getSetting(prayerName)
            }

            val isEnabled = this@PrayerNotificationViewModel.settings.getBoolean("daily_dua_enabled", false)
            val hour = this@PrayerNotificationViewModel.settings.getInt("daily_dua_hour", 8)
            val minute = this@PrayerNotificationViewModel.settings.getInt("daily_dua_minute", 0)
            val asrMethod = calculationSettingsRepository.getAsrJuristicMethod()
            val hasAsr = prayers.any { it.equals("Asr", ignoreCase = true) }

            _uiState.update {
                it.copy(
                    prayerSettings = settings,
                    dailyDuaEnabled = isEnabled,
                    dailyDuaHour = hour,
                    dailyDuaMinute = minute,
                    asrJuristicMethod = asrMethod,
                    showAsrCalculationSetting = hasAsr,
                )
            }
        }
    }

    fun updateAsrJuristicMethod(method: AsrJuristicMethod) {
        viewModelScope.launch {
            calculationSettingsRepository.saveAsrJuristicMethod(method)
            _uiState.update { it.copy(asrJuristicMethod = method) }
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

    fun setConfigTitle(title: String) {
        _uiState.update { it.copy(configTitle = title) }
    }

    fun updateDailyDuaEnabled(enabled: Boolean) {
        settings.putBoolean("daily_dua_enabled", enabled)
        _uiState.update { it.copy(dailyDuaEnabled = enabled) }
        viewModelScope.launch {
            scheduler.scheduleDailyNotification(_uiState.value.configTitle)
        }
    }

    fun updateDailyDuaTime(hour: Int, minute: Int) {
        settings.putInt("daily_dua_hour", hour)
        settings.putInt("daily_dua_minute", minute)
        _uiState.update { it.copy(dailyDuaHour = hour, dailyDuaMinute = minute) }
        viewModelScope.launch {
            scheduler.scheduleDailyNotification(_uiState.value.configTitle)
        }
    }

    override fun onCleared() {
        audioPlayer.stop()
        super.onCleared()
    }
}
