package com.sanctum.core.feature.scripture.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.data.dictionary.MockDictionaryRepository
import com.sanctum.core.feature.scripture.domain.ScriptureChapter
import com.sanctum.core.feature.scripture.domain.crossreference.CrossReference
import com.sanctum.core.feature.scripture.domain.dictionary.DictionaryTerm
import com.sanctum.core.feature.scripture.domain.history.HistoricalContext
import com.sanctum.core.feature.scripture.domain.memorization.MemorizationDifficulty
import com.sanctum.core.feature.scripture.domain.memorization.VerseMasker
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
private val alphabetRegex = Regex("[^a-z]")

@Composable
fun ScriptureReaderScreen(
    loadedChapters: List<ScriptureChapter>,
    allChapters: List<ScriptureChapter>,
    initialScrollIndex: Int = 0,
    initialScrollOffset: Int = 0,
    bookName: String = "",
    bookmarkedVerseIds: Set<String>,
    crossReferences: Map<String, List<CrossReference>> = emptyMap(),
    uiState: ScriptureUiState? = null,
    onBookmarkToggle: (String) -> Unit,
    onAssignTag: (String, Int) -> Unit = { _, _ -> },
    onUnassignTag: (String, Int) -> Unit = { _, _ -> },
    onCreateTag: (String, String) -> Unit = { _, _ -> },
    onReflectClick: (String, String) -> Unit = { _, _ -> },
    onNavigateToChapter: (String) -> Unit = {},
    onLoadNextChapter: (String) -> Unit = {},
    onSaveScrollPosition: (String, Int, Int) -> Unit = { _, _, _ -> },
) {
    val navigator = LocalNavigator.currentOrThrow
    var fontSizeMultiplier by remember { mutableStateOf(1.0f) }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var showTransliteration by remember { mutableStateOf(true) }
    var selectedHistoricalContext by remember { mutableStateOf<HistoricalContext?>(null) }
    var selectedDictionaryTerm by remember { mutableStateOf<DictionaryTerm?>(null) }
    val dictionaryRepository = remember { MockDictionaryRepository() }
    var selectedCrossReferences by remember { mutableStateOf<List<CrossReference>>(emptyList()) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var bookmarkActionVerseId by remember { mutableStateOf<String?>(null) }
    var selectedMemorizationDifficulty by remember { mutableStateOf(MemorizationDifficulty.LEVEL_0_READ) }

    val config = LocalWhiteLabelConfig.current

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialScrollIndex, initialFirstVisibleItemScrollOffset = initialScrollOffset)

    // Flat list of items to render
    val listItems = remember(loadedChapters) {
        val items = mutableListOf<ScriptureReaderItem>()
        loadedChapters.forEach { chapter ->
            items.add(ScriptureReaderItem.Header(chapter))
            chapter.verses.forEach { verse ->
                items.add(ScriptureReaderItem.Verse(chapter, verse))
            }
        }
        items
    }

    // Determine current active chapter from scroll position
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val activeChapter = remember(firstVisibleItemIndex, listItems) {
        if (listItems.isNotEmpty() && firstVisibleItemIndex < listItems.size) {
            when (val item = listItems[firstVisibleItemIndex]) {
                is ScriptureReaderItem.Header -> item.chapter
                is ScriptureReaderItem.Verse -> item.chapter
            }
        } else {
            loadedChapters.firstOrNull()
        }
    }

    // Pre-compute text sizes based on font multiplier
    val cachedTranslationTexts = remember(loadedChapters, fontSizeMultiplier) {
        mutableMapOf<String, String>() // Placeholder if we need actual AnnotatedStrings later
    }

    // Save scroll position
    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        activeChapter?.let {
            onSaveScrollPosition(it.id, listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }
    }

    // Load next chapter dynamically
    LaunchedEffect(listState.layoutInfo, loadedChapters) {
        val totalItems = listState.layoutInfo.totalItemsCount
        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        if (totalItems > 0 && lastVisibleItem >= totalItems - 5) {
            val lastLoadedChapter = loadedChapters.lastOrNull()
            if (lastLoadedChapter != null) {
                val currentIdx = allChapters.indexOfFirst { it.id == lastLoadedChapter.id }
                if (currentIdx != -1 && currentIdx < allChapters.size - 1) {
                    val nextChapterId = allChapters[currentIdx + 1].id
                    onLoadNextChapter(nextChapterId)
                }
            }
        }
    }

    val firstLoadedChapter = loadedChapters.firstOrNull()
    val currentIdx = firstLoadedChapter?.let { fc -> allChapters.indexOfFirst { it.id == fc.id } } ?: -1
    val previousChapter = if (currentIdx > 0) allChapters.getOrNull(currentIdx - 1) else null
    val lastLoadedChapter = loadedChapters.lastOrNull()
    val lastCurrentIdx = lastLoadedChapter?.let { lc -> allChapters.indexOfFirst { it.id == lc.id } } ?: -1
    val nextChapter = if (lastCurrentIdx != -1 && lastCurrentIdx < allChapters.size - 1) allChapters.getOrNull(lastCurrentIdx + 1) else null

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetBackgroundColor = SanctumTheme.colors.background,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = "CROSS REFERENCES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SanctumTheme.colors.brand,
                    letterSpacing = 2.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (selectedCrossReferences.isEmpty()) {
                    Spacer(modifier = Modifier.height(40.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(selectedCrossReferences) { ref ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch { sheetState.hide() }
                                        onNavigateToChapter(ref.targetVerseId.split(":").firstOrNull() ?: "")
                                    }
                                    .padding(vertical = 8.dp),
                            ) {
                                Text(
                                    text = "${ref.targetChapterName} ${ref.targetVerseNumber}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SanctumTheme.colors.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ref.previewText,
                                    fontSize = 13.sp,
                                    color = SanctumTheme.colors.textSecondary,
                                    maxLines = 3,
                                )
                            }
                        }
                    }
                }
            }
            if (config.hasMemorizationMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SanctumTheme.colors.surface)
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val difficulties = listOf(
                        MemorizationDifficulty.LEVEL_0_READ to "Read",
                        MemorizationDifficulty.LEVEL_1_FIRST_LETTER to "Hint",
                        MemorizationDifficulty.LEVEL_2_HALF_BLANK to "Test",
                        MemorizationDifficulty.LEVEL_3_FULL_BLANK to "Blind",
                    )
                    difficulties.forEach { (difficulty, label) ->
                        val isSelected = selectedMemorizationDifficulty == difficulty
                        Box(
                            modifier = Modifier
                                .clickable { selectedMemorizationDifficulty = difficulty }
                                .background(
                                    color = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.background,
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.outlineVariant,
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SanctumTheme.colors.surface else SanctumTheme.colors.textPrimary,
                            )
                        }
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SanctumTheme.colors.background),
        ) {
            // ─── Sticky Reading Toolbar ──────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SanctumTheme.colors.surface)
                    .border(width = 0.5.dp, color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = activeChapter?.title ?: activeChapter?.let { "Chapter ${it.number}" } ?: "Chapter",
                    style = SanctumTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SanctumTheme.colors.textPrimary,
                    letterSpacing = 1.sp,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Font Decrease
                    IconButton(
                        onClick = { fontSizeMultiplier = (fontSizeMultiplier - 0.1f).coerceIn(0.8f, 1.6f) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Text("-A", fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textSecondary, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${(fontSizeMultiplier * 100).toInt()}%",
                        fontSize = 11.sp,
                        color = SanctumTheme.colors.textSecondary,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Font Increase
                    IconButton(
                        onClick = { fontSizeMultiplier = (fontSizeMultiplier + 0.1f).coerceIn(0.8f, 1.6f) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Text("+A", fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textSecondary, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Audio Player Toggle
                    IconButton(
                        onClick = { isPlayingAudio = !isPlayingAudio },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Default.PlayArrow else Icons.Default.Notifications,
                            contentDescription = "Listen",
                            tint = if (isPlayingAudio) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    if (config.hasTransliteration) {
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { showTransliteration = !showTransliteration },
                            modifier = Modifier.size(32.dp),
                        ) {
                            Text(
                                text = "T",
                                fontWeight = FontWeight.Bold,
                                color = if (showTransliteration) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }

            // ─── Reading Area ────────────────────────────────────────────
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    top = 24.dp,
                    bottom = SanctumTheme.spacing.bottomNavPadding + 16.dp,
                    start = 20.dp,
                    end = 20.dp,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                items(
                    items = listItems,
                    key = { item ->
                        when (item) {
                            is ScriptureReaderItem.Header -> "header_${item.chapter.id}"
                            is ScriptureReaderItem.Verse -> "verse_${item.verse.id}"
                        }
                    },
                    contentType = { item ->
                        when (item) {
                            is ScriptureReaderItem.Header -> "header"
                            is ScriptureReaderItem.Verse -> "verse"
                        }
                    },
                ) { item ->
                    when (item) {
                        is ScriptureReaderItem.Header -> {
                            // Cinematic Hero Backdrop
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .aspectRatio(3f)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                SanctumTheme.colors.brand.copy(alpha = 0.12f),
                                                SanctumTheme.colors.brand.copy(alpha = 0.02f),
                                            ),
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                    )
                                    .border(
                                        width = 0.5.dp,
                                        color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(16.dp),
                                    )
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "SCRIPTURE READER",
                                        fontSize = 11.sp,
                                        color = SanctumTheme.colors.brand,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 3.sp,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.chapter.title?.uppercase() ?: "CHAPTER ${item.chapter.number}",
                                        fontSize = 28.sp,
                                        color = SanctumTheme.colors.textPrimary,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                        letterSpacing = 2.sp,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(28.dp))
                        }
                        is ScriptureReaderItem.Verse -> {
                            val verse = item.verse
                            val chapter = item.chapter
                            val isBookmarked = bookmarkedVerseIds.contains(verse.id)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                // Left border line highlighting bookmark status
                                val lineColor = if (isBookmarked) {
                                    SanctumTheme.colors.brand
                                } else {
                                    SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f)
                                }
                                val lineWidth = if (isBookmarked) 3.dp else 1.dp

                                Box(
                                    modifier = Modifier
                                        .width(lineWidth)
                                        .fillMaxHeight()
                                        .background(lineColor),
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        // Superscript verse number
                                        Text(
                                            text = "${verse.number}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SanctumTheme.colors.brand,
                                            letterSpacing = 1.sp,
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (verse.historicalContext != null) {
                                                IconButton(
                                                    onClick = { selectedHistoricalContext = verse.historicalContext },
                                                    modifier = Modifier.size(28.dp),
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = "Historical Context",
                                                        tint = SanctumTheme.colors.brand,
                                                        modifier = Modifier.size(16.dp),
                                                    )
                                                }
                                            }

                                            val shareController = remember { com.sanctum.core.feature.share.domain.ShareController() }
                                            if (shareController.isShareSupported()) {
                                                // Share button
                                                IconButton(
                                                    onClick = {
                                                        navigator.push(com.sanctum.core.feature.share.presentation.ShareVerseScreen(verse, bookName, chapter.number))
                                                    },
                                                    modifier = Modifier.size(28.dp),
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Share,
                                                        contentDescription = "Share",
                                                        tint = SanctumTheme.colors.textSecondary.copy(alpha = 0.35f),
                                                        modifier = Modifier.size(16.dp),
                                                    )
                                                }
                                            }
                                            // Heart bookmark toggle button
                                            IconButton(
                                                onClick = { onBookmarkToggle(verse.id) },
                                                modifier = Modifier.size(28.dp),
                                            ) {
                                                Icon(
                                                    imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                    contentDescription = "Bookmark",
                                                    tint = if (isBookmarked) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary.copy(alpha = 0.35f),
                                                    modifier = Modifier.size(16.dp),
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (verse.originalText.isNotEmpty() && verse.originalText != verse.translation) {
                                        Text(
                                            text = verse.originalText,
                                            fontSize = (24 * fontSizeMultiplier).sp,
                                            color = SanctumTheme.colors.textPrimary,
                                            lineHeight = (36 * fontSizeMultiplier).sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = SanctumTheme.typography.amiri,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    if (selectedMemorizationDifficulty == MemorizationDifficulty.LEVEL_0_READ) {
                                        Text(
                                            text = verse.translation,
                                            fontSize = (18 * fontSizeMultiplier).sp,
                                            color = SanctumTheme.colors.textPrimary,
                                            lineHeight = (28 * fontSizeMultiplier).sp,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                        )
                                    } else {
                                        val maskedWords = remember(verse.translation, selectedMemorizationDifficulty) {
                                            VerseMasker.maskVerse(verse.translation, selectedMemorizationDifficulty)
                                        }
                                        val revealedWords = remember(verse.id, selectedMemorizationDifficulty) { mutableStateMapOf<Int, Boolean>() }

                                        // Use FlowRow or wrap logic using layout. But standard Compose doesn't have FlowRow by default without Accompanist
                                        // or modern Compose flow layout. We can use ExperimentalLayoutApi FlowRow.
                                        @OptIn(ExperimentalLayoutApi::class)
                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            maskedWords.forEachIndexed { index, word ->
                                                val isRevealed = revealedWords[index] ?: false
                                                val textToDisplay = if (isRevealed || word.isPunctuation) word.originalWord else word.maskedWord
                                                Text(
                                                    text = textToDisplay,
                                                    fontSize = (18 * fontSizeMultiplier).sp,
                                                    color = if (!isRevealed && !word.isPunctuation) SanctumTheme.colors.brand else SanctumTheme.colors.textPrimary,
                                                    lineHeight = (28 * fontSizeMultiplier).sp,
                                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                                    modifier = Modifier.clickable {
                                                        if (!word.isPunctuation) {
                                                            revealedWords[index] = !isRevealed
                                                        }
                                                    },
                                                )
                                            }
                                        }
                                    }

                                    if (showTransliteration && verse.transliteration != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = verse.transliteration,
                                            fontSize = (14 * fontSizeMultiplier).sp,
                                            color = SanctumTheme.colors.brand.copy(alpha = 0.7f),
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            lineHeight = (20 * fontSizeMultiplier).sp,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Chapter Navigation Footer
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 40.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        if (previousChapter != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToChapter(previousChapter.id) },
                            ) {
                                SanctumCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(16.dp),
                                ) {
                                    Column {
                                        Text(
                                            text = "PREVIOUS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SanctumTheme.colors.brand,
                                            letterSpacing = 1.sp,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = previousChapter.title ?: "Chapter ${previousChapter.number}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = SanctumTheme.colors.textPrimary,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        if (nextChapter != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToChapter(nextChapter.id) },
                            ) {
                                SanctumCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(16.dp),
                                ) {
                                    Column {
                                        Text(
                                            text = "NEXT",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SanctumTheme.colors.brand,
                                            letterSpacing = 1.sp,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = nextChapter.title ?: "Chapter ${nextChapter.number}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = SanctumTheme.colors.textPrimary,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        selectedDictionaryTerm?.let { term ->
            Dialog(onDismissRequest = { selectedDictionaryTerm = null }) {
                SanctumCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentPadding = PaddingValues(24.dp),
                ) {
                    Column {
                        Text(
                            text = "DICTIONARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SanctumTheme.colors.brand,
                            letterSpacing = 2.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = term.word,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = SanctumTheme.colors.textPrimary,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        )

                        if (term.transliteration != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = term.transliteration,
                                fontSize = 14.sp,
                                color = SanctumTheme.colors.brand.copy(alpha = 0.8f),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = term.definition,
                            fontSize = 16.sp,
                            color = SanctumTheme.colors.textPrimary,
                            lineHeight = 24.sp,
                        )

                        if (term.root != null || term.etymology != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                if (term.root != null) {
                                    Column {
                                        Text(
                                            text = "ROOT",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SanctumTheme.colors.textSecondary,
                                        )
                                        Text(
                                            text = term.root,
                                            fontSize = 14.sp,
                                            color = SanctumTheme.colors.textPrimary,
                                        )
                                    }
                                }
                                if (term.etymology != null) {
                                    Column {
                                        Text(
                                            text = "ORIGIN",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SanctumTheme.colors.textSecondary,
                                        )
                                        Text(
                                            text = term.etymology,
                                            fontSize = 14.sp,
                                            color = SanctumTheme.colors.textPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedHistoricalContext?.let { context ->
            Dialog(onDismissRequest = { selectedHistoricalContext = null }) {
                SanctumCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentPadding = PaddingValues(24.dp),
                ) {
                    Column {
                        Text(
                            text = "HISTORICAL CONTEXT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SanctumTheme.colors.brand,
                            letterSpacing = 2.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (context.timelineDate != null) {
                            Text(
                                text = "Timeline: ${context.timelineDate}",
                                fontSize = 14.sp,
                                color = SanctumTheme.colors.textPrimary,
                                fontWeight = FontWeight.Medium,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (context.location != null) {
                            Text(
                                text = "Location: ${context.location.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SanctumTheme.colors.textPrimary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = context.location.description,
                                fontSize = 14.sp,
                                color = SanctumTheme.colors.textSecondary,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (context.figures.isNotEmpty()) {
                            Text(
                                text = "Notable Figures:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SanctumTheme.colors.textPrimary,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            context.figures.forEach { figure ->
                                Text(
                                    text = figure.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SanctumTheme.colors.textPrimary,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = figure.summary,
                                    fontSize = 13.sp,
                                    color = SanctumTheme.colors.textSecondary,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
