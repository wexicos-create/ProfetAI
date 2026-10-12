package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.ImageItem
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun ImageStudioScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onGenerateImage: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var prompt by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("Cyberpunk") }
    var aspect by remember { mutableStateOf("16:9") }
    var activeTab by remember { mutableStateOf("studio") }
    var selectedImage by remember(uiState.images) { mutableStateOf(uiState.images.firstOrNull()) }
    var selectedFilter by remember { mutableStateOf("Normal") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NexusHeader(
            title = "Nexus Canvas AI",
            subtitle = "AI Image Generation & Professional Editor",
            icon = Icons.Default.Image,
            iconTint = PurpleAccent,
            onBack = onBack,
            trailingContent = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .padding(2.dp)
                ) {
                    Button(
                        onClick = { activeTab = "studio" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == "studio") PurpleAccent else Color.Transparent,
                            contentColor = if (activeTab == "studio") Color.White else TextMuted
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Studio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { activeTab = "editor" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == "editor") PurpleAccent else Color.Transparent,
                            contentColor = if (activeTab == "editor") Color.White else TextMuted
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Editor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )

        if (activeTab == "studio") {
            // Prompt Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AI PROMPT & DESCRIPTION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        placeholder = { Text("Describe image in vivid detail...", fontSize = 11.sp, color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("image_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedBorderColor = PurpleAccent,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    // Style Row
                    Text("ART STYLE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Cyberpunk", "Photorealistic", "3D Render", "Anime", "Oil Painting").forEach { s ->
                            val isSelected = style == s
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PurpleAccent else SurfaceDark)
                                    .clickable { style = s }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(s, fontSize = 11.sp, color = if (isSelected) Color.White else TextSecondary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (prompt.isNotBlank()) {
                                onGenerateImage(prompt, style, aspect)
                                prompt = ""
                            }
                        },
                        enabled = !uiState.isGenerating && prompt.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_image_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent, contentColor = Color.White)
                    ) {
                        if (uiState.isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Synthesize", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Synthesize with Imagen 3", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Gallery
            Text("RECENT AI CREATIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            uiState.images.forEach { img ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedImage = img },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(if (selectedImage?.id == img.id) PurpleAccent else BorderColor))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(img.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                            Text(img.style, fontSize = 10.sp, color = PurpleAccent)
                        }
                        Text(img.prompt, fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        } else {
            // Editor Mode
            selectedImage?.let { img ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Preview: ${img.title} ($selectedFilter)",
                                color = PurpleAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text("AI FILTERS & ENHANCEMENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Normal", "B&W", "Sepia", "HDR+ AI").forEach { f ->
                                val isF = selectedFilter == f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isF) PurpleAccent.copy(alpha = 0.2f) else SurfaceDark)
                                        .border(1.dp, if (isF) PurpleAccent else BorderColor, RoundedCornerShape(8.dp))
                                        .clickable { selectedFilter = f }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(f, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isF) PurpleAccent else TextSecondary)
                                }
                            }
                        }

                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent.copy(alpha = 0.2f), contentColor = PurpleAccent)
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = "Upscale", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Magical AI Upscale & Retouch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
