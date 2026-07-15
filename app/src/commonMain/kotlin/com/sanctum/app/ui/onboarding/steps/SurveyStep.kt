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
import com.sanctum.app.BuildConfig
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun SurveyStep(selectedIntent: MutableState<String>, onIntentSelect: (String) -> Unit) {
    val intents = listOf(
        "read" to "Read \${BuildConfig.TERM_SCRIPTURE_TITLE}",
        "pray" to BuildConfig.TERM_DAILY_DEVOTION,
        "learn" to "Learn more about the faith",
        "other" to "Other",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
    ) {
        Text(
            text = "What brings you here?",
            style = SanctumTheme.typography.headlineLarge,
            color = SanctumTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "We will personalize your experience based on your goals.",
            style = SanctumTheme.typography.bodyLarge,
            color = SanctumTheme.colors.textSecondary,
            lineHeight = 24.sp,
        )

        Spacer(modifier = Modifier.height(48.dp))

        intents.forEach { (id, title) ->
            val isSelected = selectedIntent.value == id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) SanctumTheme.colors.brand.copy(alpha = 0.1f) else SanctumTheme.colors.surface)
                    .clickable { onIntentSelect(id) }
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
