package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable

@Serializable
data class DrivingMode(
    val enabled: Boolean = false,
    val largeControls: Boolean = true,
    val autoplayNext: Boolean = true,
)

fun DrivingMode.toggle(): DrivingMode = copy(enabled = !enabled)

fun DrivingMode.controlScale(): Float = if (enabled && largeControls) 1.5f else 1f
