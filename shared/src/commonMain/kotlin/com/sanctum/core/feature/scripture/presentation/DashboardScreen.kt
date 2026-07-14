package com.sanctum.core.feature.scripture.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.ErrorRetryView
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import dev.chrisbanes.haze.haze

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onRetry: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    val hrs = uiState.hoursRemaining.toIntOrNull() ?: 0
    val mins = uiState.minutesRemaining.toIntOrNull() ?: 0
    val secs = uiState.secondsRemaining.toIntOrNull() ?: 0
    val totalRemainingSeconds = hrs * 3600 + mins * 60 + secs
    val maxSeconds = 6 * 3600
    val progress = 1f - (totalRemainingSeconds.toFloat() / maxSeconds).coerceIn(0f, 1f)

    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress",
    )

    val hazeState = com.sanctum.core.core.navigation.LocalHazeState.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Background layer — source for haze blur effect on child cards
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SanctumTheme.colors.background)
                .haze(state = hazeState),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SanctumTheme.colors.brand.copy(alpha = 0.08f),
                                Color.Transparent,
                            ),
                            radius = 900f,
                        ),
                    ),
            )
        }

        // Loading state
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = SanctumTheme.colors.brand,
                modifier = Modifier.align(Alignment.Center),
            )
            return@Box
        }

        // Location permission denied — prompt user to enter manually
        if (uiState.locationError) {
            ErrorRetryView(
                message = "Location unavailable. Enable location permission or set it during onboarding.",
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center),
            )
            return@Box
        }

        // Prayer schedule calculation failed
        if (uiState.scheduleError) {
            ErrorRetryView(
                message = "Could not load prayer schedule. Check your connection and try again.",
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center),
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = SanctumTheme.spacing.xxl,
                    bottom = SanctumTheme.spacing.bottomNavPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val config = LocalWhiteLabelConfig.current

            // ─── Header: Logo + App Name + Theme Toggle ──────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SanctumTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Invisible box for balance (left)
                Box(modifier = Modifier.size(48.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    config.headerIcon()
                    Spacer(modifier = Modifier.width(SanctumTheme.spacing.sm))
                    Text(
                        // Use only the short name before the colon to prevent overflow
                        text = config.appName.substringBefore(":").trim(),
                        style = SanctumTheme.typography.headlineLarge,
                        color = SanctumTheme.colors.textPrimary,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center,
                    )
                }

                // Invisible box for balance (right)
                Box(modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${uiState.location}  |  ${uiState.dateString}",
                style = SanctumTheme.typography.bodyMedium,
                color = SanctumTheme.colors.textSecondary,
                letterSpacing = 1.sp,
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ─── UPCOMING PRAYER label ───────────────────────────
            Text(
                text = "UPCOMING PRAYER",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Circular Progress Ring + Prayer Name + Timer ────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp),
            ) {
                // Circular progress arc
                val brandColor = SanctumTheme.colors.brand
                val trackColor = SanctumTheme.colors.textPrimary.copy(alpha = 0.06f)

                Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    val strokeWidth = 4.dp.toPx()
                    val arcSize = Size(size.width, size.height)

                    // Track (background ring)
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = arcSize,
                    )

                    // Progress arc
                    drawArc(
                        color = brandColor,
                        startAngle = -90f,
                        sweepAngle = animatedProgress.value * 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = arcSize,
                    )

                    // Small dot at the progress tip
                    val angle = (-90.0 + animatedProgress.value * 360.0) * kotlin.math.PI / 180.0
                    val dotRadius = 6.dp.toPx()
                    val cx = center.x + (size.width / 2f) * kotlin.math.cos(angle).toFloat()
                    val cy = center.y + (size.height / 2f) * kotlin.math.sin(angle).toFloat()
                    drawCircle(
                        color = brandColor,
                        radius = dotRadius,
                        center = Offset(cx, cy),
                    )
                }

                // Inner content
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Prayer name
                    Text(
                        text = "${uiState.upcomingPrayerName} in",
                        style = SanctumTheme.typography.displayMedium,
                        color = SanctumTheme.colors.textPrimary,
                        fontWeight = FontWeight.Normal,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Time display: 2 h  15 m
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = uiState.hoursRemaining,
                            fontSize = 48.sp,
                            color = SanctumTheme.colors.textPrimary,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 1.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "h",
                            fontSize = 20.sp,
                            color = SanctumTheme.colors.textSecondary,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = uiState.minutesRemaining,
                            fontSize = 48.sp,
                            color = SanctumTheme.colors.textPrimary,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 1.sp,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "m",
                            fontSize = 20.sp,
                            color = SanctumTheme.colors.textSecondary,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Motivational message — only shown when a prayer name is resolved
            if (uiState.upcomingPrayerName.isNotEmpty()) {
                Text(
                    text = "Take a moment to prepare your heart and\nmind for the ${uiState.upcomingPrayerName.lowercase()} devotion.",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 48.dp),
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ─── Daily Schedule ──────────────────────────────────
            DailyScheduleRow(prayers = uiState.prayers, hazeState = hazeState)

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.xxl))

            // ─── Verse of the Day ────────────────────────────────
            Box(modifier = Modifier.padding(horizontal = SanctumTheme.spacing.lg)) {
                VerseOfTheDayCard(
                    originalText = "\u0671\u0644\u0644\u064e\u0651\u0647\u064f \u0644\u064e\u0622 \u0625\u0650\u0644\u064e\u0640\u0670\u0647\u064e \u0625\u0650\u0644\u064e\u0651\u0627 \u0647\u064f\u0648\u064e \u0671\u0644\u0652\u062d\u064e\u0649\u064f\u0651 \u0671\u0644\u0652\u0642\u064e\u064a\u064f\u0651\u0648\u0645\u064f \u06da",
                    translation = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of existence.",
                    reference = "Al-Baqarah 2:255",
                    hazeState = hazeState,
                )
            }
        }
    }
}
