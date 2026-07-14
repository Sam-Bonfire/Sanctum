package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.components.SanctumEditorialCard
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
import com.sanctum.core.core.designsystem.theme.SanctumTheme

data class PrayerTime(val name: String, val time: String, val amPm: String, val isCurrent: Boolean = false)

@Composable
fun TimeOfDayIcon(
    name: String,
    isCurrent: Boolean,
    modifier: Modifier = Modifier,
) {
    val brandColor = SanctumTheme.colors.brand
    val iconColor = if (isCurrent) brandColor else SanctumTheme.colors.textSecondary.copy(alpha = 0.7f)

    androidx.compose.foundation.Canvas(modifier = modifier.size(32.dp)) {
        val w = size.width
        val h = size.height
        val strokeWidth = 1.5.dp.toPx()
        val cap = StrokeCap.Round

        val normalizedName = name.lowercase()
        when {
            normalizedName.contains("fajr") || normalizedName.contains("sunrise") || normalizedName.contains("shacharit") -> {
                val horizonY = h * 0.7f
                drawLine(
                    color = iconColor,
                    start = Offset(w * 0.1f, horizonY),
                    end = Offset(w * 0.9f, horizonY),
                    strokeWidth = strokeWidth,
                    cap = cap,
                )
                drawArc(
                    color = iconColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.3f, h * 0.35f),
                    size = Size(w * 0.4f, h * 0.7f),
                    style = Stroke(width = strokeWidth),
                )
                drawLine(color = iconColor, start = Offset(w * 0.5f, h * 0.2f), end = Offset(w * 0.5f, h * 0.3f), strokeWidth = strokeWidth, cap = cap)
                drawLine(color = iconColor, start = Offset(w * 0.25f, h * 0.35f), end = Offset(w * 0.35f, h * 0.42f), strokeWidth = strokeWidth, cap = cap)
                drawLine(color = iconColor, start = Offset(w * 0.75f, h * 0.35f), end = Offset(w * 0.65f, h * 0.42f), strokeWidth = strokeWidth, cap = cap)
            }
            normalizedName.contains("dhuhr") || normalizedName.contains("noon") || normalizedName.contains("mincha") -> {
                val r = w * 0.2f
                drawCircle(
                    color = iconColor,
                    radius = r,
                    center = Offset(w / 2f, h / 2f),
                    style = Stroke(width = strokeWidth),
                )
                for (i in 0 until 8) {
                    val angle = i * 45.0 * kotlin.math.PI / 180.0
                    val startX = (w / 2f) + (r + 4.dp.toPx()) * kotlin.math.cos(angle).toFloat()
                    val startY = (h / 2f) + (r + 4.dp.toPx()) * kotlin.math.sin(angle).toFloat()
                    val endX = (w / 2f) + (r + 8.dp.toPx()) * kotlin.math.cos(angle).toFloat()
                    val endY = (h / 2f) + (r + 8.dp.toPx()) * kotlin.math.sin(angle).toFloat()
                    drawLine(
                        color = iconColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = strokeWidth,
                        cap = cap,
                    )
                }
            }
            normalizedName.contains("asr") || normalizedName.contains("afternoon") -> {
                val sunR = w * 0.16f
                drawCircle(
                    color = iconColor,
                    radius = sunR,
                    center = Offset(w * 0.4f, h * 0.4f),
                    style = Stroke(width = strokeWidth),
                )
                val cloudPath = Path().apply {
                    moveTo(w * 0.25f, h * 0.7f)
                    quadraticTo(w * 0.2f, h * 0.55f, w * 0.35f, h * 0.52f)
                    quadraticTo(w * 0.45f, h * 0.38f, w * 0.6f, h * 0.48f)
                    quadraticTo(w * 0.75f, h * 0.45f, w * 0.78f, h * 0.58f)
                    quadraticTo(w * 0.85f, h * 0.65f, w * 0.78f, h * 0.72f)
                    lineTo(w * 0.25f, h * 0.72f)
                    close()
                }
                drawPath(
                    path = cloudPath,
                    color = iconColor,
                    style = Stroke(width = strokeWidth),
                )
            }
            normalizedName.contains("maghrib") || normalizedName.contains("sunset") || normalizedName.contains("arvit") -> {
                val horizonY = h * 0.7f
                drawLine(
                    color = iconColor,
                    start = Offset(w * 0.1f, horizonY),
                    end = Offset(w * 0.9f, horizonY),
                    strokeWidth = strokeWidth,
                    cap = cap,
                )
                drawArc(
                    color = iconColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.3f, h * 0.5f),
                    size = Size(w * 0.4f, h * 0.4f),
                    style = Stroke(width = strokeWidth),
                )
                drawLine(color = iconColor, start = Offset(w * 0.5f, h * 0.4f), end = Offset(w * 0.5f, h * 0.45f), strokeWidth = strokeWidth, cap = cap)
                drawLine(color = iconColor, start = Offset(w * 0.35f, h * 0.45f), end = Offset(w * 0.42f, h * 0.5f), strokeWidth = strokeWidth, cap = cap)
                drawLine(color = iconColor, start = Offset(w * 0.65f, h * 0.45f), end = Offset(w * 0.58f, h * 0.5f), strokeWidth = strokeWidth, cap = cap)
            }
            normalizedName.contains("isha") || normalizedName.contains("night") -> {
                val moonPath = Path().apply {
                    moveTo(w * 0.35f, h * 0.3f)
                    quadraticTo(w * 0.65f, h * 0.35f, w * 0.55f, h * 0.7f)
                    quadraticTo(w * 0.25f, h * 0.75f, w * 0.3f, h * 0.45f)
                    quadraticTo(w * 0.2f, h * 0.35f, w * 0.35f, h * 0.3f)
                    close()
                }
                drawPath(
                    path = moonPath,
                    color = iconColor,
                    style = Stroke(width = strokeWidth),
                )
                drawCircle(color = iconColor, radius = 1.5f, center = Offset(w * 0.7f, h * 0.35f))
                drawCircle(color = iconColor, radius = 1f, center = Offset(w * 0.6f, h * 0.5f))
            }
            else -> {
                val starPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.25f)
                    lineTo(w * 0.58f, h * 0.42f)
                    lineTo(w * 0.77f, h * 0.45f)
                    lineTo(w * 0.63f, h * 0.58f)
                    lineTo(w * 0.66f, h * 0.77f)
                    lineTo(w * 0.5f, h * 0.68f)
                    lineTo(w * 0.34f, h * 0.77f)
                    lineTo(w * 0.37f, h * 0.58f)
                    lineTo(w * 0.23f, h * 0.45f)
                    lineTo(w * 0.42f, h * 0.42f)
                    close()
                }
                drawPath(
                    path = starPath,
                    color = iconColor,
                    style = Stroke(width = strokeWidth),
                )
            }
        }
    }
}

