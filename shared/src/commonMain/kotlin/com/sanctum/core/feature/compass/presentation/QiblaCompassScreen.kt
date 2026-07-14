package com.sanctum.core.feature.compass.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanctum.core.core.design.LocalWhiteLabelConfig
import com.sanctum.core.core.designsystem.components.SanctumCard
import com.sanctum.core.core.designsystem.components.SanctumPrimaryButton
import com.sanctum.core.core.designsystem.components.SanctumSectionHeader
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.compass.domain.GeoLocation
import com.sanctum.core.feature.compass.domain.PlatformSensors
import com.sanctum.core.feature.compass.domain.QiblaMath
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QiblaCompassScreen(
    sensors: PlatformSensors,
    onManualLocationClick: () -> Unit,
) {
    var deviceHeading by remember { mutableStateOf(0f) }
    var location by remember { mutableStateOf<GeoLocation?>(null) }
    var qiblaBearing by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        // Fetch location
        sensors.getCurrentLocation().onSuccess { loc ->
            location = loc
            qiblaBearing = QiblaMath.calculateQiblaDirection(loc.latitude, loc.longitude)
        }.onFailure {
            // Handle failure implicitly for now (could show manual input prompt)
        }
    }

    LaunchedEffect(Unit) {
        // Collect heading updates
        sensors.deviceHeading.collectLatest { heading ->
            if (heading != null) {
                deviceHeading = heading
            }
        }
    }

    // Calculate the pointer angle:
    // If the device is facing North (0 deg), the Kaaba is at `qiblaBearing`.
    // When the device rotates, the pointer must counter-rotate by `deviceHeading`.
    val pointerAngle = (qiblaBearing.toFloat() - deviceHeading + 360f) % 360f

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val config = LocalWhiteLabelConfig.current
            Spacer(modifier = Modifier.height(48.dp))

            // ─── Cinematic Hero Header ──────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SanctumTheme.colors.brand.copy(alpha = 0.08f),
                                SanctumTheme.colors.brand.copy(alpha = 0.02f),
                            ),
                        ),
                    )
                    .border(
                        width = 0.5.dp,
                        color = SanctumTheme.colors.outlineVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SACRED DIRECTION",
                        fontSize = 11.sp,
                        color = SanctumTheme.colors.brand,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = config.compassTitle,
                        fontSize = 28.sp,
                        color = SanctumTheme.colors.textPrimary,
                        fontWeight = FontWeight.Medium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        letterSpacing = 2.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (location != null) {
                Text(
                    text = "Bearing: ${qiblaBearing.toInt()}°",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp,
                )
            } else {
                Text(
                    text = "DETECTING LOCATION…",
                    style = SanctumTheme.typography.bodyMedium,
                    color = SanctumTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 11.sp,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ─── Section Header ──────────
            SanctumSectionHeader(
                text = "COMPASS",
                modifier = Modifier.align(Alignment.Start),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── The Compass ──────────
            SanctumCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(24.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        SanctumTheme.colors.brand.copy(alpha = 0.05f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        val primaryColor = SanctumTheme.colors.brand
                        val primaryVariantColor = SanctumTheme.colors.brandVariant
                        val onBackgroundColor = SanctumTheme.colors.textPrimary
                        val onBackgroundFaded = SanctumTheme.colors.textPrimary.copy(alpha = 0.3f)

                        // Outer Dial (Rotates with the phone)
                        Canvas(
                            modifier = Modifier.fillMaxSize().graphicsLayer {
                                rotationZ = -deviceHeading
                            },
                        ) {
                            val radius = size.width / 2
                            // Draw tick marks
                            for (i in 0 until 360 step 15) {
                                val isCardinal = i % 90 == 0
                                val tickLength = if (isCardinal) 20.dp.toPx() else 10.dp.toPx()
                                val tickColor = if (isCardinal) onBackgroundColor else onBackgroundFaded

                                rotate(i.toFloat(), size.center) {
                                    drawLine(
                                        color = tickColor,
                                        start = Offset(size.center.x, 0f),
                                        end = Offset(size.center.x, tickLength),
                                        strokeWidth = if (isCardinal) 3.dp.toPx() else 1.dp.toPx(),
                                        cap = StrokeCap.Round,
                                    )
                                }
                            }
                        }

                        // Inner Dial / Needle (Points to Qibla)
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            rotate(pointerAngle, size.center) {
                                // The Qibla Arrow
                                val path = Path().apply {
                                    moveTo(size.center.x, 30.dp.toPx()) // Tip
                                    lineTo(size.center.x + 15.dp.toPx(), size.center.y) // Right wide
                                    lineTo(size.center.x, size.center.y - 15.dp.toPx()) // Inner indent
                                    lineTo(size.center.x - 15.dp.toPx(), size.center.y) // Left wide
                                    close()
                                }

                                drawPath(
                                    path = path,
                                    brush = Brush.linearGradient(
                                        colors = listOf(primaryColor, primaryVariantColor),
                                    ),
                                )

                                // Center dot
                                drawCircle(
                                    color = onBackgroundColor,
                                    radius = 6.dp.toPx(),
                                    center = size.center,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Manual Location Button
            SanctumPrimaryButton(
                onClick = onManualLocationClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Set Location Manually")
            }

            Spacer(modifier = Modifier.height(SanctumTheme.spacing.bottomNavPadding))
        }
    }
}
