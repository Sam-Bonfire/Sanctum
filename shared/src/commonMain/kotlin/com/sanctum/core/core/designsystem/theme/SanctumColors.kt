package com.sanctum.core.core.designsystem.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class SanctumColors(
    brand: Color,
    brandVariant: Color,
    tertiary: Color,
    neutral: Color,
    background: Color,
    surface: Color,
    glassBackground: Color,
    textPrimary: Color,
    textSecondary: Color,
    error: Color,
    isLight: Boolean,
    outline: Color = Color(0xFF727973),
    outlineVariant: Color = Color(0xFFC1C8C2),
) {
    var brand by mutableStateOf(brand)
        private set
    var brandVariant by mutableStateOf(brandVariant)
        private set
    var tertiary by mutableStateOf(tertiary)
        private set
    var neutral by mutableStateOf(neutral)
        private set
    var background by mutableStateOf(background)
        private set
    var surface by mutableStateOf(surface)
        private set
    var glassBackground by mutableStateOf(glassBackground)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var error by mutableStateOf(error)
        private set
    var isLight by mutableStateOf(isLight)
        private set
    var outline by mutableStateOf(outline)
        private set
    var outlineVariant by mutableStateOf(outlineVariant)
        private set

    fun copy(
        brand: Color = this.brand,
        brandVariant: Color = this.brandVariant,
        tertiary: Color = this.tertiary,
        neutral: Color = this.neutral,
        background: Color = this.background,
        surface: Color = this.surface,
        glassBackground: Color = this.glassBackground,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        error: Color = this.error,
        isLight: Boolean = this.isLight,
        outline: Color = this.outline,
        outlineVariant: Color = this.outlineVariant,
    ) = SanctumColors(
        brand = brand,
        brandVariant = brandVariant,
        tertiary = tertiary,
        neutral = neutral,
        background = background,
        surface = surface,
        glassBackground = glassBackground,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        error = error,
        isLight = isLight,
        outline = outline,
        outlineVariant = outlineVariant,
    )
}

val LocalSanctumColors = staticCompositionLocalOf<SanctumColors> {
    error("No SanctumColors provided")
}
