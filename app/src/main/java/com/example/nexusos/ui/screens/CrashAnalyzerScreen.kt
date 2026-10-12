package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.nexusos.model.CrashLog
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun CrashAnalyzerScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onAnalyzeCrash: (String) -> Unit,
    onRepairApk: (CrashLog) -> Unit,
    modifier: Modifier = Modifier
) {
    var rawInput by remember { mutableStateOf("") }
    val currentCrash = uiState.crashLogs.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NexusHeader(
            title = "Crash Inspector",
            subtitle = "Android 15 Neural Diagnostic & APK Repair",
            icon = Icons.Default.BugReport,
            iconTint = RoseAccent,
            onBack = onBack,
            trailingContent = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoseAccent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SDK 35 • Moto G54",
                        color = RoseAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        )

        // Raw input box
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = rawInput,
                onValueChange = { rawInput = it },
                placeholder = { Text("Paste raw Android stack trace or crash report...", fontSize = 11.sp, color = TextMuted) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("crash_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = RoseAccent,
                    unfocusedBorderColor = BorderColor,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Button(
                onClick = {
                    if (rawInput.isNotBlank()) {
                        onAnalyzeCrash(rawInput)
                        rawInput = ""
                    }
                },
                enabled = !uiState.isGenerating && rawInput.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoseAccent, contentColor = Color.White),
                modifier = Modifier
                    .height(52.dp)
                    .testTag("analyze_crash_button")
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Analyze", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Analyze", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        currentCrash?.let { crash ->
            // Diagnostic Alert Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCardHover),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(RoseAccent.copy(alpha = 0.5f)))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Security, contentDescription = "Alert", tint = RoseAccent, modifier = Modifier.size(18.dp))
                            Text("FATAL CRASH DETECTED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseAccent)
                        }
                        Text("Active Event", fontSize = 10.sp, color = TextMuted)
                    }

                    // Exception box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2A151C))
                            .border(1.dp, RoseAccent.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(crash.exceptionType, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = RoseAccent)
                            Text("\"${crash.exceptionMessage}\"", fontSize = 11.sp, color = TextPrimary)
                        }
                    }

                    // Device Grid
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Device Model", fontSize = 9.sp, color = TextMuted)
                                Text(crash.model, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Android OS / SDK", fontSize = 9.sp, color = TextMuted)
                                Text("Android ${crash.release} (SDK ${crash.sdk})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Target SDK / ABI", fontSize = 9.sp, color = TextMuted)
                                Text("SDK ${crash.targetSdk} • ${crash.abi}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                            }
                        }
                    }
                }
            }

            // AI Root Cause & Fix Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AI ROOT CAUSE BREAKDOWN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    Text(
                        "An APK archive was passed to the ZipInputStream decoder on Android 15, but lacks mandatory PK magic bytes (0x50 0x4B 0x03 0x04). The binary is corrupt or truncated.",
                        fontSize = 11.sp,
                        color = TextPrimary
                    )

                    Text("RECOMMENDED FIXES:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("1. Align archive using 4-byte zipalign tool.", fontSize = 10.sp, color = TextSecondary)
                        Text("2. Re-sign using APK Signature Scheme v2/v3.", fontSize = 10.sp, color = TextSecondary)
                        Text("3. Verify valid PK headers in payload.", fontSize = 10.sp, color = TextSecondary)
                    }

                    // 1-Click Repair button
                    Button(
                        onClick = { onRepairApk(crash) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("repair_apk_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = "Repair", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("1-Click Repair APK (Darkia Fixed)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Raw Stacktrace Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("RAW STACKTRACE DUMP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.padding(bottom = 6.dp))
                    Text(
                        text = crash.stackTrace,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
