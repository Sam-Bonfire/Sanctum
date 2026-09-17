package com.sanctum.core.feature.sync.domain

import kotlinx.serialization.Serializable

val GIFT_DURATIONS_MONTHS = setOf(1, 3, 6, 12)

@Serializable
data class GiftSubscription(
    val recipientEmail: String,
    val months: Int,
    val message: String = "",
)

fun GiftSubscription.isValid(): Boolean =
    recipientEmail.contains("@") && months in GIFT_DURATIONS_MONTHS

fun GiftSubscription.priceCents(monthlyCents: Long): Long = monthlyCents * months
