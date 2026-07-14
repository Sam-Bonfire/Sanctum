package com.sanctum.core.feature.duas.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.components.ErrorRetryView
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun DuasCatalogScreen(viewModel: DuasCatalogViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(SanctumTheme.colors.background)) {
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
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp),
                ) {
                    item {
                        Text(
                            text = "SUPPLICATIONS",
                            style = SanctumTheme.typography.displayMedium,
                            color = SanctumTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .width(48.dp)
                                .background(SanctumTheme.colors.brand.copy(alpha = 0.5f)),
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                    }

                    for (dua in state.duas) {
                        item {
                            SanctumCard(
                                modifier = Modifier.fillMaxWidth().padding(bottom = SanctumTheme.spacing.xl),
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
