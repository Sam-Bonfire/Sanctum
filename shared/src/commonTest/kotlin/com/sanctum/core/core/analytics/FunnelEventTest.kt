package com.sanctum.core.core.analytics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FunnelEventTest {

    @Test
    fun testHappyPath() {
        val funnel: FunnelEvent = FunnelEvent(funnelId = "purchase_123")

        val event1: AnalyticsEvent = funnel.transitionTo(FunnelEvent.Step.VIEWED)
        assertEquals("funnel_event", event1.name)
        assertEquals("purchase_123", event1.params["funnel_id"])
        assertEquals("VIEWED", event1.params["step"])
        assertEquals("false", event1.params["is_drop"])

        val event2: AnalyticsEvent = funnel.transitionTo(FunnelEvent.Step.STARTED)
        assertEquals("STARTED", event2.params["step"])
        assertEquals("false", event2.params["is_drop"])

        val event3: AnalyticsEvent = funnel.transitionTo(FunnelEvent.Step.COMPLETED)
        assertEquals("COMPLETED", event3.params["step"])
        assertEquals("false", event3.params["is_drop"])
    }

    @Test
    fun testAbandonment() {
        val funnel: FunnelEvent = FunnelEvent(funnelId = "purchase_124")
        funnel.transitionTo(FunnelEvent.Step.VIEWED)
        val event: AnalyticsEvent = funnel.transitionTo(FunnelEvent.Step.ABANDONED)

        assertEquals("ABANDONED", event.params["step"])
        assertEquals("true", event.params["is_drop"])
    }

    @Test
    fun testInvalidStart() {
        val funnel: FunnelEvent = FunnelEvent(funnelId = "purchase_125")

        assertFailsWith<IllegalArgumentException> {
            funnel.transitionTo(FunnelEvent.Step.STARTED)
        }

        assertFailsWith<IllegalArgumentException> {
            funnel.transitionTo(FunnelEvent.Step.COMPLETED)
        }
    }

    @Test
    fun testInvalidTransitions() {
        val funnel: FunnelEvent = FunnelEvent(funnelId = "purchase_126")
        funnel.transitionTo(FunnelEvent.Step.VIEWED)

        assertFailsWith<IllegalArgumentException> {
            funnel.transitionTo(FunnelEvent.Step.COMPLETED)
        }
    }

    @Test
    fun testTerminalStates() {
        val funnel1: FunnelEvent = FunnelEvent(funnelId = "purchase_127")
        funnel1.transitionTo(FunnelEvent.Step.VIEWED)
        funnel1.transitionTo(FunnelEvent.Step.STARTED)
        funnel1.transitionTo(FunnelEvent.Step.COMPLETED)

        assertFailsWith<IllegalArgumentException> {
            funnel1.transitionTo(FunnelEvent.Step.ABANDONED)
        }

        val funnel2: FunnelEvent = FunnelEvent(funnelId = "purchase_128")
        funnel2.transitionTo(FunnelEvent.Step.VIEWED)
        funnel2.transitionTo(FunnelEvent.Step.ABANDONED)

        assertFailsWith<IllegalArgumentException> {
            funnel2.transitionTo(FunnelEvent.Step.STARTED)
        }
    }
}
