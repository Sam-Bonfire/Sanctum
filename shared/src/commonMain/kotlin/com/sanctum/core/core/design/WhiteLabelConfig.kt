package com.sanctum.core.core.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines a navigation tab for the dynamic Bottom Navigation Bar.
 */
data class NavItemConfig(
    val id: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * The core design and branding configuration for a white-label app flavor.
 */
data class WhiteLabelConfig(
    val appName: String,
    val brandName: String,
    val brandSubtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val navItems: List<NavItemConfig>,
    val headerIcon: @Composable () -> Unit,
    val compassTitle: String,
    val hasTransliteration: Boolean = true,
    val dailyDevotionalTitle: String = "Daily Supplication",
    val hasDailyDevotionalNotification: Boolean = true,
    val hasZakatCalculator: Boolean = false,
    val hasTajweedRules: Boolean = false,
    val hasFastingTracker: Boolean = false,
)

/**
 * CompositionLocal to inject the WhiteLabelConfig down the Compose tree.
 */
val LocalWhiteLabelConfig = staticCompositionLocalOf<WhiteLabelConfig> {
    error("No WhiteLabelConfig provided! Make sure your App.kt wraps the UI in CompositionLocalProvider.")
}
