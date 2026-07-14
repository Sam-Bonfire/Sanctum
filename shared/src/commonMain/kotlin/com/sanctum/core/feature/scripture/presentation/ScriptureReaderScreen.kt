package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.SanctumEditorialCard
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.ScriptureVerse

@Composable
fun ScriptureReaderScreen(
    chapter: ScriptureChapter,
) {
    val config = LocalWhiteLabelConfig.current
    // Capture verses list outside LazyColumn to avoid Wasm coroutine crash
    val verseList = chapter.verses

    Box(modifier = Modifier.fillMaxSize().background(SanctumTheme.colors.background)) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = SanctumTheme.spacing.xxxl,
                bottom = SanctumTheme.spacing.bottomNavPadding,
                start = SanctumTheme.spacing.lg,
                end = SanctumTheme.spacing.lg,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = chapter.title?.uppercase() ?: "CHAPTER ${chapter.number}",
                        style = SanctumTheme.typography.displayMedium,
                        color = SanctumTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                    )
                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.sm))
                    // Decorative underline
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(48.dp)
                            .background(SanctumTheme.colors.brand.copy(alpha = 0.5f)),
                    )
                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.xxl))
                }
            }

            // Use items() to avoid loop variable capturing bugs in Wasm
            items(
                items = verseList,
                key = { it.id },
            ) { verse ->
                ScriptureVerseItem(verse)
                Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))
            }
        }
    }
}

@Composable
fun ScriptureVerseItem(verse: ScriptureVerse) {
    SanctumEditorialCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Verse number pill
            Box(
                modifier = Modifier
                    .background(
                        SanctumTheme.colors.brand.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "${verse.number}",
                    style = SanctumTheme.typography.labelMedium,
                    color = SanctumTheme.colors.brand,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            // Original Text (Arabic, Hebrew, etc.)
            Text(
                text = verse.originalText,
                style = SanctumTheme.typography.headlineMedium,
                color = SanctumTheme.colors.textPrimary,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SanctumTheme.colors.textPrimary.copy(alpha = 0.06f)),
            )

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))

            // Translation
            Text(
                text = verse.translation,
                style = SanctumTheme.typography.bodyLarge,
                color = SanctumTheme.colors.textSecondary,
                lineHeight = 26.sp,
                letterSpacing = 0.3.sp,
            )

            if (verse.transliteration != null) {
                Spacer(modifier = Modifier.height(SanctumTheme.spacing.md))
                Text(
                    text = verse.transliteration,
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.brand.copy(alpha = 0.7f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    lineHeight = 22.sp,
                )
            }
        }
    }
}
