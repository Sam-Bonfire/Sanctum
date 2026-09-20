package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class SafetyReport(
    val reporterId: String,
    val zoneId: String,
    val category: String,
    val details: String,
) {
    init {
        require(reporterId.isNotBlank()) { "reporterId cannot be blank" }
        require(zoneId.isNotBlank()) { "zoneId cannot be blank" }
        require(details.isNotBlank()) { "details cannot be blank" }
        require(category in ALLOWED_CATEGORIES) { "Invalid category: $category" }
    }

    companion object {
        val ALLOWED_CATEGORIES = setOf("INTOLERANCE", "HARASSMENT", "SPAM", "OTHER")
    }
}
