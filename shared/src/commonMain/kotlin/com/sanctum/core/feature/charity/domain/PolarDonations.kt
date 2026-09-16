package com.sanctum.core.feature.charity.domain

data class PolarDonationConfig(
    val checkoutBaseUrl: String,
    val organizationSlug: String = "",
)

fun buildPolarCheckoutUrl(
    config: PolarDonationConfig,
    amount: Double? = null,
    category: CharityCategory? = null,
    reference: String? = null,
): String {
    val base = config.checkoutBaseUrl.trim().trimEnd('/')
    if (base.isEmpty()) return ""
    val params = buildList {
        if (amount != null && amount > 0) add("amount=${(amount * 100).toLong()}")
        if (category != null) add("category=${category.name.lowercase()}")
        if (!reference.isNullOrBlank()) add("reference=${reference.trim()}")
    }
    // ponytail: hosted checkout links only, webhook verification lives server-side if needed
    return if (params.isEmpty()) base else "$base?${params.joinToString("&")}"
}