@Composable
fun DailyScheduleRow(prayers: List<PrayerTime>, hazeState: dev.chrisbanes.haze.HazeState? = null) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SanctumSectionHeader(
            text = "DAILY SCHEDULE",
            modifier = Modifier.padding(horizontal = SanctumTheme.spacing.xl - 8.dp),
        )

        Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

        if (prayers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .padding(horizontal = SanctumTheme.spacing.xl),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No devotions scheduled for today.",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = SanctumTheme.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(SanctumTheme.spacing.md),
            ) {
                val currentIndex = prayers.indexOfFirst { it.isCurrent }
                prayers.forEachIndexed { index, prayer ->
                    val isPast = currentIndex != -1 && index < currentIndex
                    val isFuture = currentIndex != -1 && index > currentIndex
                    val cardWidth = if (prayer.isCurrent) 140.dp else 120.dp
                    val cardHeight = if (prayer.isCurrent) 190.dp else 170.dp
                    val alpha = if (isPast) 0.6f else 1.0f

                    item {
                        val activeBorder = if (prayer.isCurrent) {
                            Modifier.border(width = 0.5.dp, color = SanctumTheme.colors.brand, shape = RoundedCornerShape(16.dp))
                        } else {
                            Modifier
                        }

                        Box(
                            modifier = Modifier
                                .alpha(alpha)
                                .then(activeBorder),
                        ) {
                            SanctumEditorialCard(
                                hazeState = hazeState,
                                modifier = Modifier
                                    .width(cardWidth)
                                    .height(cardHeight),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                val tintColor = if (prayer.isCurrent) {
                                    SanctumTheme.colors.brand.copy(alpha = 0.05f)
                                } else {
                                    androidx.compose.ui.graphics.Color.Transparent
                                }

                                Box(modifier = Modifier.fillMaxSize().background(tintColor)) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Text(
                                            text = prayer.name.uppercase(),
                                            fontSize = 12.sp,
                                            color = if (prayer.isCurrent) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 2.sp,
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        TimeOfDayIcon(name = prayer.name, isCurrent = prayer.isCurrent)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = prayer.time,
                                            fontSize = 24.sp,
                                            color = SanctumTheme.colors.textPrimary,
                                            fontWeight = FontWeight.Light,
                                            letterSpacing = 1.sp,
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = prayer.amPm.uppercase(),
                                            fontSize = 11.sp,
                                            color = SanctumTheme.colors.textSecondary,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 2.sp,
                                        )

                                        if (prayer.isCurrent) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "NOW",
                                                fontSize = 9.sp,
                                                color = SanctumTheme.colors.brand,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 3.sp,
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Icon(
                                                imageVector = Icons.Default.Notifications,
                                                contentDescription = "Notifications",
                                                tint = SanctumTheme.colors.textSecondary.copy(alpha = 0.35f),
                                                modifier = Modifier.size(16.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerseOfTheDayCard(
    modifier: Modifier = Modifier,
    originalText: String,
    translation: String,
    reference: String,
    isRtl: Boolean = false,
    hazeState: dev.chrisbanes.haze.HazeState? = null,
) {
    SanctumEditorialCard(
        hazeState = hazeState,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "DAILY REFLECTION",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.brand,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Decorative Quote Mark
            Text(
                text = "“",
                fontSize = 54.sp,
                color = SanctumTheme.colors.brand.copy(alpha = 0.35f),
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                lineHeight = 24.sp,
                modifier = Modifier.height(32.dp),
            )

            if (originalText.isNotEmpty() && originalText != translation) {
                Text(
                    text = originalText,
                    fontSize = 24.sp,
                    color = SanctumTheme.colors.textPrimary,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 38.sp,
                    fontFamily = SanctumTheme.typography.amiri,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = translation,
                fontSize = 19.sp,
                color = SanctumTheme.colors.textPrimary,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Flanked Divider for Citation Reference
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(SanctumTheme.colors.brand.copy(alpha = 0.25f)),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = reference.uppercase(),
                    fontSize = 11.sp,
                    color = SanctumTheme.colors.brand,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(SanctumTheme.colors.brand.copy(alpha = 0.25f)),
                )
            }
        }
    }
}
