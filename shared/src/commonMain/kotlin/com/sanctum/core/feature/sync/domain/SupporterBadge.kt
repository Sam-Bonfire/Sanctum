package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class SupporterBadge(
    val tier: String,
    val label: String,
    val grantedAtMs: Long = 0,
)

val SUPPORTER_TIERS = listOf("bronze", "silver", "gold")

fun badgeForTier(tier: String): SupporterBadge? {
    if (tier !in SUPPORTER_TIERS) return null
    return SupporterBadge(tier, tier.replaceFirstChar { it.uppercase() } + " Supporter")
}

fun SupporterBadge.outranks(other: SupporterBadge): Boolean =
    SUPPORTER_TIERS.indexOf(tier) > SUPPORTER_TIERS.indexOf(other.tier)
