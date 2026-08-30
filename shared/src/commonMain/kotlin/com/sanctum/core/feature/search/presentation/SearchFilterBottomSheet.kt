package com.sanctum.core.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.search.domain.AnnotationFilter
import com.sanctum.core.feature.search.domain.SearchFilterState
import com.sanctum.core.feature.search.domain.SortOrder
import com.sanctum.core.feature.search.presentation.components.FilterChip

@Composable
fun SearchFilterBottomSheet(
    filterState: SearchFilterState,
    onFilterStateChanged: (SearchFilterState) -> Unit,
    onClose: () -> Unit,
    availableBooks: List<Pair<String, String>> = emptyList(),
    availableDivisions: List<String> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SanctumTheme.colors.surface)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Advanced Filters",
                style = SanctumTheme.typography.titleMedium,
                color = SanctumTheme.colors.textPrimary,
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = SanctumTheme.colors.textSecondary,
                modifier = Modifier.clickable(onClick = onClose),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Sort By", style = SanctumTheme.typography.labelMedium, color = SanctumTheme.colors.textSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(SortOrder.values()) { order ->
                FilterChip(
                    text = order.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                    isSelected = filterState.sortOrder == order,
                    onClick = { onFilterStateChanged(filterState.copy(sortOrder = order)) },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Annotations", style = SanctumTheme.typography.labelMedium, color = SanctumTheme.colors.textSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(AnnotationFilter.values()) { annotation ->
                FilterChip(
                    text = if (annotation == AnnotationFilter.NONE) "Any" else annotation.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                    isSelected = filterState.requireAnnotation == annotation,
                    onClick = { onFilterStateChanged(filterState.copy(requireAnnotation = annotation)) },
                )
            }
        }

        if (availableBooks.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Books", style = SanctumTheme.typography.labelMedium, color = SanctumTheme.colors.textSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        text = "All",
                        isSelected = filterState.bookId == null,
                        onClick = { onFilterStateChanged(filterState.copy(bookId = null)) },
                    )
                }
                items(availableBooks) { book ->
                    FilterChip(
                        text = book.second,
                        isSelected = filterState.bookId == book.first,
                        onClick = { onFilterStateChanged(filterState.copy(bookId = book.first)) },
                    )
                }
            }
        }

        if (availableDivisions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Divisions", style = SanctumTheme.typography.labelMedium, color = SanctumTheme.colors.textSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        text = "All",
                        isSelected = filterState.division == null,
                        onClick = { onFilterStateChanged(filterState.copy(division = null)) },
                    )
                }
                items(availableDivisions) { division ->
                    FilterChip(
                        text = division,
                        isSelected = filterState.division == division,
                        onClick = { onFilterStateChanged(filterState.copy(division = division)) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
