package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable
import kotlin.math.atan
import kotlin.math.tan

@Serializable
enum class MadhabAsrMethod {
    SHAFII,
    HANAFI,
}

fun MadhabAsrMethod.shadowFactor(): Double = if (this == MadhabAsrMethod.SHAFII) 1.0 else 2.0

fun asrAltitudeDegrees(noonShadowLength: Double, method: MadhabAsrMethod): Double {
    require(noonShadowLength >= 0) { "Shadow length must not be negative" }
    val shadow = noonShadowLength + method.shadowFactor()
    return Math.toDegrees(atan(1.0 / shadow))
}

fun MadhabAsrMethod.asrShadowLength(objectHeight: Double, sunAltitudeDegrees: Double): Double {
    require(objectHeight > 0) { "Object height must be positive" }
    return objectHeight / tan(Math.toRadians(sunAltitudeDegrees)) - shadowFactor() * objectHeight
}
