package com.sanctum.core.feature.scripture.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanctum.core.core.designsystem.components.SanctumBadge
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumPrimaryButton
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.MoodTag
import kotlinx.coroutines.launch

@Composable
fun InspirationScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InspirationViewModel = viewModel { InspirationViewModel() },
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentVerse = uiState.currentVerse

    // Shake animation state
    val shakeAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun shuffleVerse() {
        scope.launch {
            // Simple shake animation
            shakeAnim.animateTo(10f, tween(50))
            shakeAnim.animateTo(-10f, tween(50))
            shakeAnim.animateTo(10f, tween(50))
            shakeAnim.animateTo(-10f, tween(50))
            shakeAnim.animateTo(0f, tween(50))
        }
        viewModel.shuffleVerse()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Inspiration") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = SanctumTheme.colors.background,
                contentColor = SanctumTheme.colors.textPrimary,
                elevation = 0.dp,
            )
        },
        backgroundColor = SanctumTheme.colors.background,
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "How are you feeling today?",
                style = SanctumTheme.typography.titleMedium,
                color = SanctumTheme.colors.textPrimary,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Mood Tags
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(MoodTag.entries) { tag ->
                    SanctumBadge(
                        text = tag.name.lowercase().replaceFirstChar { it.uppercase() },
                        isSelected = uiState.selectedTag == tag,
                        onClick = {
                            viewModel.selectTag(tag)
                            // We don't trigger shake here normally, but could
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Verse Card
            SanctumCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer {
                        translationX = shakeAnim.value
                    },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (currentVerse != null) {
                        Text(
                            text = currentVerse.originalText,
                            style = SanctumTheme.typography.headlineLarge,
                            color = SanctumTheme.colors.textPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )

                        Text(
                            text = currentVerse.translation,
                            style = SanctumTheme.typography.bodyLarge,
                            color = SanctumTheme.colors.textSecondary,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Card Actions
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            IconButton(onClick = { /* TODO Read in Context */ }) {
                                Icon(Icons.Default.Info, contentDescription = "Read in Context")
                            }
                            IconButton(onClick = { /* TODO Bookmark */ }) {
                                Icon(Icons.Default.FavoriteBorder, contentDescription = "Bookmark")
                            }
                            IconButton(onClick = { /* TODO Share */ }) {
                                Icon(Icons.Default.Share, contentDescription = "Share as Image")
                            }
                        }
                    } else {
                        Text(
                            text = "Loading...",
                            style = SanctumTheme.typography.bodyLarge,
                            color = SanctumTheme.colors.textSecondary,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Shuffle Button
            SanctumPrimaryButton(
                onClick = { shuffleVerse() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Shuffle",
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text("Get Another Verse")
            }
        }
    }
}
