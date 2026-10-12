package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.VideoItem
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun VideoSuiteScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onGenerateVideo: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var prompt by remember { mutableStateOf("") }
    var resolution by remember { mutableStateOf("1080p") }
    var format by remember { mutableStateOf("16:9") }
    var isPlaying by remember { mutableStateOf(false) }
    var selectedVideo by remember(uiState.videos) { mutableStateOf(uiState.videos.firstOrNull()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NexusHeader(
            title = "CineGen AI 15",
            subtitle = "Cinematic AI Video Generation Studio (Veo)",
            icon = Icons.Default.Movie,
            iconTint = AmberAccent,
            onBack = onBack,
            trailingContent = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AmberAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Veo 3.1",
                        color = AmberAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        )

        // Generator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("VIDEO PROMPT & STORYBOARD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("Describe cinematic video (e.g. Neon drone shot flying through future city)...", fontSize = 11.sp, color = TextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("video_prompt_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedBorderColor = AmberAccent,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Resolution
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("RESOLUTION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("1080p", "720p").forEach { r ->
                                val isR = resolution == r
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isR) AmberAccent else SurfaceDark)
                                        .clickable { resolution = r }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(r, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isR) Color.Black else TextSecondary)
                                }
                            }
                        }
                    }

                    // Format
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("FORMAT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("16:9", "9:16").forEach { f ->
                                val isF = format == f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isF) AmberAccent else SurfaceDark)
                                        .clickable { format = f }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(f, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isF) Color.Black else TextSecondary)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (prompt.isNotBlank()) {
                            onGenerateVideo(prompt, resolution, format)
                            prompt = ""
                        }
                    },
                    enabled = !uiState.isGenerating && prompt.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("generate_video_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberAccent, contentColor = Color.Black)
                ) {
                    if (uiState.isGenerating) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Movie, contentDescription = "Generate", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate Cinematic Video", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Active Player Card
        selectedVideo?.let { vid ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(AmberAccent)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("${vid.resolution} • ${vid.duration}", fontSize = 10.sp, color = AmberAccent, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(vid.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text(vid.prompt, fontSize = 11.sp, color = TextSecondary)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent.copy(alpha = 0.2f), contentColor = AmberAccent),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "MP4", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("MP4", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
