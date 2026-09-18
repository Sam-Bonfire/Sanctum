package com.sanctum.core.feature.compass.domain

import kotlin.math.abs

/**
 * Calculates AR Qibla parameters based on the current device heading and Qibla bearing.
 */
object ArQiblaCalculator {

    /**
     * The assumed horizontal field of view of the device camera in degrees.
     */
    const val CAMERA_FOV_DEGREES = 60f

    /**
     * Tolerance in degrees for the user to be considered "aligned" with the target.
     */
    const val ALIGNMENT_TOLERANCE_DEGREES = 2f

    /**
     * Represents the calculated state for the AR Viewport.
     * @property relativeBearing The relative angle in degrees from the center of the camera viewport to the Qibla (-180 to +180). Positive means Qibla is to the right.
     * @property elevationAngle The elevation angle in degrees.
     * @property isVisible True if the Qibla is currently within the camera's field of view.
     * @property isAligned True if the device is pointing almost exactly at the Qibla (within [ALIGNMENT_TOLERANCE_DEGREES]).
     * @property horizontalOffsetRatio The horizontal position of the target relative to the viewport center (-1.0 to 1.0).
     *                                 0.0 is dead center. -1.0 is left edge, 1.0 is right edge.
     */
    data class ArViewportState(
        val relativeBearing: Float,
        val elevationAngle: Float,
        val isVisible: Boolean,
        val isAligned: Boolean,
        val horizontalOffsetRatio: Float,
    )

    /**
     * Calculates the viewport state given the absolute Qibla bearing and the device's current heading.
     *
     * @param qiblaBearing The absolute bearing to Qibla (0-360 degrees).
     * @param deviceHeading The absolute current heading of the device camera (0-360 degrees).
     * @return The [ArViewportState] representing where the Qibla is relative to the camera.
     */
    fun calculateViewportState(qiblaBearing: Double, deviceHeading: Float): ArViewportState {
        // Calculate the difference. E.g. if Qibla is at 90 and we point at 80, relative is +10 (turn right).
        var diff = (qiblaBearing - deviceHeading).toFloat()

        // For simplicity, we assume an elevation angle of 0.0 for now as it would require pitch/roll sensors
        // and full 3D positioning that isn't provided by the platform sensors abstraction.
        val elevationAngle = 0.0f

        // Normalize to -180 .. +180
        while (diff > 180f) diff -= 360f
        while (diff < -180f) diff += 360f

        val isAligned = abs(diff) <= ALIGNMENT_TOLERANCE_DEGREES
        val isVisible = abs(diff) <= (CAMERA_FOV_DEGREES / 2f)

        // Calculate offset ratio based on FOV. If diff is 30 (edge of 60 FOV), ratio is 1.0.
        // If it's outside FOV, clamp it to -1.0 or 1.0 for UI edge markers.
        val maxAngle = CAMERA_FOV_DEGREES / 2f
        val ratio = diff / maxAngle
        val clampedRatio = ratio.coerceIn(-1.0f, 1.0f)

        return ArViewportState(
            relativeBearing = diff,
            elevationAngle = elevationAngle,
            isVisible = isVisible,
            isAligned = isAligned,
            horizontalOffsetRatio = clampedRatio,
        )
    }
}
