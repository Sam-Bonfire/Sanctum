package com.sanctum.core.feature.share.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sanctum.core.core.designsystem.theme.SanctumTheme
import com.sanctum.core.feature.scripture.domain.ScriptureVerse
import com.sanctum.core.feature.share.domain.ShareController
import kotlinx.coroutines.launch

class ShareVerseScreen(
    val verse: ScriptureVerse,
    val bookName: String,
    val chapterNumber: Int,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val shareController = remember { ShareController() }
        val graphicsLayer = rememberGraphicsLayer()

        val backgroundPresets = listOf(
            listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)),
            listOf(Color(0xFF1CB5E0), Color(0xFF000851)),
            listOf(Color(0xFFFDC830), Color(0xFFF37335)),
            listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)),
            listOf(Color(0xFF232526), Color(0xFF414345)),
            listOf(Color(0xFFECE9E6), Color(0xFFFFFFFF)),
        )

        var selectedBackground by remember { mutableStateOf(backgroundPresets[0]) }
        var showTranslation by remember { mutableStateOf(true) }
        var showOriginal by remember { mutableStateOf(true) }
        var fontScale by remember { mutableStateOf(1f) }
        val isLightText = selectedBackground[0].luminance() < 0.5f
        val textColor = if (isLightText) Color.White else Color.Black

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Share Verse", color = SanctumTheme.colors.textPrimary) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SanctumTheme.colors.textPrimary)
                        }
                    },
                    backgroundColor = SanctumTheme.colors.background,
                    elevation = 0.dp,
                )
            },
            backgroundColor = SanctumTheme.colors.background,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // Preview Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.8f)
                            .drawWithContent {
                                graphicsLayer.record {
                                    this@drawWithContent.drawContent()
                                }
                                drawLayer(graphicsLayer)
                            }
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(selectedBackground))
                            .padding(24.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (showOriginal && verse.originalText.isNotEmpty() && verse.originalText != verse.translation) {
                                Text(
                                    text = verse.originalText,
                                    color = textColor,
                                    fontSize = (24 * fontScale).sp,
                                    fontFamily = SanctumTheme.typography.amiri,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                )
                            }

                            if (showTranslation) {
                                Text(
                                    text = verse.translation,
                                    color = textColor,
                                    fontSize = (18 * fontScale).sp,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center,
                                    lineHeight = (28 * fontScale).sp,
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "$bookName $chapterNumber:${verse.number}",
                                color = textColor.copy(alpha = 0.7f),
                                fontSize = (14 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }

                        // Watermark
                        Text(
                            text = "via Sanctum",
                            color = textColor.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                        )
                    }
                }

                // Controls Area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 4.dp,
                    backgroundColor = SanctumTheme.colors.surface,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text("Background", fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            backgroundPresets.forEach { preset ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(preset))
                                        .border(
                                            width = 2.dp,
                                            color = if (selectedBackground == preset) SanctumTheme.colors.brand else Color.Transparent,
                                            shape = CircleShape,
                                        )
                                        .clickable { selectedBackground = preset },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Text Options", fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Original Text", color = SanctumTheme.colors.textPrimary)
                            Switch(checked = showOriginal, onCheckedChange = { showOriginal = it }, colors = SwitchDefaults.colors(checkedThumbColor = SanctumTheme.colors.brand))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Translation", color = SanctumTheme.colors.textPrimary)
                            Switch(checked = showTranslation, onCheckedChange = { showTranslation = it }, colors = SwitchDefaults.colors(checkedThumbColor = SanctumTheme.colors.brand))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Text Size", fontWeight = FontWeight.Bold, color = SanctumTheme.colors.textPrimary)
                        Slider(
                            value = fontScale,
                            onValueChange = { fontScale = it },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(thumbColor = SanctumTheme.colors.brand, activeTrackColor = SanctumTheme.colors.brand),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (shareController.isShareSupported()) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val bitmap = graphicsLayer.toImageBitmap()
                                        shareController.shareImage(bitmap)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = SanctumTheme.colors.brand),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share Image", color = Color.White)
                            }
                        } else {
                            Text(
                                "Sharing image is not supported on this platform.",
                                color = SanctumTheme.colors.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}
