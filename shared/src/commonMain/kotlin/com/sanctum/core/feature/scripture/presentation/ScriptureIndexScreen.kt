package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.ScriptureChapter

@Composable
fun ScriptureIndexScreen(
    uiState: ScriptureUiState,
    onChapterClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = SanctumTheme.colors.brand,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            val chapters = uiState.chapters
            if (chapters.isEmpty()) {
                Text(
                    text = "No chapters available.",
                    style = SanctumTheme.typography.bodyLarge,
                    color = SanctumTheme.colors.textSecondary,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(chapters) { chapter ->
                        ChapterCard(chapter = chapter, onClick = { onChapterClick(chapter.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterCard(chapter: ScriptureChapter, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = SanctumTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = chapter.number.toString(),
                style = SanctumTheme.typography.headlineMedium,
                color = SanctumTheme.colors.brand,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = chapter.title ?: "Chapter ${chapter.number}",
                style = SanctumTheme.typography.bodyLarge,
                color = SanctumTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}
