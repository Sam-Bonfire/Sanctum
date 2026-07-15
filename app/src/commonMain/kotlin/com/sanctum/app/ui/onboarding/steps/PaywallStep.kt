package com.sanctum.app.ui.onboarding.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun PaywallStep() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Unlock Premium",
            style = SanctumTheme.typography.headlineLarge,
            color = SanctumTheme.colors.brand,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Get access to offline downloads, advanced search, and ad-free experience.",
            style = SanctumTheme.typography.bodyLarge,
            color = SanctumTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Subscription Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SanctumTheme.colors.brand.copy(alpha = 0.05f))
                .padding(24.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Yearly Plan",
                    style = SanctumTheme.typography.bodyLarge,
                    color = SanctumTheme.colors.brand,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$29.99 / year",
                    style = SanctumTheme.typography.displayMedium,
                    color = SanctumTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "That's only $2.49 a month!",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mock free tier
        Text(
            text = "Or continue with the limited free version.",
            style = SanctumTheme.typography.bodyMedium,
            color = SanctumTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
