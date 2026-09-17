package com.sanctum.core.feature.habits.domain

import kotlinx.serialization.Serializable

@Serializable
data class ActivitySample(
    val hourOfDay: Int,
    val count: Int,
)

fun timeOfDayBuckets(samples: List<ActivitySample>): IntArray {
    val buckets = IntArray(4)
    for (sample in samples) {
        val bucket = when (sample.hourOfDay) {
            in 5..10 -> 0
            in 11..16 -> 1
            in 17..21 -> 2
            else -> 3
        }
        buckets[bucket] += sample.count
    }
    return buckets
}

fun peakBucket(buckets: IntArray): Int {
    require(buckets.isNotEmpty()) { "Buckets must not be empty" }
    return buckets.indices.maxBy { buckets[it] }
}
