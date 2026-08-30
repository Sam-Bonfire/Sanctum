package com.sanctum.core.feature.search.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.search.domain.AnnotationFilter
import com.sanctum.core.feature.search.domain.SearchFilterState
import com.sanctum.core.feature.search.presentation.components.FilterChip

@Composable
fun SearchFilterRow(
    filterState: SearchFilterState,
    onFilterStateChanged: (SearchFilterState) -> Unit,
    onOpenAdvancedFilters: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onOpenAdvancedFilters) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Advanced Filters",
                tint = SanctumTheme.colors.brand,
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            val quickFilters = listOf(
                AnnotationFilter.BOOKMARKED,
                AnnotationFilter.HIGHLIGHTED,
                AnnotationFilter.NOTED,
            )

            items(quickFilters) { filter ->
                FilterChip(
                    text = filter.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                    isSelected = filterState.requireAnnotation == filter,
                    onClick = {
                        val newFilter = if (filterState.requireAnnotation == filter) AnnotationFilter.NONE else filter
                        onFilterStateChanged(filterState.copy(requireAnnotation = newFilter))
                    },
                )
            }
        }
    }
}
