package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.ThemeAccent
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun SettingsScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onSelectAccent: (ThemeAccent) -> Unit,
    onTogglePhoneFrame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        NexusHeader(
            title = "Android 15 Settings",
            subtitle = "NexusOS System & AI Configuration",
            icon = Icons.Default.Settings,
            iconTint = TextSecondary,
            onBack = onBack
        )

        // Material You Accents Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("MATERIAL YOU DYNAMIC THEME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)

                ThemeAccent.entries.forEach { acc ->
                    val isSelected = uiState.themeAccent == acc
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) SurfaceCardHover else SurfaceDark)
                            .border(1.dp, if (isSelected) Color(acc.colorHex) else BorderColor, RoundedCornerShape(10.dp))
                            .clickable { onSelectAccent(acc) }
                            .padding(12.dp)
                            .testTag("theme_accent_${acc.name.lowercase()}"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Color(acc.colorHex))
                            )
                            Text(acc.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        }

                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color(acc.colorHex), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // AI Engine Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("AI Neural Copilot Engine", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                    Text("Gemini 3.6 Flash & Veo 3.1", fontSize = 11.sp, color = EmeraldPrimary)
                    Text("● Fully Active & Server-Connected", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
                }
                Icon(Icons.Default.AutoAwesome, contentDescription = "Active", tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
            }
        }

        // System Specs Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("SYSTEM SPECIFICATIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueAccent)

                listOf(
                    Pair("OS Version", "Android 15 (Nexus Edition v15.4)"),
                    Pair("Security Patch", "August 2026"),
                    Pair("Target Architecture", "arm64-v8a • x86_64"),
                    Pair("Virtual RAM", "16 GB + 8 GB AI Swap"),
                    Pair("Android SDK Level", "35 (Android 15)")
                ).forEach { (label, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, fontSize = 11.sp, color = TextSecondary)
                        Text(value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                }
            }
        }
    }
}
