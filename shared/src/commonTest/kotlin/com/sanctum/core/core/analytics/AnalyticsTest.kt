package com.sanctum.core.core.analytics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AnalyticsTest {

    @Test
    fun noOpAcceptsEvents() {
        val tracker: AnalyticsTracker = NoOpAnalyticsTracker
        tracker.track(AnalyticsEvent("plan_enrolled", mapOf("plan_id" to "quran_khatam_30")))
    }

    @Test
    fun recordingTrackerCapturesInOrder() {
        val seen = mutableListOf<AnalyticsEvent>()
        val tracker = AnalyticsTracker { seen.add(it) }
        tracker.track(AnalyticsEvent("a"))
        tracker.track(AnalyticsEvent("b", mapOf("k" to "v")))
        assertEquals(listOf("a", "b"), seen.map { it.name })
        assertEquals("v", seen[1].params["k"])
    }

    @Test
    fun rejectsBlankNames() {
        assertFailsWith<IllegalArgumentException> { AnalyticsEvent("") }
        assertFailsWith<IllegalArgumentException> { AnalyticsEvent("   ") }
    }
}
