package com.sanctum.core.feature.habits.domain

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivityGraphTest {

    @Test
    fun `buckets samples by time of day`() {
        val buckets = timeOfDayBuckets(
            listOf(
                ActivitySample(6, 2),
                ActivitySample(12, 3),
                ActivitySample(19, 1),
                ActivitySample(23, 4),
            ),
        )
        assertContentEquals(intArrayOf(2, 3, 1, 4), buckets)
    }

    @Test
    fun `finds peak and rejects empty`() {
        assertEquals(3, peakBucket(intArrayOf(2, 3, 1, 4)))
        assertFailsWith<IllegalArgumentException> { peakBucket(intArrayOf()) }
    }
}
