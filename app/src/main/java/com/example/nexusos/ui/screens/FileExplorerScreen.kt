package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.ScreenId
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun FileExplorerScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onNavigate: (ScreenId) -> Unit,
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
            title = "Files & Storage",
            subtitle = "Android 15 Asset & APK Package Storage",
            icon = Icons.Default.Folder,
            iconTint = BlueAccent,
            onBack = onBack
        )

        // Section: Compiled APKs & AAB Bundles
        Text("COMPILED APKS & BUNDLES (${uiState.apks.size + 1})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCardHover),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary.copy(alpha = 0.4f)))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Play Store AAB
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("shieldsecurity-playstore.aab", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PurpleAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("AAB Bundle", fontSize = 8.sp, color = PurpleAccent, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("RSA-2048 • Esquema v2/v3/v4 • Adaptive Icon 512x512", fontSize = 9.sp, color = TextMuted)
                    }

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent, contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AAB", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = BorderColor)

                // Darkia APK with Inspector link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("instal/darkia.apk", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(EmeraldPrimary.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("v2/v3 Validadas", fontSize = 8.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("ZIP Magic Bytes PK\\x03\\x04 • zipalign 4-byte • SDK 35", fontSize = 9.sp, color = TextMuted)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { onNavigate(ScreenId.CRASH_ANALYZER) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RoseAccent.copy(alpha = 0.2f), contentColor = RoseAccent),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.BugReport, contentDescription = "Inspector", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Inspector", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "APK", modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("APK", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // User APKs
        uiState.apks.forEach { apk ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenId.APK_STUDIO) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(apk.appName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        Text(apk.packageName, fontSize = 10.sp, color = TextMuted)
                    }
                    Icon(Icons.Default.Download, contentDescription = "Download", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Section: Web Projects
        Text("WEB PROJECTS (${uiState.webs.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        uiState.webs.forEach { web ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenId.WEB_FORGE) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(web.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        Text(web.prompt, fontSize = 10.sp, color = TextMuted)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(BlueAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("HTML", fontSize = 9.sp, color = BlueAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Documents
        Text("DOCUMENTS & FILES (${uiState.docs.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        uiState.docs.forEach { doc ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenId.DOC_EDITOR) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(doc.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        Text("Size: ${doc.size}", fontSize = 10.sp, color = TextMuted)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CyanAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(doc.type.uppercase(), fontSize = 9.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
