package com.sanctum.core.core.analytics

data class AnalyticsEvent(
    val name: String,
    val params: Map<String, String> = emptyMap(),
) {
    init {
        require(name.isNotBlank()) { "event name must not be blank" }
    }
}

/**
 * Vendor-neutral analytics sink. PostHog/Firebase adapters implement this
 * interface when provider SDKs land; until then [NoOpAnalyticsTracker] lets
 * call sites log without a vendor decision.
 *
 * Params stay [String]-valued (the portable subset); widen the type when the
 * first numeric/boolean need lands.
 */
fun interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
}

object NoOpAnalyticsTracker : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) = Unit
}
