package com.sanctum.core.feature.compass.domain

import kotlin.test.Test
import kotlin.test.assertTrue

class QiblaMathTest {

    @Test
    fun testQiblaDirectionFromLondon() {
        // London Coordinates
        val lat = 51.5074
        val lng = -0.1278

        val qiblaDirection = QiblaMath.calculateQiblaDirection(lat, lng)

        // Qibla from London is approximately 119 degrees East of True North
        assertTrue(qiblaDirection in 118.0..120.0, "Expected Qibla direction from London to be around 119 degrees, but was $qiblaDirection")
    }

    @Test
    fun testQiblaDirectionFromNewYork() {
        // New York Coordinates
        val lat = 40.7128
        val lng = -74.0060

        val qiblaDirection = QiblaMath.calculateQiblaDirection(lat, lng)

        // Qibla from New York is approximately 58.5 degrees
        assertTrue(qiblaDirection in 57.0..60.0, "Expected Qibla direction from New York to be around 58.5 degrees, but was $qiblaDirection")
    }

    @Test
    fun testQiblaDirectionFromTokyo() {
        // Tokyo Coordinates
        val lat = 35.6762
        val lng = 139.6503

        val qiblaDirection = QiblaMath.calculateQiblaDirection(lat, lng)

        // Qibla from Tokyo is approximately 293 degrees
        assertTrue(qiblaDirection in 292.0..294.0, "Expected Qibla direction from Tokyo to be around 293 degrees, but was $qiblaDirection")
    }
}
