package com.sanctum.core.core.analytics

import kotlinx.serialization.Serializable

@Serializable
class FunnelEvent(
    val funnelId: String,
    var currentStep: Step? = null,
) {
    enum class Step {
        VIEWED,
        STARTED,
        ABANDONED,
        COMPLETED,
    }

    fun transitionTo(step: Step): AnalyticsEvent {
        val isValid: Boolean = when (currentStep) {
            null -> step == Step.VIEWED
            Step.VIEWED -> step == Step.STARTED || step == Step.ABANDONED
            Step.STARTED -> step == Step.COMPLETED || step == Step.ABANDONED
            Step.ABANDONED, Step.COMPLETED -> false
        }

        require(isValid) { "Invalid funnel transition from $currentStep to $step" }

        currentStep = step

        val isDrop: Boolean = step == Step.ABANDONED
        val params: Map<String, String> = mapOf(
            "funnel_id" to funnelId,
            "step" to step.name,
            "is_drop" to isDrop.toString(),
        )

        return AnalyticsEvent(
            name = "funnel_event",
            params = params,
        )
    }
}
