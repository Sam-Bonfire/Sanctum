package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTap(
    val duaId: String,
    val count: Int = 0,
    val lastTappedMs: Long = 0,
    val burstActive: Boolean = false,
)

fun PrayerTap.tap(nowMs: Long): PrayerTap {
    val burst = nowMs - lastTappedMs < 1_000
    return copy(count = count + 1, lastTappedMs = nowMs, burstActive = burst)
}

fun PrayerTap.settle(): PrayerTap = copy(burstActive = false)
