package com.sanctum.core.feature.charity.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumDropdown
import com.sanctum.core.core.designsystem.components.SanctumPrimaryButton
import com.sanctum.core.core.designsystem.components.SanctumTextField
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.charity.domain.CharityCategory
import com.sanctum.core.feature.charity.domain.CharityRecord
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharityTrackerScreen(
    uiState: CharityUiState,
    onAddRecord: (Double, CharityCategory, String?) -> Unit,
    onEditRecord: (String, Double, CharityCategory, String?, String) -> Unit,
    onSetGoal: (Double) -> Unit,
    onDeleteRecord: (String) -> Unit,
) {
    val config = LocalWhiteLabelConfig.current
    var showAddDialog by remember { mutableStateOf(false) }
    var recordToEdit by remember { mutableStateOf<CharityRecord?>(null) }
    var showGoalDialog by remember { mutableStateOf(false) }

    Scaffold(
        backgroundColor = SanctumTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text(config.charityTrackerTitle, style = SanctumTheme.typography.titleLarge) },
                backgroundColor = SanctumTheme.colors.background,
                elevation = 0.dp,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                backgroundColor = SanctumTheme.colors.brand,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Giving", tint = SanctumTheme.colors.surface)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(SanctumTheme.spacing.md),
        ) {
            // Progress Section
            SanctumCard {
                Column(
                    modifier = Modifier.padding(SanctumTheme.spacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Monthly Progress", style = SanctumTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.sm))

                    val progress = uiState.summary.percentageCompletion

                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = progress,
                            modifier = Modifier.size(100.dp),
                            color = SanctumTheme.colors.brand,
                            strokeWidth = 8.dp,
                        )
                        Text(
                            "${(progress * 100).toInt()}%",
                            style = SanctumTheme.typography.headlineLarge,
                        )
                    }

                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.sm))
                    Text(
                        "Given: \$${uiState.summary.totalGiven} / Goal: \$${uiState.summary.goalAmount}",
                        style = SanctumTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.sm))
                    TextButton(onClick = { showGoalDialog = true }) {
                        Text("Edit Goal", color = SanctumTheme.colors.brand)
                    }
                }
            }

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            Text("Recent Contributions", style = SanctumTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(SanctumTheme.spacing.sm))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.records.isEmpty()) {
                Text("No contributions logged this month.", style = SanctumTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    // Grouping logic
                    val groupedRecords = uiState.records.groupBy {
                        try {
                            val instant = Instant.parse(it.dateIso)
                            val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                            "${dt.month.name} ${dt.dayOfMonth}, ${dt.year}"
                        } catch (e: Exception) {
                            "Unknown Date"
                        }
                    }

                    groupedRecords.forEach { (dateStr, recordsForDate) ->
                        item {
                            Text(
                                text = dateStr,
                                style = SanctumTheme.typography.labelMedium,
                                color = SanctumTheme.colors.textSecondary,
                                modifier = Modifier.padding(vertical = SanctumTheme.spacing.xs),
                            )
                        }

                        items(recordsForDate) { record ->
                            SanctumCard(modifier = Modifier.padding(vertical = SanctumTheme.spacing.sm)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(SanctumTheme.spacing.md),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column {
                                        Text("\$${record.amount}", style = SanctumTheme.typography.titleMedium)
                                        Text(record.categoryId.displayName, style = SanctumTheme.typography.bodySmall)
                                        if (!record.privateNotes.isNullOrEmpty()) {
                                            Text(record.privateNotes, style = SanctumTheme.typography.bodySmall, color = SanctumTheme.colors.textSecondary)
                                        }
                                    }
                                    Row {
                                        IconButton(onClick = { recordToEdit = record }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SanctumTheme.colors.textSecondary)
                                        }
                                        IconButton(onClick = { onDeleteRecord(record.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SanctumTheme.colors.error)
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

    if (showAddDialog || recordToEdit != null) {
        val isEditing = recordToEdit != null
        var amount by remember { mutableStateOf(if (isEditing) recordToEdit!!.amount.toString() else "") }
        var category by remember { mutableStateOf(if (isEditing) recordToEdit!!.categoryId else CharityCategory.GENERAL) }
        var notes by remember { mutableStateOf(if (isEditing) recordToEdit!!.privateNotes ?: "" else "") }
        var showCategoryDropdown by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                recordToEdit = null
            },
            title = { Text(if (isEditing) "Edit Contribution" else "Log Contribution") },
            text = {
                Column {
                    SanctumTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = "Amount",
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box {
                        SanctumDropdown(
                            value = category.displayName,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCategoryDropdown = true },
                        )
                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false },
                        ) {
                            CharityCategory.entries.forEach { cat ->
                                DropdownMenuItem(onClick = {
                                    category = cat
                                    showCategoryDropdown = false
                                }) {
                                    Text(cat.displayName)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    SanctumTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = "Private Notes (Optional)",
                    )
                }
            },
            confirmButton = {
                SanctumPrimaryButton(onClick = {
                    val parsedAmount = amount.toDoubleOrNull()
                    if (parsedAmount != null) {
                        if (isEditing) {
                            onEditRecord(recordToEdit!!.id, parsedAmount, category, notes.ifBlank { null }, recordToEdit!!.dateIso)
                        } else {
                            onAddRecord(parsedAmount, category, notes.ifBlank { null })
                        }
                        showAddDialog = false
                        recordToEdit = null
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    recordToEdit = null
                }) {
                    Text("Cancel", color = SanctumTheme.colors.textSecondary)
                }
            },
        )
    }

    if (showGoalDialog) {
        var goalAmount by remember { mutableStateOf(uiState.summary.goalAmount.toString()) }

        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text("Set Monthly Goal") },
            text = {
                SanctumTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    placeholder = "Goal Amount",
                )
            },
            confirmButton = {
                SanctumPrimaryButton(onClick = {
                    val parsed = goalAmount.toDoubleOrNull()
                    if (parsed != null) {
                        onSetGoal(parsed)
                        showGoalDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("Cancel", color = SanctumTheme.colors.textSecondary)
                }
            },
        )
    }
}
