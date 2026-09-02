package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumChip
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.BookmarkTag

@Composable
fun BookmarkActionBottomSheet(
    availableTags: List<BookmarkTag>,
    verseTags: List<BookmarkTag>,
    onDismiss: () -> Unit,
    onToggleBookmark: () -> Unit,
    onAssignTag: (Int) -> Unit,
    onUnassignTag: (Int) -> Unit,
    onCreateTag: (String, String) -> Unit,
    isBookmarked: Boolean,
) {
    Dialog(onDismissRequest = onDismiss) {
        SanctumCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(24.dp),
        ) {
            Column {
                Text(
                    text = "BOOKMARK OPTIONS",
                    style = SanctumTheme.typography.labelMedium,
                    color = SanctumTheme.colors.brand,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleBookmark() }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (isBookmarked) "Remove Bookmark" else "Add Bookmark",
                        style = SanctumTheme.typography.bodyLarge,
                        color = SanctumTheme.colors.textPrimary,
                        modifier = Modifier.weight(1f),
                    )
                }

                if (isBookmarked) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "TAGS",
                        style = SanctumTheme.typography.labelSmall,
                        color = SanctumTheme.colors.textSecondary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Simple tag list
                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(availableTags) { tag ->
                            val isSelected = verseTags.any { it.id == tag.id }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isSelected) onUnassignTag(tag.id) else onAssignTag(tag.id)
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(tag.colorHex.toLong(16) or 0xFF000000)),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = tag.name,
                                    style = SanctumTheme.typography.bodyMedium,
                                    color = SanctumTheme.colors.textPrimary,
                                    modifier = Modifier.weight(1f),
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = SanctumTheme.colors.brand,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    var isCreatingTag by remember { mutableStateOf(false) }

                    if (!isCreatingTag) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isCreatingTag = true }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Create Tag",
                                tint = SanctumTheme.colors.brand,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Create New Tag",
                                style = SanctumTheme.typography.bodyMedium,
                                color = SanctumTheme.colors.brand,
                            )
                        }
                    } else {
                        // Quick add inline
                        var newTagName by remember { mutableStateOf("") }
                        androidx.compose.material.OutlinedTextField(
                            value = newTagName,
                            onValueChange = { newTagName = it },
                            label = { Text("Tag Name") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            SanctumChip(
                                text = "Cancel",
                                onClick = { isCreatingTag = false },
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            SanctumChip(
                                text = "Create",
                                selected = true,
                                onClick = {
                                    if (newTagName.isNotBlank()) {
                                        onCreateTag(newTagName, "FF9C27B0")
                                        isCreatingTag = false
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
