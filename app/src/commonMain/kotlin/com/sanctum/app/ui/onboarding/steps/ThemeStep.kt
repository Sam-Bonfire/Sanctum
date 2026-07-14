package com.sanctum.app.ui.onboarding.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun ThemeStep(selectedTheme: MutableState<String>, onThemeSelect: (String) -> Unit) {
    val themes = listOf(
        "system" to "System Default",
        "light" to "Light Mode",
        "dark" to "Dark Mode",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
    ) {
        Text(
            text = "Choose Your Theme",
            style = SanctumTheme.typography.headlineLarge,
            color = SanctumTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Customize the appearance of the app to your liking. You can change this later in Settings.",
            style = SanctumTheme.typography.bodyLarge,
            color = SanctumTheme.colors.textSecondary,
            lineHeight = 24.sp,
        )

        Spacer(modifier = Modifier.height(48.dp))

        themes.forEach { (id, title) ->
            val isSelected = selectedTheme.value == id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) SanctumTheme.colors.brand.copy(alpha = 0.1f) else SanctumTheme.colors.surface)
                    .clickable { onThemeSelect(id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = SanctumTheme.typography.bodyLarge,
                    color = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.textPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f),
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = SanctumTheme.colors.brand,
                    )
                }
            }
        }
    }
}
