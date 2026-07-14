package com.sanctum.core.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

@Composable
fun SanctumCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = SanctumTheme.colors.surface,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(width = 0.5.dp, color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.5f), shape = shape)
            .background(backgroundColor)
            .padding(contentPadding),
        content = content,
    )
}

/**
 * Premium frosted-glass card.
 * Uses hazeChild on the background layer to blur the elements behind the card,
 * without blurring the content inside the card.
 */
@Composable
fun SanctumEditorialCard(
    hazeState: HazeState? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(28.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            SanctumTheme.colors.textPrimary.copy(alpha = 0.12f),
            SanctumTheme.colors.brand.copy(alpha = 0.08f),
            SanctumTheme.colors.textPrimary.copy(alpha = 0.04f),
        ),
    )

    val tintColor = if (SanctumTheme.colors.isLight) {
        Color.White.copy(alpha = 0.55f)
    } else {
        Color.White.copy(alpha = 0.06f)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .border(width = 0.5.dp, brush = borderBrush, shape = shape),
    ) {
        // Blur background layer
        if (hazeState != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .hazeChild(state = hazeState, shape = shape)
                    .background(tintColor),
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(tintColor),
            )
        }

        // Content layer
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}

/**
 * Premium typography section header to encapsulate standard headers across pages.
 */
@Composable
fun SanctumSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        style = SanctumTheme.typography.labelMedium,
        color = SanctumTheme.colors.textSecondary,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp,
        modifier = modifier.padding(horizontal = 8.dp),
    )
}
