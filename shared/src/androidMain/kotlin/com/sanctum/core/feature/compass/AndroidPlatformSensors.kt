package com.sanctum.core.feature.compass

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import com.sanctum.core.feature.compass.domain.GeoLocation
import com.sanctum.core.feature.compass.domain.PlatformSensors
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidPlatformSensors(private val context: Context) : PlatformSensors {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override val deviceHeading: Flow<Float?> = callbackFlow {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var lastAccelerometer: FloatArray? = null
        var lastMagnetometer: FloatArray? = null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    lastAccelerometer = event.values.clone()
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    lastMagnetometer = event.values.clone()
                }

                if (lastAccelerometer != null && lastMagnetometer != null) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    val success = SensorManager.getRotationMatrix(r, i, lastAccelerometer, lastMagnetometer)
                    if (success) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        val azimuthInRadians = orientation[0]
                        var azimuthInDegress = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
                        if (azimuthInDegress < 0.0f) {
                            azimuthInDegress += 360.0f
                        }
                        trySend(azimuthInDegress)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    override suspend fun getCurrentLocation(): Result<GeoLocation> = suspendCancellableCoroutine { continuation ->
        val hasPermission = context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            continuation.resume(Result.failure(Exception("Location permission denied")))
            return@suspendCancellableCoroutine
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Try getting last known location first for speed
        var location: Location? = null
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider)
            if (l != null) {
                if (location == null || l.accuracy < location.accuracy) {
                    location = l
                }
            }
        }

        if (location != null) {
            continuation.resume(Result.success(GeoLocation(location.latitude, location.longitude)))
        } else {
            // Simplified for brevity in MVP - real world requires location updates listener
            continuation.resume(Result.failure(Exception("No cached location available")))
        }
    }
}
