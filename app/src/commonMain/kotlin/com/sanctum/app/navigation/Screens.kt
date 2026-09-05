package com.sanctum.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.compass.domain.PlatformSensors
import com.sanctum.core.feature.compass.presentation.QiblaCompassScreen
import com.sanctum.core.feature.duas.presentation.DuasCatalogScreen
import com.sanctum.core.feature.journal.presentation.JournalDetailScreen
import com.sanctum.core.feature.journal.presentation.JournalScreen
import com.sanctum.core.feature.journal.presentation.JournalViewModel
import com.sanctum.core.feature.scripture.presentation.DashboardScreen
import com.sanctum.core.feature.scripture.presentation.DashboardViewModel
import com.sanctum.core.feature.scripture.presentation.ScriptureIndexScreen
import com.sanctum.core.feature.scripture.presentation.ScriptureReaderScreen
import com.sanctum.core.feature.scripture.presentation.ScriptureViewModel
import com.sanctum.core.feature.sync.presentation.SettingsScreen
import com.sanctum.core.feature.sync.presentation.SyncViewModel
import org.koin.compose.koinInject

class CharityTrackerScreenNode : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<com.sanctum.core.feature.charity.presentation.CharityTrackerViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        com.sanctum.core.feature.charity.presentation.CharityTrackerScreen(
            uiState = uiState,
            onAddRecord = { amount, category, notes -> viewModel.addRecord(amount, category, notes) },
            onEditRecord = { id, amount, category, notes, dateIso -> viewModel.updateRecord(id, amount, category, notes, dateIso) },
            onSetGoal = { goal -> viewModel.setGoal(goal) },
            onDeleteRecord = { id -> viewModel.deleteRecord(id) },
        )
    }
}

class DashboardScreenNode : Screen {
    @Composable
    override fun Content() {
        val dashboardViewModel = koinInject<DashboardViewModel>()
        val uiState by dashboardViewModel.uiState.collectAsState()
        DashboardScreen(uiState = uiState)
    }
}

class QiblaCompassScreenNode : Screen {
    @Composable
    override fun Content() {
        val sensors = koinInject<PlatformSensors>()
        QiblaCompassScreen(
            sensors = sensors,
            onManualLocationClick = {},
        )
    }
}

class JournalScreenNode : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<JournalViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow

        androidx.compose.runtime.LaunchedEffect(Unit) {
            viewModel.loadAllEntries()
        }

        JournalScreen(
            uiState = uiState,
            onEntryClick = { entryId ->
                navigator.push(JournalDetailScreenNode(entryId, null, null))
            },
            onCreateNewEntry = {
                navigator.push(JournalDetailScreenNode(null, null, null))
            },
        )
    }
}

class JournalDetailScreenNode(
    private val entryId: Int?,
    private val verseId: Int?,
    private val chapterId: Int?,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<JournalViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow

        androidx.compose.runtime.LaunchedEffect(entryId, verseId, chapterId) {
            if (entryId != null) {
                viewModel.loadEntry(entryId)
            } else {
                viewModel.prepareNewEntry(verseId, chapterId)
            }
        }

        JournalDetailScreen(
            entry = uiState.currentEntry,
            onSave = { title, content ->
                viewModel.saveEntry(title, content)
            },
            onDelete = {
                if (entryId != null) viewModel.deleteEntry(entryId)
            },
            onBack = { navigator.pop() },
        )
    }
}

class ScriptureIndexScreenNode : Screen {
    @Composable
    override fun Content() {
        val scriptureViewModel = koinInject<ScriptureViewModel>()
        val uiState by scriptureViewModel.uiState.collectAsState()
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow

        ScriptureIndexScreen(
            uiState = uiState,
            onChapterClick = { chapterId ->
                navigator.push(ScriptureReaderScreenNode(chapterId))
            },
        )
    }
}

class ScriptureReaderScreenNode(private val chapterId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow
        val scriptureViewModel = koinInject<ScriptureViewModel>()
        val uiState by scriptureViewModel.uiState.collectAsState()

        androidx.compose.runtime.LaunchedEffect(chapterId) {
            scriptureViewModel.loadChapter(chapterId)
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SanctumTheme.colors.brand)
            }
        } else {
            if (uiState.loadedChapters.isNotEmpty()) {
                ScriptureReaderScreen(
                    loadedChapters = uiState.loadedChapters,
                    allChapters = uiState.chapters,
                    initialScrollIndex = uiState.scrollIndex,
                    initialScrollOffset = uiState.scrollOffset,
                    bookmarkedVerseIds = uiState.bookmarkedVerseIds,
                    crossReferences = uiState.crossReferences,
                    uiState = uiState,
                    onBookmarkToggle = { scriptureViewModel.toggleBookmark(it) },
                    onAssignTag = { verseId, tagId -> scriptureViewModel.assignTag(verseId, tagId) },
                    onUnassignTag = { verseId, tagId -> scriptureViewModel.unassignTag(verseId, tagId) },
                    onCreateTag = { name, color -> scriptureViewModel.createTag(name, color) },
                    onReflectClick = { verseId, chapterId ->
                        navigator.push(JournalDetailScreenNode(null, verseId.toIntOrNull(), chapterId.toIntOrNull()))
                    },
                    onNavigateToChapter = { nextId ->
                        navigator.replace(ScriptureReaderScreenNode(nextId))
                    },
                    onLoadNextChapter = { nextId ->
                        scriptureViewModel.loadNextChapter(nextId)
                    },
                    onSaveScrollPosition = { id, index, offset ->
                        scriptureViewModel.saveScrollPosition(id, index, offset)
                    },
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No Scripture Loaded", color = SanctumTheme.colors.textPrimary)
                }
            }
        }
    }
}

class DuasCatalogScreenNode : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<com.sanctum.core.feature.duas.presentation.DuasCatalogViewModel>()
        DuasCatalogScreen(viewModel)
    }
}

class BookmarksScreenNode : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<com.sanctum.core.feature.scripture.presentation.BookmarkViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow

        com.sanctum.core.feature.scripture.presentation.BookmarksScreen(
            bookmarks = uiState.bookmarks,
            availableTags = uiState.availableTags,
            selectedTagId = uiState.selectedTagId,
            onTagSelected = { viewModel.selectTag(it) },
            onDeleteTag = { viewModel.deleteTag(it) },
            onCreateTag = { name, color -> viewModel.createTag(name, color) },
            onRenameTag = { id, name -> viewModel.renameTag(id, name) },
            onVerseClick = {
                // Future enhancement: could navigate to the specific chapter/verse
            },
        )
    }
}

class SettingsScreenNode : Screen {
    @Composable
    override fun Content() {
        val syncViewModel = koinInject<SyncViewModel>()
        val syncState by syncViewModel.syncState.collectAsState()
        val cloudProvider by syncViewModel.cloudProvider.collectAsState()
        val isAutoBackupEnabled by syncViewModel.isAutoBackupEnabled.collectAsState()

        val navigator = cafe.adriel.voyager.navigator.LocalNavigator.currentOrThrow
        SettingsScreen(
            syncState = syncState,
            cloudProvider = cloudProvider,
            isAutoBackupEnabled = isAutoBackupEnabled,
            onBackupClick = { syncViewModel.backupNow() },
            onRestoreClick = { syncViewModel.restoreNow() },
            onProviderChange = { syncViewModel.setProvider(it) },
            onAutoBackupToggle = { syncViewModel.toggleAutoBackup(it) },
            onBookmarksClick = { navigator.push(BookmarksScreenNode()) },
        )
    }
}
