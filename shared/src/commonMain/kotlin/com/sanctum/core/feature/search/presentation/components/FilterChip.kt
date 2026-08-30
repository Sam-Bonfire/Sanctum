package com.sanctum.core.feature.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) SanctumTheme.colors.brand else Color.Transparent
    val contentColor = if (isSelected) SanctumTheme.colors.background else SanctumTheme.colors.textPrimary
    val borderColor = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.outlineVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = SanctumTheme.typography.bodyMedium,
            color = contentColor,
        )
    }
}
