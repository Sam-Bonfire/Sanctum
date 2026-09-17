package com.sanctum.core.core.design

import kotlinx.serialization.Serializable

@Serializable
data class AppIcon(
    val id: String,
    val name: String,
    val assetKey: String,
    val premiumOnly: Boolean = false,
)

val DEFAULT_APP_ICONS = listOf(
    AppIcon("classic", "Classic", "icon_classic"),
    AppIcon("midnight", "Midnight Gold", "icon_midnight", premiumOnly = true),
    AppIcon("dawn", "Dawn Rose", "icon_dawn", premiumOnly = true),
)

fun availableIcons(isPremium: Boolean): List<AppIcon> =
    if (isPremium) DEFAULT_APP_ICONS else DEFAULT_APP_ICONS.filter { !it.premiumOnly }

fun findIcon(id: String, isPremium: Boolean): AppIcon? =
    availableIcons(isPremium).firstOrNull { it.id == id }
