package com.sanctum.core.feature.prayer.domain

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
data class SilentWindow(
    val startMillis: Long,
    val endMillis: Long,
) {
    init {
        require(startMillis <= endMillis) { "startMillis must be <= endMillis" }
    }

    companion object {
        fun calculate(
            prayerStartMillis: Long,
            prayerEndMillis: Long,
            gracePeriodBeforeMs: Long,
            gracePeriodAfterMs: Long,
        ): SilentWindow {
            val safeBefore: Long = max(0L, gracePeriodBeforeMs)
            val safeAfter: Long = max(0L, gracePeriodAfterMs)
            val start: Long = prayerStartMillis - safeBefore
            val end: Long = max(prayerStartMillis, prayerEndMillis) + safeAfter

            return SilentWindow(
                startMillis = start,
                endMillis = end,
            )
        }
    }
}
