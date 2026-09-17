package com.sanctum.core.core.design

import kotlinx.serialization.Serializable

@Serializable
data class FaithProfile(
    val flavorId: String,
    val displayName: String,
    val isActive: Boolean = false,
)

@Serializable
data class FaithProfileSet(
    val profiles: List<FaithProfile> = emptyList(),
)

fun FaithProfileSet.active(): List<FaithProfile> = profiles.filter { it.isActive }

fun FaithProfileSet.toggle(flavorId: String): FaithProfileSet =
    copy(profiles = profiles.map { if (it.flavorId == flavorId) it.copy(isActive = !it.isActive) else it })

fun FaithProfileSet.activateOnly(flavorId: String): FaithProfileSet =
    copy(profiles = profiles.map { it.copy(isActive = it.flavorId == flavorId) })
