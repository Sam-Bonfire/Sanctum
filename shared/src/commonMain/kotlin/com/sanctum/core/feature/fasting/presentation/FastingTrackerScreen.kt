package com.sanctum.core.feature.fasting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.fasting.data.FastingRepository
import com.sanctum.core.feature.fasting.domain.FastingDayRecord
import com.sanctum.core.feature.fasting.domain.FastingStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object FastingTrackerDependencyHelper : KoinComponent {
    val settings: Settings by inject()
}

@Composable
fun FastingTrackerScreen() {
    val settings = FastingTrackerDependencyHelper.settings
    val repository = remember { FastingRepository(settings) }
    val records = remember { mutableStateOf<List<FastingDayRecord>>(emptyList()) }

    LaunchedEffect(Unit) {
        records.value = repository.getAllRecords()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background)
            .padding(top = 48.dp, start = 24.dp, end = 24.dp),
    ) {
        Text(
            text = "Ramadan",
            style = SanctumTheme.typography.displayMedium,
            color = SanctumTheme.colors.textPrimary,
        )
        Text(
            text = "FASTING TRACKER",
            style = SanctumTheme.typography.labelMedium,
            color = SanctumTheme.colors.brand,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress & Metrics
        val completedCount = records.value.count { it.status == FastingStatus.COMPLETED }
        val currentStreak = calculateStreak(records.value)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MetricCard("COMPLETED", "$completedCount / 30", Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            MetricCard("STREAK", "$currentStreak days", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(records.value) { record ->
                FastingDayCard(
                    record = record,
                    onStatusChange = { newStatus ->
                        val updatedRecord = record.copy(status = newStatus)
                        repository.saveRecord(updatedRecord)
                        records.value = repository.getAllRecords()
                    },
                    onNotesChange = { newNotes ->
                        val updatedRecord = record.copy(notes = newNotes)
                        repository.saveRecord(updatedRecord)
                        records.value = repository.getAllRecords()
                    },
                )
            }
        }
    }
}

fun calculateStreak(records: List<FastingDayRecord>): Int {
    var maxStreak = 0
    var currentStreak = 0

    // Streak logic: simple consecutive completed days
    for (record in records) {
        if (record.status == FastingStatus.COMPLETED) {
            currentStreak++
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak
            }
        } else {
            currentStreak = 0
        }
    }
    return maxStreak
}

@Composable
fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(SanctumTheme.colors.surface)
            .border(0.5.dp, SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f), shape)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = SanctumTheme.typography.labelSmall,
            color = SanctumTheme.colors.textSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = SanctumTheme.typography.titleLarge,
            color = SanctumTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun FastingDayCard(
    record: FastingDayRecord,
    onStatusChange: (FastingStatus?) -> Unit,
    onNotesChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(16.dp)
    val isCompleted = record.status == FastingStatus.COMPLETED

    val bgColor = if (isCompleted) {
        SanctumTheme.colors.brand.copy(alpha = 0.05f)
    } else {
        SanctumTheme.colors.surface
    }

    val borderColor = if (isCompleted) {
        SanctumTheme.colors.brand.copy(alpha = 0.3f)
    } else {
        SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bgColor)
            .border(width = 0.5.dp, color = borderColor, shape = shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val nextStatus = if (isCompleted) null else FastingStatus.COMPLETED
                    onStatusChange(nextStatus)
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Day ${record.dayOfRamadan}",
                    style = SanctumTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = SanctumTheme.colors.textPrimary,
                )
                if (isCompleted) {
                    Text(
                        text = "Fast Completed",
                        style = SanctumTheme.typography.labelSmall,
                        color = SanctumTheme.colors.brand,
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Notes",
                tint = SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { expanded = !expanded },
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Status",
                    tint = SanctumTheme.colors.brand,
                    modifier = Modifier.size(28.dp),
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .border(2.dp, SanctumTheme.colors.textSecondary.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape),
                )
            }
        }

        if (expanded || record.notes.isNotEmpty()) {
            OutlinedTextField(
                value = record.notes,
                onValueChange = onNotesChange,
                placeholder = {
                    Text(
                        "Add a reflection...",
                        color = SanctumTheme.colors.textSecondary.copy(alpha = 0.5f),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                textStyle = SanctumTheme.typography.bodyMedium.copy(color = SanctumTheme.colors.textPrimary),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = SanctumTheme.colors.surface,
                    unfocusedBorderColor = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                    focusedBorderColor = SanctumTheme.colors.brand,
                ),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
            )
        }
    }
}
