package com.sanctum.core.feature.compass.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
actual fun CameraPreviewView(modifier: Modifier) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "AR View not supported on Web",
            color = Color.White,
            style = SanctumTheme.typography.bodyMedium,
        )
    }
}
