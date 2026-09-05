package com.sanctum.core.feature.compass.domain

import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object QiblaMath {
    // Exact coordinates of the Kaaba in Mecca
    private const val KAABA_LAT = 21.422487
    private const val KAABA_LNG = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Calculates the bearing to the Qibla (Kaaba) from a given GPS coordinate.
     * The result is the angle in degrees from True North (0 to 360).
     */
    fun calculateQiblaDirection(latitude: Double, longitude: Double): Double {
        val kaabaLatRad = degreesToRadians(KAABA_LAT)
        val kaabaLngRad = degreesToRadians(KAABA_LNG)
        val latRad = degreesToRadians(latitude)
        val lngRad = degreesToRadians(longitude)

        val y = sin(kaabaLngRad - lngRad)
        val x = cos(latRad) * tan(kaabaLatRad) - sin(latRad) * cos(kaabaLngRad - lngRad)

        var qibla = radiansToDegrees(atan2(y, x))

        // Normalize to 0 - 360
        if (qibla < 0) {
            qibla += 360.0
        }

        return qibla
    }

    /**
     * Calculates the great-circle distance to the Qibla in kilometers.
     */
    fun calculateDistanceKm(latitude: Double, longitude: Double): Double {
        val lat1 = degreesToRadians(latitude)
        val lng1 = degreesToRadians(longitude)
        val lat2 = degreesToRadians(KAABA_LAT)
        val lng2 = degreesToRadians(KAABA_LNG)

        // Spherical Law of Cosines
        val angle = acos(
            sin(lat1) * sin(lat2) +
                cos(lat1) * cos(lat2) * cos(lng2 - lng1),
        )

        return angle * EARTH_RADIUS_KM
    }

    private fun degreesToRadians(degrees: Double): Double {
        return degrees * kotlin.math.PI / 180.0
    }

    private fun radiansToDegrees(radians: Double): Double {
        return radians * 180.0 / kotlin.math.PI
    }
}
