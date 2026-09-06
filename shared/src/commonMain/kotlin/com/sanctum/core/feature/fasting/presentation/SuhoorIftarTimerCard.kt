package com.sanctum.core.feature.fasting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.fasting.domain.FastingPhase
import com.sanctum.core.feature.fasting.domain.FastingState

@Composable
fun SuhoorIftarTimerCard(fastingState: FastingState?) {
    if (fastingState == null) return

    val phaseLabel = when (fastingState.phase) {
        FastingPhase.EATING_WINDOW -> "EATING WINDOW"
        FastingPhase.ACTIVE_FAST -> "ACTIVE FAST"
    }

    val hours = fastingState.remainingHours.toString().padStart(2, '0')
    val minutes = fastingState.remainingMinutes.toString().padStart(2, '0')

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
                text = phaseLabel,
                style = SanctumTheme.typography.labelSmall,
                color = SanctumTheme.colors.textSecondary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontSize = 11.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${fastingState.targetEventName} in",
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
                    text = hours,
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
                    text = minutes,
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
}
