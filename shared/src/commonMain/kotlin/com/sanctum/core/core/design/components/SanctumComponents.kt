package com.sanctum.core.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

/**
 * A reusable premium card component that enforces the Sanctum Design System:
 * - Floating effect with subtle border
 * - Light surface color
 * - Organic rounded corners
 */
@Composable
fun SanctumCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(SanctumTheme.spacing.xl),
    backgroundColor: Color = SanctumTheme.colors.surface,
    borderColor: Color = SanctumTheme.colors.textSecondary.copy(alpha = 0.05f),
    borderWidth: Dp = 1.dp,
    contentPadding: Dp = SanctumTheme.spacing.xl,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(width = borderWidth, color = borderColor, shape = shape)
            .padding(contentPadding),
    ) {
        content()
    }
}

/**
 * A reusable pill/tag component for active states, warnings, or small informational bubbles.
 */
@Composable
fun SanctumTag(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SanctumTheme.colors.textPrimary.copy(alpha = 0.1f),
    contentPaddingVertical: Dp = 8.dp,
    contentPaddingHorizontal: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = shape)
            .padding(horizontal = contentPaddingHorizontal, vertical = contentPaddingVertical),
    ) {
        content()
    }
}
