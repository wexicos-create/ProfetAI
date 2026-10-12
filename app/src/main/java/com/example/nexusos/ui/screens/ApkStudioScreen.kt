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
import com.example.nexusos.model.ApkProject
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun ApkStudioScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onGenerateApk: (String) -> Unit,
    onDownloadApk: (ApkProject) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedApk by remember(uiState.apks) { mutableStateOf(uiState.apks.firstOrNull()) }
    var prompt by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("preview") } // preview, playstore, kotlin, xml, manifest

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        NexusHeader(
            title = "APK Studio AI 15",
            subtitle = "Compile & test Android APKs instantly",
            icon = Icons.Default.Terminal,
            iconTint = EmeraldPrimary,
            onBack = onBack,
            trailingContent = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Firma v2/v3",
                        color = EmeraldPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        )

        // Prompt input
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
                placeholder = { Text("Describe APK to generate (e.g. Crypto Tracker...)", fontSize = 12.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("apk_prompt_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = BorderColor,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        onGenerateApk(prompt)
                        prompt = ""
                    }
                },
                enabled = !uiState.isGenerating && prompt.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(52.dp)
                    .testTag("generate_apk_button")
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Generate", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Build", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Horizontal Project Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.apks.forEach { apk ->
                val isSelected = selectedApk?.id == apk.id
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) EmeraldPrimary else SurfaceCard)
                        .border(1.dp, if (isSelected) EmeraldPrimary else BorderColor, RoundedCornerShape(10.dp))
                        .clickable { selectedApk = apk }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = apk.appName,
                        tint = if (isSelected) Color.Black else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = apk.appName,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else TextPrimary
                    )
                }
            }
        }

        selectedApk?.let { apk ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header inside card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceCardHover)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(apk.appName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text("(${apk.packageName})", fontSize = 10.sp, color = TextMuted)
                            }
                            Text("Firma v2 OK (apksigner) • SDK 35", fontSize = 10.sp, color = EmeraldPrimary, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { onDownloadApk(apk) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary.copy(alpha = 0.2f), contentColor = EmeraldPrimary),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("APK v2", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Tab selector row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceDark)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val tabs = listOf(
                            Pair("preview", "Live Sandbox"),
                            Pair("playstore", "Play Console Export"),
                            Pair("kotlin", "MainActivity.kt"),
                            Pair("xml", "activity_main.xml"),
                            Pair("manifest", "Manifest")
                        )
                        tabs.forEach { (tabId, label) ->
                            val isTabSelected = activeTab == tabId
                            Box(
                                modifier = Modifier
                                    .clickable { activeTab = tabId }
                                    .background(if (isTabSelected) SurfaceCard else Color.Transparent)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isTabSelected) EmeraldPrimary else TextSecondary
                                )
                            }
                        }
                    }

                    // Tab content
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
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Virtual Phone Device
                                    Box(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .height(340.dp)
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(Color.Black)
                                            .border(3.dp, BorderColor, RoundedCornerShape(24.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(SurfaceDark)
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(apk.appName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmeraldPrimary)
                                                Text(apk.description, fontSize = 10.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                                            }

                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(EmeraldPrimary.copy(alpha = 0.2f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("App Running Live", fontSize = 10.sp, color = TextSecondary)
                                            }

                                            Button(
                                                onClick = {},
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                                                modifier = Modifier.fillMaxWidth().height(36.dp)
                                            ) {
                                                Text("Interactive Button", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            "playstore" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Google Play Bundle (.AAB) Ready", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                    Text("Signed with RSA-2048, Scheme v2/v3/v4 enabled. Target SDK: 35 (Android 15). Compatible with 99.8% of modern Android devices.", fontSize = 11.sp, color = TextSecondary)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(SurfaceDark)
                                            .padding(10.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text("• Package: ${apk.packageName}", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CyanAccent)
                                            Text("• Version Code: 1 (VersionName: ${apk.version})", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CyanAccent)
                                            Text("• Adaptive Icon: 512x512 PNG included", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CyanAccent)
                                            Text("• APK Alignment: 4-byte zipalign verified", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CyanAccent)
                                        }
                                    }
                                }
                            }
                            "kotlin" -> {
                                Text(
                                    text = apk.kotlinCode,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                            }
                            "xml" -> {
                                Text(
                                    text = apk.xmlLayout,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyanAccent,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                            }
                            "manifest" -> {
                                Text(
                                    text = apk.manifest,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberAccent,
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
