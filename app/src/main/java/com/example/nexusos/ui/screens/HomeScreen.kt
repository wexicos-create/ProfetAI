package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.ScreenId
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

data class AppLauncherItem(
    val id: ScreenId,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val badge: String? = null
)

@Composable
fun HomeScreen(
    uiState: NexusUiState,
    onNavigate: (ScreenId) -> Unit,
    modifier: Modifier = Modifier
) {
    val appItems = listOf(
        AppLauncherItem(
            id = ScreenId.APK_STUDIO,
            name = "APK Studio",
            description = "SDK 35 • v2/v3 Signing",
            icon = Icons.Default.Terminal,
            color = EmeraldPrimary,
            badge = "${uiState.apks.size} APKs"
        ),
        AppLauncherItem(
            id = ScreenId.CRASH_ANALYZER,
            name = "Crash Inspector",
            description = "Moto G54 Diagnostic",
            icon = Icons.Default.BugReport,
            color = RoseAccent,
            badge = "1 Alert"
        ),
        AppLauncherItem(
            id = ScreenId.WEB_FORGE,
            name = "WebForge AI",
            description = "Prompt-to-Website",
            icon = Icons.Default.Language,
            color = BlueAccent
        ),
        AppLauncherItem(
            id = ScreenId.IMAGE_STUDIO,
            name = "Nexus Canvas",
            description = "Imagen 3 Generator",
            icon = Icons.Default.Image,
            color = PurpleAccent
        ),
        AppLauncherItem(
            id = ScreenId.VIDEO_SUITE,
            name = "CineGen AI",
            description = "Veo 3.1 Studio",
            icon = Icons.Default.Movie,
            color = AmberAccent
        ),
        AppLauncherItem(
            id = ScreenId.DOC_EDITOR,
            name = "DocMaster AI",
            description = "Modify & Refactor",
            icon = Icons.Default.Description,
            color = CyanAccent
        ),
        AppLauncherItem(
            id = ScreenId.GEMINI_ASSISTANT,
            name = "Gemini Copilot",
            description = "OS Neural Engine",
            icon = Icons.Default.AutoAwesome,
            color = EmeraldPrimary
        ),
        AppLauncherItem(
            id = ScreenId.FILE_EXPLORER,
            name = "Storage & APKs",
            description = "AAB / APK Packages",
            icon = Icons.Default.Folder,
            color = BlueAccent
        ),
        AppLauncherItem(
            id = ScreenId.SETTINGS,
            name = "Settings",
            description = "Material You Accents",
            icon = Icons.Default.Settings,
            color = TextSecondary
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Android 15 Quick Glance Widget
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NexusOS 15 AI Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Android 15 Kernel 6.6 • Gemini 3.6 Flash",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "SDK 35",
                                color = EmeraldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Crash alert quick chip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(RoseAccent.copy(alpha = 0.15f))
                            .border(1.dp, RoseAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { onNavigate(ScreenId.CRASH_ANALYZER) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert",
                            tint = RoseAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fatal Crash: Archive is not a ZIP archive",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseAccent
                            )
                            Text(
                                text = "Moto G54 5G • 1-Click APK Repair Available",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Inspect",
                            tint = RoseAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Grid of App Launchers
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(appItems) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceCard)
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .clickable { onNavigate(item.id) }
                            .padding(10.dp)
                            .testTag("app_launcher_${item.id.name.lowercase()}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(item.color.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name,
                                tint = item.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.description,
                            fontSize = 9.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Bottom Android Quick Dock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceCardHover)
                .border(1.dp, BorderColor, RoundedCornerShape(24.dp))
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dockItems = listOf(
                Triple(ScreenId.GEMINI_ASSISTANT, Icons.Default.AutoAwesome, EmeraldPrimary),
                Triple(ScreenId.APK_STUDIO, Icons.Default.Terminal, EmeraldPrimary),
                Triple(ScreenId.FILE_EXPLORER, Icons.Default.Folder, BlueAccent),
                Triple(ScreenId.SETTINGS, Icons.Default.Settings, TextSecondary)
            )

            dockItems.forEach { (id, icon, tint) ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.2f))
                        .clickable { onNavigate(id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = id.name,
                        tint = tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
