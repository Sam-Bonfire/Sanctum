package com.sanctum.core.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun SanctumPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SanctumTheme.colors.brand,
            contentColor = SanctumTheme.colors.surface,
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun SanctumOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        border = BorderStroke(1.dp, SanctumTheme.colors.brand),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = SanctumTheme.colors.brand,
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun SanctumBadge(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val backgroundColor = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.neutral
    val textColor = if (isSelected) SanctumTheme.colors.surface else SanctumTheme.colors.brand

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = SanctumTheme.typography.labelMedium,
            color = textColor,
        )
    }
}

@Composable
fun SanctumIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    backgroundColor: Color = SanctumTheme.colors.brand,
    iconColor: Color = SanctumTheme.colors.surface,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(24.dp),
        )
    }
}
