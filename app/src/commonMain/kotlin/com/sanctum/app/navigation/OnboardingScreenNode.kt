package com.sanctum.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.russhwolf.settings.Settings
import com.sanctum.app.ui.onboarding.steps.LocationStep
import com.sanctum.app.ui.onboarding.steps.PaywallStep
import com.sanctum.app.ui.onboarding.steps.SurveyStep
import com.sanctum.app.ui.onboarding.steps.ThemeStep
import com.sanctum.app.ui.onboarding.steps.WelcomeStep
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class OnboardingScreenNode : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val settings = koinInject<Settings>()
        val coroutineScope = rememberCoroutineScope()

        val pagerState = rememberPagerState(pageCount = { 5 })

        val themePref = remember { mutableStateOf(settings.getString("theme_pref", "system")) }
        val intentPref = remember { mutableStateOf(settings.getString("intent_pref", "")) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SanctumTheme.colors.background),
        ) {
            // Force using buttons
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    0 -> WelcomeStep()
                    1 -> LocationStep()
                    2 -> ThemeStep(themePref) {
                        themePref.value = it
                        com.sanctum.app.ThemeController.themePref.value = it
                        settings.putString("theme_pref", it)
                    }
                    3 -> SurveyStep(intentPref) {
                        intentPref.value = it
                        settings.putString("intent_pref", it)
                    }
                    4 -> PaywallStep()
                }
            }

            // Bottom Bar
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                // Pager Indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(5) { iteration ->
                        val color = if (pagerState.currentPage == iteration) SanctumTheme.colors.brand else SanctumTheme.colors.brand.copy(alpha = 0.3f)
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(if (pagerState.currentPage == iteration) 10.dp else 8.dp),
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < 4) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            settings.putBoolean("has_onboarded", true)
                            settings.putString("religion_id", com.sanctum.app.BuildConfig.FLAVOR_ID)
                            navigator.replaceAll(DashboardScreenNode())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = SanctumTheme.colors.brand,
                        contentColor = Color.White,
                    ),
                    elevation = ButtonDefaults.elevation(0.dp),
                ) {
                    Text(
                        text = if (pagerState.currentPage == 4) "Start Journey" else "Continue",
                        style = SanctumTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                }

                if (pagerState.currentPage == 4) {
                    TextButton(
                        onClick = {
                            settings.putBoolean("has_onboarded", true)
                            navigator.replaceAll(DashboardScreenNode())
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        Text("Restore Purchases", color = SanctumTheme.colors.textSecondary)
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}
