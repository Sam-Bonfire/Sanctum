package com.sanctum.core.core.analytics

import kotlinx.serialization.Serializable

@Serializable
data class AdConfig(
    val enabled: Boolean = false,
    val adUnitId: String = "",
)

fun shouldShowAds(isPremium: Boolean, config: AdConfig): Boolean =
    !isPremium && config.enabled && config.adUnitId.isNotBlank()
