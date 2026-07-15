package com.sanctum.core.feature.duas.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.ErrorRetryView
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun DuasCatalogScreen(viewModel: DuasCatalogViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
        contentAlignment = Alignment.TopCenter,
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = SanctumTheme.colors.brand,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            state.error != null -> {
                ErrorRetryView(
                    message = state.error!!,
                    onRetry = viewModel::load,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            state.duas.isEmpty() -> {
                Text(
                    text = "No supplications available.",
                    style = SanctumTheme.typography.bodyLarge,
                    color = SanctumTheme.colors.textSecondary,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(
                        top = 48.dp,
                        bottom = SanctumTheme.spacing.bottomNavPadding,
                    ),
                ) {
                    // ─── Cinematic Hero Header ──────────
                    item {
                        val config = LocalWhiteLabelConfig.current
                        val pageTitle = config.navItems.find { it.id == "duas" }?.label ?: "SUPPLICATIONS"

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
                                    text = "DEVOTIONAL PRAYERS",
                                    fontSize = 11.sp,
                                    color = SanctumTheme.colors.brand,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 3.sp,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = pageTitle,
                                    fontSize = 28.sp,
                                    color = SanctumTheme.colors.textPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                    letterSpacing = 2.sp,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // ─── Section Header ──────────
                    item {
                        SanctumSectionHeader(
                            text = "COLLECTION",
                            modifier = Modifier,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // ─── Dua Cards ──────────
                    for (dua in state.duas) {
                        item {
                            SanctumCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = SanctumTheme.spacing.xl),
                                contentPadding = PaddingValues(SanctumTheme.spacing.xl),
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = dua.title.uppercase(),
                                        style = SanctumTheme.typography.labelMedium,
                                        color = SanctumTheme.colors.brand,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp,
                                    )
                                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.lg))
                                    Text(
                                        text = dua.originalText,
                                        style = SanctumTheme.typography.headlineMedium,
                                        color = SanctumTheme.colors.textPrimary,
                                        lineHeight = 40.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                    Spacer(modifier = Modifier.height(SanctumTheme.spacing.md))
                                    Text(
                                        text = dua.translation,
                                        style = SanctumTheme.typography.bodyLarge,
                                        color = SanctumTheme.colors.textSecondary,
                                        lineHeight = 24.sp,
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
