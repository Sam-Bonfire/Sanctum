package com.sanctum.core.feature.compass.domain

import kotlinx.coroutines.flow.Flow

/**
 * Represents a geographic coordinate.
 */
data class GeoLocation(val latitude: Double, val longitude: Double)

/**
 * Abstraction for device sensors required by the Qibla Compass.
 * Platform-specific modules (Android, iOS, WasmJs) will implement this interface.
 */
interface PlatformSensors {

    /**
     * Emits the current device heading in degrees relative to True North (0.0 to 360.0).
     * If the hardware (like a desktop PC) does not have a compass/magnetometer,
     * this flow may emit a default value or not emit at all.
     */
    val deviceHeading: Flow<Float?>

    /**
     * Requests the current geolocation from the platform.
     * @return Result containing GeoLocation on success, or an Exception if permission denied or unavailable.
     */
    suspend fun getCurrentLocation(): Result<GeoLocation>
}
