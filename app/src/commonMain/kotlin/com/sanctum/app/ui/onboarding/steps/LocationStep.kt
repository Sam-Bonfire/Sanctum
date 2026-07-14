package com.sanctum.app.ui.onboarding.steps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.sanctum.app.BuildConfig
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.compass.data.GeocodingRepository
import com.sanctum.core.feature.compass.domain.PlatformSensors
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LocationStep() {
    val sensors = koinInject<PlatformSensors>()
    val geocodingRepo = koinInject<GeocodingRepository>()
    val settings = koinInject<Settings>()
    val scope = rememberCoroutineScope()

    var showManualEntry by remember { mutableStateOf(false) }
    var manualCity by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    val savedLat = settings.getDoubleOrNull("location_lat")
    val savedName = settings.getStringOrNull("location_name")

    LaunchedEffect(savedLat) {
        if (savedLat != null) {
            successMsg = "Location set to $savedName"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp),
    ) {
        Text(
            text = "Set Your Location",
            style = SanctumTheme.typography.headlineLarge,
            color = SanctumTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "We use your location to provide accurate timings and directions for the ${BuildConfig.TERM_SCHEDULE_TITLE}.",
            style = SanctumTheme.typography.bodyLarge,
            color = SanctumTheme.colors.textSecondary,
            lineHeight = 24.sp,
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (successMsg != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SanctumTheme.colors.brand,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = successMsg!!,
                    style = SanctumTheme.typography.bodyLarge,
                    color = SanctumTheme.colors.textPrimary,
                )
            }
            return@Column
        }

        if (isLoading) {
            CircularProgressIndicator(color = SanctumTheme.colors.brand, modifier = Modifier.align(Alignment.CenterHorizontally))
            return@Column
        }

        if (errorMsg != null) {
            Text(
                text = errorMsg!!,
                color = SanctumTheme.colors.error,
                style = SanctumTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        if (showManualEntry) {
            OutlinedTextField(
                value = manualCity,
                onValueChange = { manualCity = it },
                label = { Text("Enter City Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = SanctumTheme.colors.textPrimary,
                    cursorColor = SanctumTheme.colors.brand,
                    focusedBorderColor = SanctumTheme.colors.brand,
                    unfocusedBorderColor = SanctumTheme.colors.textSecondary,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (manualCity.isBlank()) return@Button
                    scope.launch {
                        isLoading = true
                        errorMsg = null
                        val result = geocodingRepo.geocode(manualCity)
                        result.onSuccess { pair ->
                            val loc = pair.first
                            val name = pair.second
                            settings.putDouble("location_lat", loc.latitude)
                            settings.putDouble("location_lon", loc.longitude)
                            settings.putString("location_name", name)
                            successMsg = "Location set to $name"
                        }.onFailure {
                            errorMsg = "Could not find location or network error."
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SanctumTheme.colors.brand,
                    contentColor = Color.White,
                ),
            ) {
                Text("Save Location", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { showManualEntry = false },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Text("Back", color = SanctumTheme.colors.textPrimary)
            }
        } else {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMsg = null
                        val result = sensors.getCurrentLocation()
                        result.onSuccess { loc ->
                            settings.putDouble("location_lat", loc.latitude)
                            settings.putDouble("location_lon", loc.longitude)
                            settings.putString("location_name", "Current Location")
                            successMsg = "Location successfully determined."
                        }.onFailure {
                            errorMsg = "Could not get location automatically. Please enter manually."
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SanctumTheme.colors.brand,
                    contentColor = Color.White,
                ),
            ) {
                Text("Allow Location Access", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showManualEntry = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SanctumTheme.colors.textPrimary,
                ),
            ) {
                Text("Enter Manually", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
