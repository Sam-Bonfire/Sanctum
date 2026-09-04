package com.sanctum.core.feature.zakat.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
import com.sanctum.core.core.designsystem.components.SanctumTextField
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.zakat.domain.NisabStandard
import org.koin.compose.koinInject

class ZakatCalculatorScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<ZakatCalculatorViewModel>()
        val state by viewModel.state.collectAsState(ZakatCalculatorState())

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 40.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                Text(
                    text = "Zakat Calculator",
                    style = SanctumTheme.typography.headlineLarge,
                    color = SanctumTheme.colors.textPrimary,
                )
            }

            item {
                SanctumSectionHeader("Summary")
                Spacer(modifier = Modifier.height(16.dp))
                SanctumCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Wealth:", style = SanctumTheme.typography.bodyLarge, color = SanctumTheme.colors.textPrimary)
                            Text("${state.currency} ${formatDouble(state.result.totalWealth)}", style = SanctumTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Nisab Threshold:", style = SanctumTheme.typography.bodyLarge, color = SanctumTheme.colors.textPrimary)
                            Text("${state.currency} ${formatDouble(state.result.nisabValue)}", style = SanctumTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Eligible for Zakat:", style = SanctumTheme.typography.bodyLarge, color = SanctumTheme.colors.textPrimary)
                            val eligibleText = if (state.result.isEligible) "Yes" else "No"
                            val eligibleColor = if (state.result.isEligible) SanctumTheme.colors.brand else SanctumTheme.colors.textSecondary
                            Text(eligibleText, style = SanctumTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = eligibleColor)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Zakat Payable:", style = SanctumTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                            Text("${state.currency} ${formatDouble(state.result.zakatPayable)}", style = SanctumTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SanctumTheme.colors.brand)
                        }
                    }
                }
            }

            item {
                SanctumSectionHeader("Nisab Standard")
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    NisabStandardOption(
                        title = "Gold (85g)",
                        isSelected = state.portfolio.selectedNisabStandard == NisabStandard.GOLD,
                        onClick = { viewModel.updateNisabStandard(NisabStandard.GOLD) },
                        modifier = Modifier.weight(1f),
                    )
                    NisabStandardOption(
                        title = "Silver (595g)",
                        isSelected = state.portfolio.selectedNisabStandard == NisabStandard.SILVER,
                        onClick = { viewModel.updateNisabStandard(NisabStandard.SILVER) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item {
                SanctumSectionHeader("Assets")
                Spacer(modifier = Modifier.height(16.dp))

                SanctumTextField(
                    value = state.rawCash,
                    onValueChange = { viewModel.updateCash(it) },
                    placeholder = "Cash in Hand/Bank",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
                Spacer(modifier = Modifier.height(12.dp))

                SanctumTextField(
                    value = state.rawGoldValue,
                    onValueChange = { viewModel.updateGoldValue(it) },
                    placeholder = "Gold Value",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
                Spacer(modifier = Modifier.height(12.dp))

                SanctumTextField(
                    value = state.rawSilverValue,
                    onValueChange = { viewModel.updateSilverValue(it) },
                    placeholder = "Silver Value",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
                Spacer(modifier = Modifier.height(12.dp))

                SanctumTextField(
                    value = state.rawInvestments,
                    onValueChange = { viewModel.updateInvestments(it) },
                    placeholder = "Investments Value",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
                Spacer(modifier = Modifier.height(12.dp))

                SanctumTextField(
                    value = state.rawBusinessInventory,
                    onValueChange = { viewModel.updateBusinessInventory(it) },
                    placeholder = "Business Inventory Value",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
            }

            item {
                SanctumSectionHeader("Liabilities")
                Spacer(modifier = Modifier.height(16.dp))

                SanctumTextField(
                    value = state.rawLiabilities,
                    onValueChange = { viewModel.updateLiabilities(it) },
                    placeholder = "Short-Term Liabilities",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
            }

            item {
                SanctumSectionHeader("Settings")
                Spacer(modifier = Modifier.height(16.dp))

                SanctumTextField(
                    value = state.rawGoldPrice,
                    onValueChange = { viewModel.updateGoldPrice(it) },
                    placeholder = "Gold Price per Gram",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
                Spacer(modifier = Modifier.height(12.dp))

                SanctumTextField(
                    value = state.rawSilverPrice,
                    onValueChange = { viewModel.updateSilverPrice(it) },
                    placeholder = "Silver Price per Gram",
                    visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                )
            }
        }
    }

    @Composable
    private fun NisabStandardOption(
        title: String,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val backgroundColor = if (isSelected) SanctumTheme.colors.brand.copy(alpha = 0.1f) else SanctumTheme.colors.background
        val borderColor = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.outlineVariant

        SanctumCard(
            modifier = modifier.clickable { onClick() },
            backgroundColor = backgroundColor,
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = SanctumTheme.typography.bodyLarge,
                    color = if (isSelected) SanctumTheme.colors.brand else SanctumTheme.colors.textPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }

    private fun formatDouble(value: Double): String {
        // Use a simpler string formatting to prevent scientific notation truncation issues
        // We'll convert double to string in a way that avoids standard scientific notation for common UI values
        val formatted = value.toLong().toString()
        val decimalPart = ((value - value.toLong()) * 100).toInt()
        val paddedDecimal = if (decimalPart < 10) "0$decimalPart" else "$decimalPart"

        // This is a naive implementation but works safely up to 2^63 and avoids scientific E issues
        return "$formatted.$paddedDecimal"
    }
}
