package com.sanctum.core.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LightBrand = Color(0xFF6B8E7B)
val LightBrandVariant = Color(0xFF8AA698)
val LightTertiary = Color(0xFF3F353B)
val LightNeutral = Color(0xFFFAFAF6)
val LightBackground = Color(0xFFFAF9F6)
val LightSurface = Color(0xFFFFFFFF)
val LightGlassBackground = Color(0x1A000000)
val LightTextPrimary = Color(0xFF3F3E41)
val LightTextSecondary = Color(0xFF7A797D)
val LightError = Color(0xFFB00020)

val DarkBrand = Color(0xFF8AA698)
val DarkBrandVariant = Color(0xFF6B8E7B)
val DarkTertiary = Color(0xFFFAFAF6) // Inverted for dark mode
val DarkNeutral = Color(0xFF2C2C2C)
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkGlassBackground = Color(0x33FFFFFF)
val DarkTextPrimary = Color(0xFFE0E0E0)
val DarkTextSecondary = Color(0xFFA0A0A0)
val DarkError = Color(0xFFCF6679)

fun lightSanctumColors() = SanctumColors(
    brand = LightBrand,
    brandVariant = LightBrandVariant,
    tertiary = LightTertiary,
    neutral = LightNeutral,
    background = LightBackground,
    surface = LightSurface,
    glassBackground = LightGlassBackground,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    error = LightError,
    isLight = true,
    outline = Color(0xFF727973),
    outlineVariant = Color(0xFFC1C8C2),
)

fun darkSanctumColors() = SanctumColors(
    brand = DarkBrand,
    brandVariant = DarkBrandVariant,
    tertiary = DarkTertiary,
    neutral = DarkNeutral,
    background = DarkBackground,
    surface = DarkSurface,
    glassBackground = DarkGlassBackground,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    error = DarkError,
    isLight = false,
    outline = Color(0xFF727973),
    outlineVariant = Color(0xFFC1C8C2),
)

val LocalThemeToggle = compositionLocalOf { {} }
val LocalIsDarkTheme = compositionLocalOf { false }

object SanctumTheme {
    val colors: SanctumColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSanctumColors.current

    val typography: SanctumTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSanctumTypography.current

    val spacing: SanctumSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSanctumSpacing.current
}

@Composable
fun SanctumTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    brandColor: Color? = null,
    brandVariantColor: Color? = null,
    content: @Composable () -> Unit,
) {
    val baseColors = if (isDarkTheme) darkSanctumColors() else lightSanctumColors()
    val colors = if (brandColor != null || brandVariantColor != null) {
        baseColors.copy(
            brand = brandColor ?: baseColors.brand,
            brandVariant = brandVariantColor ?: baseColors.brandVariant,
        )
    } else {
        baseColors
    }
    val typography = getSanctumTypography()
    val spacing = SanctumSpacing()

    CompositionLocalProvider(
        LocalSanctumColors provides colors,
        LocalSanctumTypography provides typography,
        LocalSanctumSpacing provides spacing,
        content = content,
    )
}
