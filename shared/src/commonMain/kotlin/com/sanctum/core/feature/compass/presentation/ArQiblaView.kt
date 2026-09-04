package com.sanctum.core.feature.compass.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.compass.domain.ArQiblaCalculator
import kotlin.math.roundToInt

@Composable
fun ArQiblaView(
    deviceHeading: Float,
    qiblaBearing: Float,
    distanceKm: Double?,
    modifier: Modifier = Modifier,
) {
    val arResult = ArQiblaCalculator.calculate(deviceHeading, qiblaBearing)
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(arResult.isAligned) {
        if (arResult.isAligned) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
        val brandColor = SanctumTheme.colors.brand
        val secondaryColor = SanctumTheme.colors.textSecondary.copy(alpha = 0.5f)
        val textPrimaryColor = SanctumTheme.colors.textPrimary
        // Camera Layer
        CameraPreviewView(modifier = Modifier.fillMaxSize())

        // UI Overlay Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Target Reticle (Center of screen)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center
                val radius = 80.dp.toPx()
                val color = if (arResult.isAligned) brandColor else secondaryColor
                val strokeWidth = if (arResult.isAligned) 4.dp.toPx() else 2.dp.toPx()

                drawCircle(
                    color = color,
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokeWidth),
                )

                // Crosshairs
                drawLine(
                    color = color,
                    start = center.copy(x = center.x - radius - 10.dp.toPx()),
                    end = center.copy(x = center.x - radius + 10.dp.toPx()),
                    strokeWidth = strokeWidth,
                )
                drawLine(
                    color = color,
                    start = center.copy(x = center.x + radius - 10.dp.toPx()),
                    end = center.copy(x = center.x + radius + 10.dp.toPx()),
                    strokeWidth = strokeWidth,
                )
                drawLine(
                    color = color,
                    start = center.copy(y = center.y - radius - 10.dp.toPx()),
                    end = center.copy(y = center.y - radius + 10.dp.toPx()),
                    strokeWidth = strokeWidth,
                )
                drawLine(
                    color = color,
                    start = center.copy(y = center.y + radius - 10.dp.toPx()),
                    end = center.copy(y = center.y + radius + 10.dp.toPx()),
                    strokeWidth = strokeWidth,
                )
            }

            // Directional Guidance Text and Arrows
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Text(
                    text = arResult.instructionText,
                    color = if (arResult.isAligned) brandColor else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = SanctumTheme.typography.bodyMedium.fontFamily,
                )
            }

            // Left/Right Indicators (if off-screen)
            if (!arResult.isVisible) {
                if (arResult.horizontalOffset == 0f) { // Hack to determine side, or we can use diff.
                    // Recalculate diff to know which side
                    var diff = qiblaBearing - deviceHeading
                    while (diff <= -180f) diff += 360f
                    while (diff > 180f) diff -= 360f

                    if (diff > 0) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Turn Right",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(64.dp),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Turn Left",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(64.dp),
                        )
                    }
                }
            }

            // Floating Marker (Kaaba Pin)
            if (arResult.isVisible) {
                // Calculate horizontal position based on offset (-1.0 to 1.0)
                // Need screen width. Box gives us constraints indirectly, but we can animate offset based on screen percentage

                // Let's use a trick: place it in the center, and offset it
                val animatedOffset by animateFloatAsState(
                    targetValue = arResult.horizontalOffset,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    val density = LocalDensity.current
                    // We'll estimate width. A better approach is using BoxWithConstraints or layout modifiers,
                    // but for simplicity in Compose we can use graphicsLayer or offset relative to size.
                    // For now, assume a max horizontal offset of 150dp from center.
                    val maxOffsetDp = 150.dp

                    Column(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = (animatedOffset * with(density) { maxOffsetDp.toPx() }).roundToInt(),
                                    y = 0,
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Kaaba Icon (Simplified as a beautiful cube/pin)
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (arResult.isAligned) brandColor else Color.White),
                            contentAlignment = Alignment.Center,
                        ) {
                            Canvas(modifier = Modifier.size(24.dp)) {
                                val path = Path().apply {
                                    moveTo(size.width / 2, 0f)
                                    lineTo(size.width, size.height / 3)
                                    lineTo(size.width / 2, size.height * 2 / 3)
                                    lineTo(0f, size.height / 3)
                                    close()

                                    moveTo(0f, size.height / 3)
                                    lineTo(0f, size.height * 2 / 3)
                                    lineTo(size.width / 2, size.height)
                                    lineTo(size.width / 2, size.height * 2 / 3)
                                    close()

                                    moveTo(size.width, size.height / 3)
                                    lineTo(size.width, size.height * 2 / 3)
                                    lineTo(size.width / 2, size.height)
                                    lineTo(size.width / 2, size.height * 2 / 3)
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    color = if (arResult.isAligned) Color.White else textPrimaryColor,
                                )
                            }
                        }

                        if (distanceKm != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${distanceKm.roundToInt()} km",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
