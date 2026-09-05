package com.sanctum.core.feature.compass.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.compass.domain.ArQiblaCalculator
import kotlin.math.roundToInt

@Composable
fun ArQiblaView(
    qiblaBearing: Double,
    deviceHeading: Float,
    modifier: Modifier = Modifier,
) {
    val config = LocalWhiteLabelConfig.current
    val haptic = LocalHapticFeedback.current
    val viewportState = ArQiblaCalculator.calculateViewportState(qiblaBearing, deviceHeading)

    LaunchedEffect(viewportState.isAligned) {
        if (viewportState.isAligned) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val pulsingAlpha by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Camera Layer
        CameraPreviewView(modifier = Modifier.fillMaxSize())

        // Dark Overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
        )

        // Overlay Content
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val width = maxWidth
            val height = maxHeight

            // Title
            Text(
                text = "AR ${config.compassTitle}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp),
            )

            // Reticle
            val brandColor = SanctumTheme.colors.brand
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val reticleColor = if (viewportState.isAligned) brandColor else Color.White.copy(alpha = 0.5f)

                drawLine(
                    color = reticleColor,
                    start = Offset(center.x - 40.dp.toPx(), center.y),
                    end = Offset(center.x + 40.dp.toPx(), center.y),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                drawLine(
                    color = reticleColor,
                    start = Offset(center.x, center.y - 40.dp.toPx()),
                    end = Offset(center.x, center.y + 40.dp.toPx()),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                drawCircle(
                    color = reticleColor,
                    radius = 20.dp.toPx(),
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
                )
            }

            if (viewportState.isVisible) {
                // Draw target marker
                val markerX = (width.value / 2) + (viewportState.horizontalOffsetRatio * (width.value / 2))

                Box(
                    modifier = Modifier
                        .offset(x = markerX.dp - 24.dp, y = height / 2 - 24.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SanctumTheme.colors.brand)
                        .alpha(if (viewportState.isAligned) pulsingAlpha else 0.8f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "🕋",
                        fontSize = 24.sp,
                    )
                }
            } else {
                // Guidance arrows
                val isRight = viewportState.relativeBearing > 0
                val icon = if (isRight) Icons.Default.ArrowForward else Icons.Default.ArrowBack

                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = if (isRight) 100.dp else (-100).dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!isRight) {
                        Icon(icon, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = "Turn ${kotlin.math.abs(viewportState.relativeBearing).roundToInt()}°",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    if (isRight) {
                        Spacer(Modifier.width(8.dp))
                        Icon(icon, contentDescription = null, tint = Color.White)
                    }
                }
            }

            // Stats overlay at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (viewportState.isAligned) {
                    Text(
                        text = "ALIGNED",
                        color = SanctumTheme.colors.brand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                } else {
                    Text(
                        text = "Bearing: ${qiblaBearing.roundToInt()}° | Heading: ${deviceHeading.roundToInt()}°",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
