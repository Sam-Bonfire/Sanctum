package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
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
        contentAlignment = Alignment.TopCenter,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = SanctumTheme.colors.brand,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            val chapters = uiState.chapters
            val config = LocalWhiteLabelConfig.current
            val companionName = config.appName.substringAfter(":").trim()

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
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth(),
                ) {
                    // ─── Cinematic Hero Header ──────────
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            SanctumTheme.colors.brand.copy(alpha = 0.08f),
                                            SanctumTheme.colors.brand.copy(alpha = 0.02f),
                                        ),
                                    ),
                                )
                                .border(
                                    width = 0.5.dp,
                                    color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .padding(24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "SCRIPTURE INDEX",
                                    fontSize = 11.sp,
                                    color = SanctumTheme.colors.brand,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 3.sp,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sacred Writings",
                                    fontSize = 28.sp,
                                    color = SanctumTheme.colors.textPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                    letterSpacing = 2.sp,
                                )
                            }
                        }
                    }

                    // ─── Section Header ──────────
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SanctumSectionHeader(
                            text = "TABLE OF CONTENTS",
                            modifier = Modifier.padding(horizontal = 0.dp),
                        )
                    }

                    // ─── Chapters Grid ──────────
                    items(chapters) { chapter ->
                        ChapterCard(chapter = chapter, onClick = { onChapterClick(chapter.id) })
                    }

                    // ─── Bottom Navigation Spacer ──────────
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(SanctumTheme.spacing.bottomNavPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterCard(chapter: ScriptureChapter, onClick: () -> Unit) {
    SanctumCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clickable { onClick() },
        contentPadding = PaddingValues(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SanctumTheme.colors.brand.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = chapter.number.toString(),
                    style = SanctumTheme.typography.titleMedium,
                    color = SanctumTheme.colors.brand,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = chapter.title ?: "Chapter ${chapter.number}",
                style = SanctumTheme.typography.bodyLarge,
                color = SanctumTheme.colors.textPrimary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap to read",
                fontSize = 11.sp,
                color = SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Light,
            )
        }
    }
}
