package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.WebProject
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun WebForgeScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onGenerateWeb: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedWeb by remember(uiState.webs) { mutableStateOf(uiState.webs.firstOrNull()) }
    var prompt by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("preview") }
    var deviceMode by remember { mutableStateOf("desktop") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        NexusHeader(
            title = "WebForge AI 15",
            subtitle = "Prompt-to-Website Builder & Live Sandbox",
            icon = Icons.Default.Language,
            iconTint = BlueAccent,
            onBack = onBack,
            trailingContent = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .padding(2.dp)
                ) {
                    IconButton(
                        onClick = { deviceMode = "desktop" },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Computer, contentDescription = "Desktop", tint = if (deviceMode == "desktop") BlueAccent else TextMuted, modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = { deviceMode = "mobile" },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = "Mobile", tint = if (deviceMode == "mobile") BlueAccent else TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        )

        // Prompt Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = { Text("Describe website to build (e.g. AI SaaS Landing...)", fontSize = 12.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("web_prompt_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = BlueAccent,
                    unfocusedBorderColor = BorderColor,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        onGenerateWeb(prompt)
                        prompt = ""
                    }
                },
                enabled = !uiState.isGenerating && prompt.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent, contentColor = Color.White),
                modifier = Modifier
                    .height(52.dp)
                    .testTag("build_web_button")
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Build", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Build", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Project Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.webs.forEach { web ->
                val isSelected = selectedWeb?.id == web.id
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) BlueAccent else SurfaceCard)
                        .border(1.dp, if (isSelected) BlueAccent else BorderColor, RoundedCornerShape(10.dp))
                        .clickable { selectedWeb = web }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Language, contentDescription = web.title, tint = if (isSelected) Color.White else TextSecondary, modifier = Modifier.size(14.dp))
                    Text(
                        text = web.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else TextPrimary
                    )
                }
            }
        }

        selectedWeb?.let { web ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceDark)
                    ) {
                        listOf(
                            Pair("preview", "Live Preview"),
                            Pair("html", "index.html"),
                            Pair("css", "style.css")
                        ).forEach { (tabId, label) ->
                            val isTabSelected = activeTab == tabId
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { activeTab = tabId }
                                    .background(if (isTabSelected) SurfaceCard else Color.Transparent)
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isTabSelected) BlueAccent else TextSecondary
                                )
                            }
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        when (activeTab) {
                            "preview" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF090D16))
                                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("NEXUS WEB PREVIEW", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = BlueAccent)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(web.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(web.prompt, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(vertical = 8.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccent, contentColor = Color.White),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Interactive Button", fontSize = 11.sp)
                                    }
                                }
                            }
                            "html" -> {
                                Text(
                                    text = web.htmlCode,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = BlueAccent,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                            }
                            "css" -> {
                                Text(
                                    text = web.cssCode,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = PurpleAccent,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
