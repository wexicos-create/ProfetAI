package com.example.nexusos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexusos.model.ScreenId
import com.example.nexusos.ui.components.NexusNavigationBar
import com.example.nexusos.ui.components.NexusStatusBar
import com.example.nexusos.ui.screens.*
import com.example.nexusos.ui.theme.BackgroundDark
import com.example.nexusos.ui.theme.EmeraldPrimary
import com.example.nexusos.ui.theme.NexusOSTheme
import com.example.nexusos.viewmodel.NexusOsViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: NexusOsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            // BackHandler handles back press inside Jetpack Compose
            BackHandler(enabled = uiState.currentScreen != ScreenId.HOME || uiState.backStack.size > 1) {
                viewModel.navigateBack()
            }

            NexusOSTheme(accent = uiState.themeAccent) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .navigationBarsPadding()
                        ) {
                            // Android 15 Status Bar
                            NexusStatusBar()

                            // App Content Area with animated transitions
                            Box(modifier = Modifier.weight(1f)) {
                                AnimatedContent(
                                    targetState = uiState.currentScreen,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "ScreenTransition"
                                ) { screen ->
                                    when (screen) {
                                        ScreenId.HOME -> HomeScreen(
                                            uiState = uiState,
                                            onNavigate = { viewModel.navigateTo(it) }
                                        )
                                        ScreenId.APK_STUDIO -> ApkStudioScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onGenerateApk = { prompt -> viewModel.generateApk(prompt) },
                                            onDownloadApk = { /* handled */ }
                                        )
                                        ScreenId.CRASH_ANALYZER -> CrashAnalyzerScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onAnalyzeCrash = { raw -> viewModel.analyzeCustomCrash(raw) },
                                            onRepairApk = { crash -> viewModel.repairCrashApk(crash) }
                                        )
                                        ScreenId.WEB_FORGE -> WebForgeScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onGenerateWeb = { prompt -> viewModel.generateWeb(prompt) }
                                        )
                                        ScreenId.IMAGE_STUDIO -> ImageStudioScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onGenerateImage = { prompt, style, aspect ->
                                                viewModel.generateImage(prompt, style, aspect)
                                            }
                                        )
                                        ScreenId.VIDEO_SUITE -> VideoSuiteScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onGenerateVideo = { prompt, res, aspect ->
                                                viewModel.generateVideo(prompt, res, aspect)
                                            }
                                        )
                                        ScreenId.DOC_EDITOR -> DocEditorScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onUpdateDoc = { doc -> viewModel.updateDoc(doc) },
                                            onAiRefactor = { docId, prompt -> viewModel.aiRefactorDoc(docId, prompt) }
                                        )
                                        ScreenId.GEMINI_ASSISTANT -> GeminiAssistantScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                                            onNavigate = { viewModel.navigateTo(it) }
                                        )
                                        ScreenId.FILE_EXPLORER -> FileExplorerScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onNavigate = { viewModel.navigateTo(it) }
                                        )
                                        ScreenId.SETTINGS -> SettingsScreen(
                                            uiState = uiState,
                                            onBack = { viewModel.navigateBack() },
                                            onSelectAccent = { viewModel.setThemeAccent(it) },
                                            onTogglePhoneFrame = { viewModel.togglePhoneFrame() }
                                        )
                                    }
                                }
                            }

                            // Android 15 3-Button Navigation Bar
                            NexusNavigationBar(
                                onBack = { viewModel.navigateBack() },
                                onHome = { viewModel.navigateTo(ScreenId.HOME) },
                                onRecents = { viewModel.navigateTo(ScreenId.FILE_EXPLORER) }
                            )
                        }

                        // Floating System Toast / Notification
                        uiState.statusNotification?.let { notif ->
                            LaunchedEffect(notif) {
                                delay(3000)
                                viewModel.clearNotification()
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Black.copy(alpha = 0.9f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = notif,
                                    color = EmeraldPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
