package com.sanctum.core.feature.compass

import com.sanctum.core.feature.compass.domain.GeoLocation
import com.sanctum.core.feature.compass.domain.PlatformSensors
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

class IosPlatformSensors : PlatformSensors {

    private val locationManager = CLLocationManager()

    override val deviceHeading: Flow<Float?> = callbackFlow {
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
                // trueHeading is relative to true north, magneticHeading to magnetic north
                val heading = if (didUpdateHeading.trueHeading >= 0) {
                    didUpdateHeading.trueHeading
                } else {
                    didUpdateHeading.magneticHeading
                }
                trySend(heading.toFloat())
            }
        }

        locationManager.delegate = delegate
        if (CLLocationManager.headingAvailable()) {
            locationManager.startUpdatingHeading()
        }

        awaitClose {
            locationManager.stopUpdatingHeading()
            locationManager.delegate = null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getCurrentLocation(): Result<GeoLocation> {
        val location = locationManager.location
        return if (location != null) {
            location.coordinate.useContents {
                Result.success(GeoLocation(latitude, longitude))
            }
        } else {
            Result.failure(Exception("No location available. Check permissions."))
        }
    }
}
