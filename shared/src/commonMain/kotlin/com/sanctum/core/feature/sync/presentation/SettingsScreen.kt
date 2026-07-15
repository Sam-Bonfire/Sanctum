package com.sanctum.core.feature.sync.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumOutlinedButton
import com.sanctum.core.core.designsystem.components.SanctumPrimaryButton
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
import com.sanctum.core.core.designsystem.theme.LocalIsDarkTheme
import com.sanctum.core.core.designsystem.theme.LocalThemeToggle
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun SettingsScreen(
    syncState: SyncState,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SanctumTheme.colors.background),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // ─── Profile Avatar & Header ──────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(SanctumTheme.colors.brand.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Avatar",
                    tint = SanctumTheme.colors.brand,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Profile",
                style = SanctumTheme.typography.displayMedium,
                color = SanctumTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Personalize your devotion and cloud backups.",
                style = SanctumTheme.typography.bodyMedium,
                color = SanctumTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ─── Preferences Section Header ──────────
            SanctumSectionHeader(
                text = "PREFERENCES",
                modifier = Modifier.align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Theme Switcher Card
            SanctumCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "Dark Mode",
                            style = SanctumTheme.typography.titleMedium,
                            color = SanctumTheme.colors.textPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Switch between light and dark themes.",
                            style = SanctumTheme.typography.bodySmall,
                            color = SanctumTheme.colors.textSecondary,
                        )
                    }

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

            Spacer(modifier = Modifier.height(32.dp))

            // ─── Sync Section Header ──────────
            SanctumSectionHeader(
                text = "DATA MANAGEMENT",
                modifier = Modifier.align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(12.dp))

            SanctumCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(24.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Bring Your Own Cloud",
                        style = SanctumTheme.typography.titleMedium,
                        color = SanctumTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Backup your data to your native cloud provider. No central servers required.",
                        style = SanctumTheme.typography.bodyMedium,
                        color = SanctumTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (syncState is SyncState.Syncing) {
                        CircularProgressIndicator(color = SanctumTheme.colors.brand)
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            SanctumPrimaryButton(
                                onClick = onBackupClick,
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                            ) {
                                Text(text = "Backup Now", fontSize = 12.sp)
                            }

                            SanctumOutlinedButton(
                                onClick = onRestoreClick,
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                            ) {
                                Text(text = "Restore Data", fontSize = 12.sp)
                            }
                        }
                    }

                    if (syncState is SyncState.Success || syncState is SyncState.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        when (syncState) {
                            is SyncState.Success -> Text(
                                text = syncState.message,
                                color = SanctumTheme.colors.brand,
                                style = SanctumTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            is SyncState.Error -> Text(
                                text = syncState.message,
                                color = SanctumTheme.colors.error,
                                style = SanctumTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                            )
                            else -> {}
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.bottomNavPadding))
        }
    }
}
