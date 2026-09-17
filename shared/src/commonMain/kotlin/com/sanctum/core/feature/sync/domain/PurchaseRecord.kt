package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRecord(
    val productId: String,
    val purchaseToken: String,
    val acknowledged: Boolean = false,
)

fun PurchaseRecord.needsAcknowledgement(): Boolean =
    purchaseToken.isNotBlank() && !acknowledged

fun PurchaseRecord.acknowledge(): PurchaseRecord {
    require(purchaseToken.isNotBlank()) { "Missing purchase token" }
    return copy(acknowledged = true)
}

fun isPremiumProduct(productId: String, premiumIds: Set<String>): Boolean = productId in premiumIds
