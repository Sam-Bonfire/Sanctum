package com.sanctum.core.feature.scripture.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(
                    top = SanctumTheme.spacing.xxl,
                    bottom = SanctumTheme.spacing.bottomNavPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val config = LocalWhiteLabelConfig.current

            // ─── Header: Premium Centered Badge + Floating Control Pill ──────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Left spacer to perfectly center the logo circle
                Spacer(modifier = Modifier.width(64.dp))

                // Centered blob logo container
                val blobShape = object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density,
                    ): Outline {
                        val w = size.width
                        val h = size.height
                        val path = Path().apply {
                            moveTo(w * 0.5f, h * 0.02f)
                            cubicTo(w * 0.78f, h * -0.02f, w * 1.02f, h * 0.22f, w * 0.98f, h * 0.48f)
                            cubicTo(w * 1.03f, h * 0.75f, w * 0.8f, h * 1.02f, w * 0.52f, h * 0.98f)
                            cubicTo(w * 0.22f, h * 1.04f, w * -0.02f, h * 0.78f, w * 0.02f, h * 0.5f)
                            cubicTo(w * -0.03f, h * 0.2f, w * 0.2f, h * -0.02f, w * 0.5f, h * 0.02f)
                            close()
                        }
                        return Outline.Generic(path)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(blobShape)
                        .border(
                            width = 0.5.dp,
                            color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                            shape = blobShape,
                        )
                        .background(SanctumTheme.colors.surface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Stylized Leaf Canvas Logo
                        val brandColor = SanctumTheme.colors.brand
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val path = Path().apply {
                                moveTo(size.width / 2, 2f)
                                quadraticTo(size.width, size.height / 2, size.width / 2, size.height - 2f)
                                quadraticTo(0f, size.height / 2, size.width / 2, 2f)
                            }
                            drawPath(path, color = brandColor)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = config.brandName,
                            style = SanctumTheme.typography.titleLarge.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            ),
                            color = SanctumTheme.colors.textPrimary,
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = config.brandSubtitle,
                            style = SanctumTheme.typography.labelSmall,
                            color = SanctumTheme.colors.textSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontSize = 9.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Vertical Floating Control Pill
                val pillShape = RoundedCornerShape(24.dp)
                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .height(96.dp)
                        .clip(pillShape)
                        .border(
                            width = 0.5.dp,
                            color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                            shape = pillShape,
                        )
                        .background(SanctumTheme.colors.surface)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = SanctumTheme.colors.textPrimary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp),
                    )
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(0.5.dp)
                            .background(SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f)),
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Locations",
                        tint = SanctumTheme.colors.textPrimary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${uiState.location.uppercase()}  •  ${uiState.dateString.uppercase()}",
                style = SanctumTheme.typography.bodyMedium,
                color = SanctumTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontSize = 11.sp,
            )

            Spacer(modifier = Modifier.height(48.dp))

            // UPCOMING PRAYER label
            Text(
                text = "UPCOMING PRAYER",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Background glow and countdown details
            if (uiState.upcomingPrayerName.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    val glowColor = SanctumTheme.colors.brand
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.85f)
                            .aspectRatio(1f)
                            .background(
                                color = glowColor.copy(alpha = 0.05f),
                                shape = CircleShape,
                            ),
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "${uiState.upcomingPrayerName} in",
                            style = SanctumTheme.typography.displayMedium,
                            color = SanctumTheme.colors.brand,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = uiState.hoursRemaining,
                                fontSize = 80.sp,
                                color = SanctumTheme.colors.textPrimary,
                                fontWeight = FontWeight.ExtraLight,
                                letterSpacing = (-2).sp,
                                lineHeight = 80.sp,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "h",
                                fontSize = 24.sp,
                                color = SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = uiState.minutesRemaining,
                                fontSize = 80.sp,
                                color = SanctumTheme.colors.textPrimary,
                                fontWeight = FontWeight.ExtraLight,
                                letterSpacing = (-2).sp,
                                lineHeight = 80.sp,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "m",
                                fontSize = 24.sp,
                                color = SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Take a moment to prepare your heart and\nmind for the ${uiState.upcomingPrayerName.lowercase()} devotion.",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 48.dp),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    val glowColor = SanctumTheme.colors.brand
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.85f)
                            .aspectRatio(1f)
                            .background(glowColor.copy(alpha = 0.05f), shape = CircleShape),
                    )
                    Text(
                        text = "Awaiting devotion schedule...",
                        style = SanctumTheme.typography.bodyMedium,
                        color = SanctumTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ─── Fasting Countdown ───────────────────────────────
            if (config.hasFastingTracker) {
                val liveTime = remember { mutableStateOf(kotlinx.datetime.Clock.System.now().toEpochMilliseconds()) }

                LaunchedEffect(Unit) {
                    while (true) {
                        // Calculate delay until the next full minute starts
                        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                        val delayMillis = 60_000L - (now % 60_000L)
                        kotlinx.coroutines.delay(delayMillis)
                        liveTime.value = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                    }
                }

                // Determine fasting state from current prayers using FastingCountdownEngine
                val engine = remember { com.sanctum.core.feature.fasting.domain.FastingCountdownEngine() }
                val fastingState = engine.calculateFastingState(liveTime.value, uiState.prayers)
                com.sanctum.core.feature.fasting.presentation.SuhoorIftarTimerCard(fastingState = fastingState)
            }

            // ─── Daily Schedule ──────────────────────────────────
            DailyScheduleRow(prayers = uiState.prayers, hazeState = hazeState)

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.xxl))

            // ─── Verse of the Day ────────────────────────────────
            Box(modifier = Modifier.padding(horizontal = SanctumTheme.spacing.lg)) {
                VerseOfTheDayCard(
                    originalText = "\u0671\u0644\u0644\u064e\u0651\u0647\u064f \u0644\u064e\u0622 \u0625\u0650\u0644\u064e\u0651\u0627 \u0647\u064f\u0648\u064e \u0671\u0644\u0652\u062d\u064e\u0649\u064f\u0651 \u0671\u0644\u0652\u0642\u064e\u064a\u064f\u0651\u0648\u0645\u064f \u06da",
                    translation = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of existence.",
                    reference = "Al-Baqarah 2:255",
                    hazeState = hazeState,
                )
            }

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.bottomNavPadding))
        }
    }
}
