package com.sanctum.core.feature.sync.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumOutlinedButton
import com.sanctum.core.core.designsystem.components.SanctumPrimaryButton
import com.sanctum.core.core.designsystem.theme.LocalIsDarkTheme
import com.sanctum.core.core.designsystem.theme.LocalThemeToggle
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun SettingsScreen(
    syncState: SyncState,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "SETTINGS",
            style = SanctumTheme.typography.labelMedium,
            color = SanctumTheme.colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Theme Switcher Card
        SanctumCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Dark Mode",
                    style = SanctumTheme.typography.titleMedium,
                    color = SanctumTheme.colors.textPrimary,
                )
                val isDark = LocalIsDarkTheme.current
                val toggleTheme = LocalThemeToggle.current
                Switch(
                    checked = isDark,
                    onCheckedChange = { toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SanctumTheme.colors.brand,
                        checkedTrackColor = SanctumTheme.colors.brand.copy(alpha = 0.5f),
                        uncheckedThumbColor = SanctumTheme.colors.textSecondary,
                        uncheckedTrackColor = SanctumTheme.colors.textSecondary.copy(alpha = 0.5f),
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SanctumCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(32.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Bring Your Own Cloud",
                    style = SanctumTheme.typography.displayMedium,
                    color = SanctumTheme.colors.textPrimary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Backup your data to your native cloud provider. No central servers required.",
                    style = SanctumTheme.typography.bodyLarge,
                    color = SanctumTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (syncState is SyncState.Syncing) {
                    CircularProgressIndicator(color = SanctumTheme.colors.brand)
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SanctumPrimaryButton(onClick = onBackupClick) {
                            Text(text = "Backup Now")
                        }

                        SanctumOutlinedButton(onClick = onRestoreClick) {
                            Text(text = "Restore Data")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (syncState) {
                    is SyncState.Success -> Text(text = syncState.message, color = SanctumTheme.colors.brand, textAlign = TextAlign.Center)
                    is SyncState.Error -> Text(text = syncState.message, color = SanctumTheme.colors.error, textAlign = TextAlign.Center)
                    else -> {}
                }
            }
        }
    }
}
