package com.sanctum.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.russhwolf.settings.Settings
import com.sanctum.app.navigation.DashboardScreenNode
import com.sanctum.app.navigation.DuasCatalogScreenNode
import com.sanctum.app.navigation.JournalScreenNode
import com.sanctum.app.navigation.OnboardingScreenNode
import com.sanctum.app.navigation.QiblaCompassScreenNode
import com.sanctum.app.navigation.ScriptureIndexScreenNode
import com.sanctum.app.navigation.SettingsScreenNode
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.design.NavItemConfig
import com.sanctum.core.core.design.WhiteLabelConfig
import com.sanctum.core.core.designsystem.theme.LocalIsDarkTheme
import com.sanctum.core.core.designsystem.theme.LocalThemeToggle
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.core.navigation.MainLayout
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.KoinContext

object ThemeController {
    val themePref = MutableStateFlow("system")
}

fun String.toColor(): Color {
    val hex = this.removePrefix("#")
    val alpha = if (hex.length == 8) hex.substring(0, 2) else "FF"
    val rgb = if (hex.length == 8) hex.substring(2) else hex
    return Color(("$alpha$rgb").toLong(16))
}

@Composable
fun App() {
    KoinContext {
        val settings = org.koin.compose.koinInject<Settings>()

        // Initialize ThemeController once from Settings
        LaunchedEffect(Unit) {
            ThemeController.themePref.value = settings.getString("theme_pref", "system")
        }

        val themePrefState by ThemeController.themePref.collectAsState()
        val systemTheme = isSystemInDarkTheme()

        var isDarkTheme by remember(themePrefState, systemTheme) {
            mutableStateOf(
                when (themePrefState) {
                    "dark" -> true
                    "light" -> false
                    else -> systemTheme
                },
            )
        }

        SanctumTheme(
            isDarkTheme = isDarkTheme,
            brandColor = BuildConfig.COLOR_PRIMARY.toColor(),
            brandVariantColor = BuildConfig.COLOR_PRIMARY_VARIANT.toColor(),
        ) {
            CompositionLocalProvider(
                LocalThemeToggle provides { isDarkTheme = !isDarkTheme },
                LocalIsDarkTheme provides isDarkTheme,
            ) {
                val flavorConfig = remember {
                    WhiteLabelConfig(
                        appName = BuildConfig.APP_NAME,
                        brandName = BuildConfig.BRAND_NAME,
                        brandSubtitle = BuildConfig.BRAND_SUBTITLE,
                        primaryColor = BuildConfig.COLOR_PRIMARY.toColor(),
                        secondaryColor = BuildConfig.COLOR_PRIMARY_VARIANT.toColor(),
                        compassTitle = BuildConfig.TERM_SCHEDULE_TITLE.uppercase(),
                        hasTransliteration = BuildConfig.HAS_TRANSLITERATION,
                        charityTrackerTitle = "Sadaqah Tracker",
                        hasCharityTracker = BuildConfig.APP_ID == "nur",
                        navItems = mutableListOf(
                            NavItemConfig("dashboard", "Home", Icons.Default.Home),
                        ).apply {
                            if (BuildConfig.HAS_COMPASS) {
                                add(NavItemConfig("qibla", BuildConfig.TERM_SCHEDULE_TITLE, Icons.Default.LocationOn))
                            }
                            add(NavItemConfig("reader", BuildConfig.TERM_SCRIPTURE_TITLE, Icons.AutoMirrored.Filled.List))
                            add(NavItemConfig("journal", "Journal", Icons.Default.Edit))
                            add(NavItemConfig("duas", BuildConfig.TERM_DAILY_DEVOTION, Icons.Default.Favorite))
                            if (BuildConfig.APP_ID == "nur") {
                                add(NavItemConfig("charity", "Sadaqah", Icons.Default.Favorite))
                            }
                            add(NavItemConfig("settings", "Profile", Icons.Default.Person))
                        },
                        headerIcon = {
                            Canvas(modifier = Modifier.size(28.dp)) {
                                val strokeWidth = 2.dp.toPx()
                                drawCircle(
                                    color = BuildConfig.COLOR_PRIMARY.toColor(),
                                    radius = 10.dp.toPx(),
                                    center = Offset(size.width / 2, size.height / 2),
                                    style = Stroke(width = strokeWidth),
                                )
                                drawCircle(
                                    color = BuildConfig.COLOR_PRIMARY_VARIANT.toColor(),
                                    radius = 4.dp.toPx(),
                                    center = Offset(size.width / 2, size.height / 2),
                                )
                            }
                        },
                    )
                }

                CompositionLocalProvider(LocalWhiteLabelConfig provides flavorConfig) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(SanctumTheme.colors.background),
                    ) {
                        val hasOnboarded = settings.getBoolean("has_onboarded", false)
                        val initialScreen = if (hasOnboarded) DashboardScreenNode() else OnboardingScreenNode()

                        Navigator(initialScreen) { navigator ->
                            if (navigator.lastItem is OnboardingScreenNode) {
                                cafe.adriel.voyager.navigator.CurrentScreen()
                            } else {
                                MainLayout(
                                    currentScreenId = when (navigator.lastItem) {
                                        is DashboardScreenNode -> "dashboard"
                                        is QiblaCompassScreenNode -> "qibla"
                                        is ScriptureIndexScreenNode -> "reader"
                                        is JournalScreenNode -> "journal"
                                        is DuasCatalogScreenNode -> "duas"
                                        is com.sanctum.app.navigation.CharityTrackerScreenNode -> "charity"
                                        is SettingsScreenNode -> "settings"
                                        else -> "dashboard"
                                    },
                                    onNavigate = { screenId ->
                                        val targetScreen = when (screenId) {
                                            "dashboard" -> DashboardScreenNode()
                                            "qibla" -> QiblaCompassScreenNode()
                                            "reader" -> ScriptureIndexScreenNode()
                                            "journal" -> JournalScreenNode()
                                            "duas" -> DuasCatalogScreenNode()
                                            "charity" -> com.sanctum.app.navigation.CharityTrackerScreenNode()
                                            "settings" -> SettingsScreenNode()
                                            else -> DashboardScreenNode()
                                        }
                                        navigator.replaceAll(targetScreen)
                                    },
                                ) {
                                    cafe.adriel.voyager.navigator.CurrentScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
