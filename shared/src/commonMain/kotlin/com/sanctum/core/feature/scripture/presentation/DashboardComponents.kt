package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.components.SanctumEditorialCard
import com.sanctum.core.core.designsystem.theme.SanctumTheme

data class PrayerTime(val name: String, val time: String, val amPm: String, val isCurrent: Boolean = false)

@Composable
fun DailyScheduleRow(prayers: List<PrayerTime>, hazeState: dev.chrisbanes.haze.HazeState? = null) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "DAILY SCHEDULE",
            style = SanctumTheme.typography.labelMedium,
            color = SanctumTheme.colors.textSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp,
            modifier = Modifier.padding(horizontal = SanctumTheme.spacing.xl),
        )

        Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

        LazyRow(
            contentPadding = PaddingValues(horizontal = SanctumTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(SanctumTheme.spacing.md),
        ) {
            for (prayer in prayers) {
                item {
                    SanctumEditorialCard(
                        hazeState = hazeState,
                        modifier = Modifier
                            .width(130.dp)
                            .height(180.dp),
                        shape = RoundedCornerShape(32.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        // Current indicator dot
                        if (prayer.isCurrent) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp, end = 16.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SanctumTheme.colors.brand)
                                    .align(Alignment.TopEnd),
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = prayer.name.uppercase(),
                                fontSize = 12.sp,
                                color = SanctumTheme.colors.textSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = prayer.time,
                                fontSize = 28.sp,
                                color = SanctumTheme.colors.textPrimary,
                                fontWeight = FontWeight.Light,
                                letterSpacing = 1.sp,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prayer.amPm.uppercase(),
                                fontSize = 12.sp,
                                color = SanctumTheme.colors.textSecondary,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.sp,
                            )

                            if (prayer.isCurrent) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "NOW",
                                    fontSize = 9.sp,
                                    color = SanctumTheme.colors.brand,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 3.sp,
                                )
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
        Column {
            Text(
                text = "DAILY REFLECTION",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.brand,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
            )
            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            Text(
                text = originalText,
                style = SanctumTheme.typography.headlineMedium,
                color = SanctumTheme.colors.textPrimary,
                fontWeight = FontWeight.Medium,
                lineHeight = 44.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SanctumTheme.colors.textPrimary.copy(alpha = 0.06f)),
            )

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            Text(
                text = translation,
                style = SanctumTheme.typography.bodyLarge,
                color = SanctumTheme.colors.textSecondary,
                lineHeight = 26.sp,
            )

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            Text(
                text = "— $reference",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.brand.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
