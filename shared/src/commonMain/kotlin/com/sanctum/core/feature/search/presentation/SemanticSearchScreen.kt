package com.sanctum.core.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumTextField
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.Verse
import com.sanctum.core.feature.search.data.LocalSemanticSearchIndexer
import com.sanctum.core.feature.search.domain.SemanticSearchEngine
import com.sanctum.core.feature.search.domain.SemanticSearchResult

class SemanticSearchScreen : Screen {

    @Composable
    override fun Content() {
        // State for search query
        var query by remember { mutableStateOf("") }
        var results by remember { mutableStateOf<List<SemanticSearchResult>>(emptyList()) }

        // Mocks setup
        val searchEngine = remember { SemanticSearchEngine() }
        val indexer = remember { LocalSemanticSearchIndexer() }
        val corpus = remember {
            indexer.buildCorpus(
                listOf(
                    Verse(1, 1, 1, "ٱلْحَمْدُ لِلَّهِ", "All praise and gratitude is due."),
                    Verse(2, 2, 155, "وَلَنَبْلُوَنَّكُم", "And We will surely test you... give good tidings to the patient."),
                    Verse(3, 94, 5, "فَإِنَّ مَعَ ٱلْعُسْرِ", "For indeed, with hardship [will be] ease. Comfort for the sorrowful."),
                ),
            )
        }

        // Search trigger effect
        LaunchedEffect(query) {
            if (query.isNotBlank()) {
                val vector = indexer.embedQuery(query)
                results = searchEngine.search(vector, corpus).filter { it.score > 0.1f }
            } else {
                results = emptyList()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SanctumTheme.colors.background)
                .padding(16.dp),
        ) {
            // Header
            Text(
                text = "Semantic Search",
                style = SanctumTheme.typography.displayMedium,
                color = SanctumTheme.colors.textPrimary,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Search Input
            SanctumTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search by concept (e.g. comfort, patience)",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Results List
            if (query.isNotBlank() && results.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No strong semantic matches found.",
                        color = SanctumTheme.colors.textSecondary,
                        style = SanctumTheme.typography.bodyLarge,
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = SanctumTheme.spacing.bottomNavPadding),
                ) {
                    items(results) { result ->
                        SemanticResultCard(result)
                    }
                }
            }
        }
    }

    @Composable
    private fun SemanticResultCard(result: SemanticSearchResult) {
        SanctumCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Surah ${result.verse.surahId} : Ayah ${result.verse.ayahNumber}",
                        style = SanctumTheme.typography.labelMedium,
                        color = SanctumTheme.colors.brand,
                        fontWeight = FontWeight.Bold,
                    )

                    // Relevance Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SanctumTheme.colors.brand.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        val percentage = (result.score * 100).toInt()
                        Text(
                            text = "$percentage% Match",
                            color = SanctumTheme.colors.brand,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = result.verse.arabicText,
                    style = SanctumTheme.typography.titleLarge,
                    color = SanctumTheme.colors.textPrimary,
                    modifier = Modifier.align(Alignment.End),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = result.verse.translatedText,
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                )
            }
        }
    }
}
