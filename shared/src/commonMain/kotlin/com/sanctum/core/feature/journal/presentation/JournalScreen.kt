package com.sanctum.core.feature.journal.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.journal.domain.JournalEntry

@Composable
fun JournalScreen(
    uiState: JournalUiState,
    onEntryClick: (Int) -> Unit,
    onCreateNewEntry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Journal", color = SanctumTheme.colors.textPrimary) },
                backgroundColor = SanctumTheme.colors.background,
                elevation = 0.dp,
                actions = {
                    IconButton(onClick = onCreateNewEntry) {
                        Icon(Icons.Default.Add, contentDescription = "New Entry", tint = SanctumTheme.colors.brand)
                    }
                },
            )
        },
        backgroundColor = SanctumTheme.colors.background,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.entries) { entry ->
                JournalEntryItem(
                    entry = entry,
                    onClick = { onEntryClick(entry.id) },
                )
            }
        }
    }
}

@Composable
fun JournalEntryItem(entry: JournalEntry, onClick: () -> Unit) {
    androidx.compose.material.Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = SanctumTheme.colors.surface,
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = entry.title.ifEmpty { "Untitled" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SanctumTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.content,
                fontSize = 14.sp,
                color = SanctumTheme.colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (entry.verseId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reflecting on verse ${entry.verseId}",
                    fontSize = 12.sp,
                    color = SanctumTheme.colors.brand,
                )
            }
        }
    }
}
