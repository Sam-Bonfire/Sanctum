package com.sanctum.core.feature.journal.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.components.SanctumTextField
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.journal.domain.JournalEntry

@Composable
fun JournalDetailScreen(
    entry: JournalEntry?,
    onSave: (title: String, content: String) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(entry) {
        if (entry != null) {
            title = entry.title
            content = entry.content
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Journal Entry", color = SanctumTheme.colors.textPrimary) },
                backgroundColor = SanctumTheme.colors.background,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        onSave(title, content)
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SanctumTheme.colors.brand)
                    }
                },
                actions = {
                    if (entry != null && entry.id != 0) {
                        IconButton(onClick = {
                            onDelete()
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Entry", tint = SanctumTheme.colors.error)
                        }
                    }
                },
            )
        },
        backgroundColor = SanctumTheme.colors.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            if (entry?.verseId != null) {
                Text(
                    text = "Reflection on Verse ${entry.verseId}",
                    color = SanctumTheme.colors.brand,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            SanctumTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title",
            )
            Spacer(modifier = Modifier.height(16.dp))
            SanctumTextField(
                value = content,
                onValueChange = { content = it },
                label = "Your reflection...",
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}
