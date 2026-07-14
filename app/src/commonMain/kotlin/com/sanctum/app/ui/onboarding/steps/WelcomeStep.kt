package com.sanctum.app.ui.onboarding.steps

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.app.BuildConfig
import com.sanctum.core.core.designsystem.theme.SanctumTheme

@Composable
fun WelcomeStep() {
    var startAnimation by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SanctumTheme.colors.background,
                        SanctumTheme.colors.brand.copy(alpha = 0.1f),
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 120.dp)
                .alpha(alpha),
        ) {
            Text(
                text = BuildConfig.APP_NAME.uppercase(),
                style = SanctumTheme.typography.displayMedium,
                color = SanctumTheme.colors.brand,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = BuildConfig.COPY_WELCOME_MESSAGE,
                style = SanctumTheme.typography.headlineMedium,
                color = SanctumTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
            )
        }
    }
}
