package com.sanctum.core.core.design

import kotlinx.serialization.Serializable

@Serializable
class A11yLabels(
    val registry: Map<String, List<String>> = emptyMap<String, List<String>>(),
) {
    fun getLabels(screenId: String, fallback: List<String> = emptyList<String>()): List<String> {
        val labels: List<String>? = registry[screenId]
        return if (labels != null) {
            labels
        } else {
            fallback
        }
    }

    fun isComplete(expectedScreenIds: Set<String>): Boolean {
        return expectedScreenIds.all { screenId: String -> registry.containsKey(screenId) }
    }
}
