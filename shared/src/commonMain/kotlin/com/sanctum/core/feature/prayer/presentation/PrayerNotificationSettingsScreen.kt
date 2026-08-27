package com.sanctum.core.feature.prayer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.prayer.domain.MuezzinVoice
import com.sanctum.core.feature.prayer.domain.NotificationAlertType

@Composable
fun PrayerNotificationSettingsScreen(
    viewModel: PrayerNotificationViewModel,
    prayers: List<String>? = null,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(prayers) {
        if (prayers != null) {
            viewModel.loadSettings(prayers)
        } else {
            viewModel.loadSettings()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp),
        ) {
            item {
                Text(
                    text = "Notification Settings",
                    style = SanctumTheme.typography.displayMedium,
                    color = SanctumTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = "Customize alerts and Muezzin voices for each prayer.",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
            }

            items(uiState.prayerSettings) { setting ->
                PrayerSettingCard(
                    prayerName = setting.prayerName,
                    alertType = setting.alertType,
                    muezzinVoice = setting.muezzinVoice,
                    playingVoice = uiState.playingVoice,
                    onAlertTypeChanged = { viewModel.updateAlertType(setting.prayerName, it) },
                    onVoiceChanged = { viewModel.updateMuezzinVoice(setting.prayerName, it) },
                    onTogglePreview = { viewModel.toggleAudioPreview(it) },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PrayerSettingCard(
    prayerName: String,
    alertType: NotificationAlertType,
    muezzinVoice: MuezzinVoice,
    playingVoice: MuezzinVoice?,
    onAlertTypeChanged: (NotificationAlertType) -> Unit,
    onVoiceChanged: (MuezzinVoice) -> Unit,
    onTogglePreview: (MuezzinVoice) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    SanctumCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = prayerName,
                    style = SanctumTheme.typography.titleMedium,
                    color = SanctumTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = if (alertType == NotificationAlertType.AUDIO) muezzinVoice.displayName else alertType.name,
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.brand,
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // Alert Type Selection
                Text(
                    text = "Alert Type",
                    style = SanctumTheme.typography.labelMedium,
                    color = SanctumTheme.colors.textSecondary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    NotificationAlertType.entries.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = type == alertType,
                                onClick = { onAlertTypeChanged(type) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = SanctumTheme.colors.brand,
                                    unselectedColor = SanctumTheme.colors.textSecondary,
                                ),
                            )
                            Text(
                                text = type.name,
                                style = SanctumTheme.typography.bodySmall,
                                color = SanctumTheme.colors.textPrimary,
                            )
                        }
                    }
                }

                // Voice Selection (only if Audio is selected)
                if (alertType == NotificationAlertType.AUDIO) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Muezzin Voice",
                        style = SanctumTheme.typography.labelMedium,
                        color = SanctumTheme.colors.textSecondary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SanctumTheme.colors.surface),
                    ) {
                        MuezzinVoice.entries.forEach { voice ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onVoiceChanged(voice) }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = voice == muezzinVoice,
                                        onClick = { onVoiceChanged(voice) },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = SanctumTheme.colors.brand,
                                            unselectedColor = SanctumTheme.colors.textSecondary,
                                        ),
                                    )
                                    Text(
                                        text = voice.displayName,
                                        style = SanctumTheme.typography.bodyMedium,
                                        color = SanctumTheme.colors.textPrimary,
                                    )
                                }
                                IconButton(onClick = { onTogglePreview(voice) }) {
                                    Icon(
                                        imageVector = if (playingVoice == voice) Icons.Default.Close else Icons.Default.PlayArrow,
                                        contentDescription = "Preview Audio",
                                        tint = SanctumTheme.colors.brand,
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
