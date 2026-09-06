package com.sanctum.core.feature.names.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.names.domain.DivineName

@Composable
fun NamesOfAllahScreen(viewModel: NamesOfAllahViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = SanctumTheme.colors.brand,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "99 NAMES",
                        style = SanctumTheme.typography.labelMedium,
                        color = SanctumTheme.colors.brand,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                    )
                    Text(
                        text = "Names of Allah",
                        style = SanctumTheme.typography.displayMedium,
                        color = SanctumTheme.colors.textPrimary,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::search,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Search names...",
                                color = SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = SanctumTheme.colors.textSecondary,
                            )
                        },
                        trailingIcon = if (state.searchQuery.isNotEmpty()) {
                            {
                                IconButton(onClick = { viewModel.search("") }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear search",
                                        tint = SanctumTheme.colors.textSecondary,
                                    )
                                }
                            }
                        } else {
                            null
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions.Default,
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = SanctumTheme.colors.surface,
                            unfocusedBorderColor = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                            focusedBorderColor = SanctumTheme.colors.brand,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = SanctumTheme.spacing.bottomNavPadding),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(state.filteredNames) { name ->
                            NameCard(
                                name = name,
                                isFavorite = state.favorites.contains(name.id),
                                onPlay = { viewModel.playAudio(name.audioFileName) },
                                onToggleFavorite = { viewModel.toggleFavorite(name.id) },
                                onClick = { viewModel.selectName(name) },
                            )
                        }
                    }
                }
            }
        }
    }

    state.selectedName?.let { name ->
        NameDetailOverlay(
            name = name,
            isFavorite = state.favorites.contains(name.id),
            isMemorized = state.memorized.contains(name.id),
            onClose = { viewModel.selectName(null) },
            onPlay = { viewModel.playAudio(name.audioFileName) },
            onToggleFavorite = { viewModel.toggleFavorite(name.id) },
            onToggleMemorized = { viewModel.toggleMemorized(name.id) },
            onStopAudio = { viewModel.stopAudio() },
            modifier = Modifier,
        )
    }
}

@Composable
fun NameCard(
    name: DivineName,
    isFavorite: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SanctumTheme.colors.surface)
            .border(0.5.dp, SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f), shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary,
                )
            }
        }
        Text(
            text = name.arabic,
            style = SanctumTheme.typography.displayLarge,
            color = SanctumTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name.transliteration,
            style = SanctumTheme.typography.titleMedium,
            color = SanctumTheme.colors.brand,
        )
        Text(
            text = name.meaning,
            style = SanctumTheme.typography.bodySmall,
            color = SanctumTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (name.audioFileName.isNotEmpty()) {
            IconButton(onClick = onPlay) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play audio",
                    tint = SanctumTheme.colors.brand,
                    modifier = Modifier.height(32.dp),
                )
            }
        }
    }
}

@Composable
fun NameDetailOverlay(
    name: DivineName,
    isFavorite: Boolean,
    isMemorized: Boolean,
    onClose: () -> Unit,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleMemorized: () -> Unit,
    onStopAudio: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SanctumTheme.colors.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = SanctumTheme.colors.textSecondary,
                    )
                }
                Row {
                    IconButton(onClick = onToggleMemorized) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Toggle memorized",
                            tint = if (isMemorized) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary.copy(alpha = 0.35f),
                        )
                    }
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = if (isFavorite) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary,
                        )
                    }
                }
            }
            Text(
                text = name.arabic,
                style = SanctumTheme.typography.displayLarge,
                color = SanctumTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = name.transliteration,
                style = SanctumTheme.typography.titleLarge,
                color = SanctumTheme.colors.brand,
            )
            Text(
                text = name.meaning,
                style = SanctumTheme.typography.bodyMedium,
                color = SanctumTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = name.explanation,
                style = SanctumTheme.typography.bodyMedium,
                color = SanctumTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (name.audioFileName.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    IconButton(
                        onClick = {
                            onPlay()
                            onStopAudio()
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SanctumTheme.colors.brand),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play audio",
                            tint = Color.White,
                        )
                    }
                    Text(
                        text = "Play recitation",
                        style = SanctumTheme.typography.labelSmall,
                        color = SanctumTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
