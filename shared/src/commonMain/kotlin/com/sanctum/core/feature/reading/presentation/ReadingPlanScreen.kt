package com.sanctum.core.feature.reading.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun ReadingPlanScreen(viewModel: ReadingPlanViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = SanctumTheme.colors.brand,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(
                        top = 48.dp,
                        bottom = SanctumTheme.spacing.bottomNavPadding,
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Text(
                            text = "READING PLANS",
                            style = SanctumTheme.typography.labelMedium,
                            color = SanctumTheme.colors.brand,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp,
                        )
                        Text(
                            text = "Your Journey",
                            style = SanctumTheme.typography.displayMedium,
                            color = SanctumTheme.colors.textPrimary,
                        )
                    }

                    if (state.enrolledPlans.isEmpty()) {
                        item {
                            Text(
                                text = "Enroll in a plan to start reading daily.",
                                style = SanctumTheme.typography.bodyLarge,
                                color = SanctumTheme.colors.textSecondary,
                            )
                        }
                    } else {
                        state.enrolledPlans.forEach { planState ->
                            item {
                                EnrolledPlanCard(
                                    planState = planState,
                                    onUnenroll = { viewModel.unenroll(planState.plan.id) },
                                    onToggleCheckpoint = { key, completed ->
                                        viewModel.toggleCheckpoint(planState.plan.id, key, completed)
                                    },
                                )
                            }
                        }
                    }

                    if (state.availablePlans.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "AVAILABLE PLANS",
                                style = SanctumTheme.typography.labelMedium,
                                color = SanctumTheme.colors.brand,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp,
                            )
                        }
                        state.availablePlans.forEach { plan ->
                            item {
                                AvailablePlanCard(
                                    planTitle = plan.title,
                                    planDescription = plan.description,
                                    dayCount = plan.dayCount,
                                    onEnroll = { viewModel.enroll(plan.id) },
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
fun EnrolledPlanCard(
    planState: com.sanctum.core.feature.reading.domain.ReadingPlanState,
    onUnenroll: () -> Unit,
    onToggleCheckpoint: (String, Boolean) -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SanctumTheme.colors.surface)
            .border(0.5.dp, SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f), shape)
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = planState.plan.title,
                    style = SanctumTheme.typography.titleMedium,
                    color = SanctumTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${planState.plan.dayCount} days",
                    style = SanctumTheme.typography.labelSmall,
                    color = SanctumTheme.colors.textSecondary,
                )
            }
            Text(
                text = "Leave",
                style = SanctumTheme.typography.labelMedium,
                color = SanctumTheme.colors.brand,
                modifier = Modifier
                    .clip(shape)
                    .clickable(onClick = onUnenroll)
                    .padding(8.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(SanctumTheme.colors.outlineVariant.copy(alpha = 0.3f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(planState.completionPercent.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SanctumTheme.colors.brand),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Streak: ${planState.streakDays} days",
            style = SanctumTheme.typography.bodyMedium,
            color = SanctumTheme.colors.textPrimary,
        )

        // Today's checkpoints
        val todayKeys = planState.todayCheckpointKeys
        todayKeys.forEachIndexed { index, key ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        val isDone = planState.progress.completedCheckpoints.contains(key)
                        onToggleCheckpoint(key, !isDone)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Complete checkpoint ${index + 1}",
                    tint = if (planState.progress.completedCheckpoints.contains(key)) {
                        SanctumTheme.colors.brand
                    } else {
                        SanctumTheme.colors.textSecondary.copy(alpha = 0.35f)
                    },
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Day ${planState.currentDayIndex() + 1}",
                        style = SanctumTheme.typography.labelSmall,
                        color = SanctumTheme.colors.textSecondary,
                    )
                    Text(
                        text = "Reading ${index + 1}",
                        style = SanctumTheme.typography.bodyMedium,
                        color = SanctumTheme.colors.textPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun AvailablePlanCard(
    planTitle: String,
    planDescription: String,
    dayCount: Int,
    onEnroll: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SanctumTheme.colors.surface)
            .border(0.5.dp, SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f), shape)
            .padding(20.dp),
    ) {
        Text(
            text = planTitle,
            style = SanctumTheme.typography.titleMedium,
            color = SanctumTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = planDescription,
            style = SanctumTheme.typography.bodySmall,
            color = SanctumTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Start plan ($dayCount days)",
            style = SanctumTheme.typography.labelMedium,
            color = Color.White,
            modifier = Modifier
                .clip(shape)
                .background(SanctumTheme.colors.brand)
                .clickable(onClick = onEnroll)
                .padding(horizontal = 16.dp, vertical = 10.dp),
        )
    }
}
