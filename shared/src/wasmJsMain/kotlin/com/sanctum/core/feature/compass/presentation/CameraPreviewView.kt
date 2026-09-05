package com.sanctum.core.feature.compass.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun CameraPreviewView(modifier: Modifier) {
    // Camera not currently supported on web
    Box(modifier = modifier.background(Color.Black))
}
