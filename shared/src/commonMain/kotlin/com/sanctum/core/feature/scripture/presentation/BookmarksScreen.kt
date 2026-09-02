package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumChip
import com.sanctum.core.core.designsystem.components.SanctumTextField
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.Bookmark
import com.sanctum.core.feature.scripture.domain.BookmarkTag

@Composable
fun BookmarksScreen(
    bookmarks: List<Bookmark>,
    availableTags: List<BookmarkTag>,
    selectedTagId: Int?,
    onTagSelected: (Int?) -> Unit,
    onDeleteTag: (Int) -> Unit,
    onCreateTag: (String, String) -> Unit,
    onRenameTag: (Int, String) -> Unit,
    onVerseClick: (String) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var tagToRename by remember { mutableStateOf<BookmarkTag?>(null) }
    var tagMenuExpanded by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
    ) {
        // App Bar / Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = "My Bookmarks",
                style = SanctumTheme.typography.displayMedium,
                color = SanctumTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
            )
        }

        // Tag Filters
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SanctumChip(
                    text = "+ New Tag",
                    selected = false,
                    onClick = { showCreateDialog = true },
                )
            }
            if (availableTags.isNotEmpty()) {
                item {
                    SanctumChip(
                        text = "All",
                        selected = selectedTagId == null,
                        onClick = { onTagSelected(null) },
                    )
                }
                items(availableTags) { tag ->
                    Box {
                        SanctumChip(
                            text = tag.name,
                            selected = selectedTagId == tag.id,
                            onClick = { onTagSelected(tag.id) },
                        )
                        IconButton(
                            onClick = { tagMenuExpanded = tag.id },
                            modifier = Modifier.align(Alignment.CenterEnd).size(24.dp),
                        ) {
                            Icon(Icons.Default.MoreVert, "Options", modifier = Modifier.size(16.dp))
                        }
                        DropdownMenu(
                            expanded = tagMenuExpanded == tag.id,
                            onDismissRequest = { tagMenuExpanded = null },
                        ) {
                            DropdownMenuItem(onClick = {
                                tagMenuExpanded = null
                                tagToRename = tag
                            }) {
                                Text("Rename")
                            }
                            DropdownMenuItem(onClick = {
                                tagMenuExpanded = null
                                if (selectedTagId == tag.id) onTagSelected(null)
                                onDeleteTag(tag.id)
                            }) {
                                Text("Delete", color = SanctumTheme.colors.error)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Filtered Bookmarks List
        val filteredBookmarks = if (selectedTagId != null) {
            bookmarks.filter { it.tags.any { t -> t.id == selectedTagId } }
        } else {
            bookmarks
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            if (filteredBookmarks.isEmpty()) {
                item {
                    Text(
                        text = "No bookmarks found.",
                        style = SanctumTheme.typography.bodyMedium,
                        color = SanctumTheme.colors.textSecondary,
                        modifier = Modifier.padding(top = 32.dp),
                    )
                }
            } else {
                items(filteredBookmarks, key = { it.id }) { bookmark ->
                    SanctumCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bookmark.verse?.let { onVerseClick(it.id) } },
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        Column {
                            bookmark.verse?.let { verse ->
                                Text(
                                    text = "Verse ${verse.number}",
                                    style = SanctumTheme.typography.labelSmall,
                                    color = SanctumTheme.colors.brand,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = verse.translation,
                                    style = SanctumTheme.typography.bodyMedium,
                                    color = SanctumTheme.colors.textPrimary,
                                    maxLines = 3,
                                )
                            }

                            if (bookmark.tags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    bookmark.tags.forEach { tag ->
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(tag.colorHex.toLong(16) or 0xFF000000).copy(alpha = 0.1f),
                                                    shape = RoundedCornerShape(4.dp),
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp),
                                        ) {
                                            Text(
                                                text = tag.name,
                                                fontSize = 10.sp,
                                                color = Color(tag.colorHex.toLong(16) or 0xFF000000),
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

    if (showCreateDialog) {
        var newTagName by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showCreateDialog = false }) {
            SanctumCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column {
                    Text("Create Tag", style = SanctumTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    SanctumTextField(
                        value = newTagName,
                        onValueChange = { newTagName = it },
                        placeholder = "Tag Name",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        SanctumChip(text = "Cancel", onClick = { showCreateDialog = false })
                        Spacer(modifier = Modifier.width(8.dp))
                        SanctumChip(text = "Create", selected = true, onClick = {
                            if (newTagName.isNotBlank()) {
                                onCreateTag(newTagName, "FF9C27B0")
                                showCreateDialog = false
                            }
                        })
                    }
                }
            }
        }
    }

    tagToRename?.let { tag ->
        var renameText by remember { mutableStateOf(tag.name) }
        Dialog(onDismissRequest = { tagToRename = null }) {
            SanctumCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column {
                    Text("Rename Tag", style = SanctumTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    SanctumTextField(
                        value = renameText,
                        onValueChange = { renameText = it },
                        placeholder = "Tag Name",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        SanctumChip(text = "Cancel", onClick = { tagToRename = null })
                        Spacer(modifier = Modifier.width(8.dp))
                        SanctumChip(text = "Save", selected = true, onClick = {
                            if (renameText.isNotBlank()) {
                                onRenameTag(tag.id, renameText)
                                tagToRename = null
                            }
                        })
                    }
                }
            }
        }
    }
}
