package com.example.nexusos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.nexusos.model.DocumentItem
import com.example.nexusos.ui.components.NexusHeader
import com.example.nexusos.ui.theme.*
import com.example.nexusos.viewmodel.NexusUiState

@Composable
fun DocEditorScreen(
    uiState: NexusUiState,
    onBack: () -> Unit,
    onUpdateDoc: (DocumentItem) -> Unit,
    onAiRefactor: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDoc by remember(uiState.docs) { mutableStateOf(uiState.docs.firstOrNull()) }
    var currentContent by remember(selectedDoc) { mutableStateOf(selectedDoc?.content ?: "") }
    var aiPrompt by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        NexusHeader(
            title = "DocMaster AI 15",
            subtitle = "Modify & Refactor any Document or File",
            icon = Icons.Default.Description,
            iconTint = CyanAccent,
            onBack = onBack,
            trailingContent = {
                Button(
                    onClick = {
                        selectedDoc?.let { doc ->
                            onUpdateDoc(doc.copy(content = currentContent))
                            isSaved = true
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("save_doc_button")
                ) {
                    Icon(if (isSaved) Icons.Default.Check else Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isSaved) "Saved!" else "Save", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        )

        // Document Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.docs.forEach { doc ->
                val isSelected = selectedDoc?.id == doc.id
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) CyanAccent else SurfaceCard)
                        .border(1.dp, if (isSelected) CyanAccent else BorderColor, RoundedCornerShape(10.dp))
                        .clickable {
                            selectedDoc = doc
                            currentContent = doc.content
                            isSaved = false
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (doc.type == "markdown") Icons.Default.Description else Icons.Default.Code,
                        contentDescription = doc.name,
                        tint = if (isSelected) Color.Black else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = doc.name,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else TextPrimary
                    )
                }
            }
        }

        // AI Refactor Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.AutoFixHigh, contentDescription = "AI", tint = CyanAccent, modifier = Modifier.size(18.dp))
            OutlinedTextField(
                value = aiPrompt,
                onValueChange = { aiPrompt = it },
                placeholder = { Text("Ask AI to modify (e.g. Format as tables, summarize...)", fontSize = 11.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_doc_prompt_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
            Button(
                onClick = {
                    selectedDoc?.let { doc ->
                        if (aiPrompt.isNotBlank()) {
                            onAiRefactor(doc.id, aiPrompt)
                            aiPrompt = ""
                        }
                    }
                },
                enabled = !uiState.isGenerating && aiPrompt.isNotBlank(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("ai_refactor_button")
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Text("AI Edit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Editor TextArea Card
        selectedDoc?.let { doc ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderColor))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceCardHover)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Editing: ${doc.name}", fontSize = 11.sp, color = TextSecondary)
                        Text("Size: ${doc.size}", fontSize = 11.sp, color = TextMuted)
                    }

                    OutlinedTextField(
                        value = currentContent,
                        onValueChange = {
                            currentContent = it
                            isSaved = false
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("doc_content_editor"),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            color = CyanAccent,
                            fontSize = 12.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0A0E17),
                            unfocusedContainerColor = Color(0xFF0A0E17),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}
