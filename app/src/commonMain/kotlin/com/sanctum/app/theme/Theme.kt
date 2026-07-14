package com.sanctum.app.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val SelahLightColors = lightColors(
    primary = PrimarySage,
    primaryVariant = ContainerSage,
    secondary = SecondaryOlive,
    secondaryVariant = SecondaryChampagne,
    background = BackgroundCream,
    surface = SurfaceCream,
    onPrimary = BackgroundCream,
    onSecondary = BackgroundCream,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun SelahTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = SelahLightColors,
        typography = SelahTypography,
        content = content,
    )
}
