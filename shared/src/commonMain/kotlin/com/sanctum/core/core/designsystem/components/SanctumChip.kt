package com.sanctum.core.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun SanctumChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SanctumTheme.colors.surface,
    borderColor: Color = SanctumTheme.colors.brand,
    textColor: Color = SanctumTheme.colors.brand,
    selected: Boolean = false,
) {
    val currentBgColor = if (selected) borderColor else backgroundColor
    val currentTextColor = if (selected) SanctumTheme.colors.surface else textColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(currentBgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = currentTextColor,
            fontSize = 12.sp,
            style = SanctumTheme.typography.labelMedium,
        )
    }
}
