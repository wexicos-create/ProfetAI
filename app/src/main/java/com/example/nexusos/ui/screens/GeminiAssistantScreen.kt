package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.nexusos.model.ScreenId
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun GeminiAssistantScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onNavigate: (ScreenId) -> Unit,
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        NexusHeader(
            title = "Gemini Copilot",
            subtitle = "Android 15 Neural Engine & Assistant",
            icon = Icons.Default.AutoAwesome,
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
                        text = "Gemini 3.6 Flash",
                        color = EmeraldPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.messages) { msg ->
                val isUser = msg.sender == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = "Gemini", tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Column(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                )
                            )
                            .background(if (isUser) BlueAccent else SurfaceCard)
                            .border(1.dp, if (isUser) BlueAccent else BorderColor, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = msg.text,
                            fontSize = 12.sp,
                            color = if (isUser) Color.White else TextPrimary,
                            lineHeight = 16.sp
                        )

                        // Action card button inside Gemini response
                        msg.actionCard?.let { action ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { onNavigate(action.targetScreen) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .testTag("action_card_button")
                            ) {
                                Text(action.title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open", modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    if (isUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(BlueAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "User", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            if (uiState.isGenerating) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .padding(10.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), color = EmeraldPrimary, strokeWidth = 2.dp)
                        Text("Gemini is thinking...", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }

        // Input Form
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Ask Gemini to build APK, web, or analyze crash...", fontSize = 11.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("gemini_chat_input"),
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

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onSendMessage(input)
                        input = ""
                    }
                },
                enabled = !uiState.isGenerating && input.isNotBlank(),
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (input.isNotBlank()) EmeraldPrimary else SurfaceCard)
                    .testTag("send_chat_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (input.isNotBlank()) Color.Black else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
