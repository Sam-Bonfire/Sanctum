package com.sanctum.core.feature.scripture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.sanctum.core.core.notifications.PlatformNotificationManager
import com.sanctum.core.feature.compass.domain.PlatformSensors
import com.sanctum.core.feature.scripture.domain.PrayerScheduleUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DashboardUiState(
    val upcomingPrayerName: String = "",
    val hoursRemaining: String = "00",
    val minutesRemaining: String = "00",
    val secondsRemaining: String = "00",
    val nextPrayerFormatted: String = "",
    val prayers: List<PrayerTime> = emptyList(),
    val location: String = "",
    val dateString: String = "",
    val isLoading: Boolean = true,
    val locationError: Boolean = false,
    val scheduleError: Boolean = false,
)

class DashboardViewModel(
    private val prayerScheduleUseCase: PrayerScheduleUseCase,
    private val platformSensors: PlatformSensors,
    private val notificationManager: PlatformNotificationManager,
    private val settings: Settings,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = now.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val dateString = "${now.dayOfWeek.name.take(3)} ${now.dayOfMonth} $month"
        _uiState.update { it.copy(dateString = dateString) }
        initializeSchedule()
    }

    fun retrySchedule() {
        _uiState.update { it.copy(scheduleError = false, locationError = false, isLoading = true) }
        initializeSchedule()
    }

    private fun initializeSchedule() {
        viewModelScope.launch {
            val religionId = settings.getString("religion_id", "")
            if (religionId.isEmpty()) {
                // Onboarding hasn't completed yet — do not guess
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val locationResult = platformSensors.getCurrentLocation()
            if (locationResult.isFailure) {
                // Check if user saved a manual location during onboarding
                val savedLat = settings.getDoubleOrNull("location_lat")
                val savedLon = settings.getDoubleOrNull("location_lon")
                val savedName = settings.getStringOrNull("location_name")

                if (savedLat == null || savedLon == null) {
                    _uiState.update { it.copy(isLoading = false, locationError = true) }
                    return@launch
                }
                loadSchedule(savedLat, savedLon, savedName ?: "", religionId)
            } else {
                val loc = locationResult.getOrThrow()
                // "Current Location" is resolved on-screen via a geocoder if needed;
                // the raw coordinates are what matters for calculation.
                loadSchedule(loc.latitude, loc.longitude, "Current Location", religionId)
            }
        }
    }

    private suspend fun loadSchedule(lat: Double, lon: Double, locationName: String, religionId: String) {
        try {
            val schedule = prayerScheduleUseCase.calculateSchedule(
                latitude = lat,
                longitude = lon,
                religionId = religionId,
            )
            _uiState.update { state ->
                state.copy(prayers = schedule, location = locationName, isLoading = false)
            }
            scheduleNotificationsForToday(schedule)
            startRealCountdown(schedule)
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, scheduleError = true) }
        }
    }

    private fun scheduleNotificationsForToday(schedule: List<PrayerTime>) {
        val now = Clock.System.now().toEpochMilliseconds()
        schedule.forEachIndexed { index, prayer ->
            val targetMillis = prayerScheduleUseCase.parsePrayerTimeToMillis(prayer)
            if (targetMillis != null && targetMillis > now) {
                notificationManager.scheduleNotification(
                    id = index,
                    title = "Prayer Time",
                    message = "It is now time for ${prayer.name}.",
                    triggerTimeInMillis = targetMillis,
                )
            }
        }
    }

    private fun startRealCountdown(schedule: List<PrayerTime>) {
        flow {
            while (true) {
                emit(prayerScheduleUseCase.getNextPrayerAndRemainingTime(schedule))
                delay(1000L)
            }
        }.onEach { (nextPrayer, duration) ->
            if (nextPrayer != null) {
                val totalSeconds = duration.inWholeSeconds
                val h = totalSeconds / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60
                _uiState.update { state ->
                    state.copy(
                        upcomingPrayerName = nextPrayer.name,
                        nextPrayerFormatted = nextPrayer.time,
                        hoursRemaining = h.toString().padStart(2, '0'),
                        minutesRemaining = m.toString().padStart(2, '0'),
                        secondsRemaining = s.toString().padStart(2, '0'),
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
