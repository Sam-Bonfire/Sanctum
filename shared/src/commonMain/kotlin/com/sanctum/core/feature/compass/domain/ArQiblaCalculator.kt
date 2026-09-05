package com.sanctum.core.feature.compass.domain

import kotlin.math.abs

/**
 * Result of the AR Qibla calculation for the overlay UI.
 */
data class ArQiblaResult(
    val isVisible: Boolean,
    val horizontalOffset: Float,
    val instructionText: String,
    val isAligned: Boolean,
)

object ArQiblaCalculator {
    private const val CAMERA_FOV_DEGREES = 60f
    private const val ALIGNMENT_TOLERANCE_DEGREES = 2f

    /**
     * Calculates the AR metrics needed to render the Qibla marker over the camera preview.
     *
     * @param deviceHeading Current device heading from True North (0-360)
     * @param qiblaBearing Bearing to the Qibla from True North (0-360)
     * @return [ArQiblaResult] containing visibility and placement metrics
     */
    fun calculate(deviceHeading: Float, qiblaBearing: Float): ArQiblaResult {
        // Calculate the difference between where the phone is pointing and the target
        var diff = qiblaBearing - deviceHeading

        // Normalize to -180 to 180
        while (diff <= -180f) diff += 360f
        while (diff > 180f) diff -= 360f

        val isAligned = abs(diff) <= ALIGNMENT_TOLERANCE_DEGREES

        // FOV is 60 degrees, so -30 to +30 degrees from center
        val halfFov = CAMERA_FOV_DEGREES / 2f
        val isVisible = abs(diff) <= halfFov

        // -1.0 is left edge of screen, 1.0 is right edge, 0 is center
        val horizontalOffset = if (isVisible) diff / halfFov else 0f

        val instructionText = when {
            isAligned -> "Aligned"
            diff > 0 -> "Turn ${abs(diff).toInt()}° Right"
            else -> "Turn ${abs(diff).toInt()}° Left"
        }

        return ArQiblaResult(
            isVisible = isVisible,
            horizontalOffset = horizontalOffset,
            instructionText = instructionText,
            isAligned = isAligned,
        )
    }
}
