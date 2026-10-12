package com.example.nexusos.model

enum class ScreenId {
    HOME,
    APK_STUDIO,
    CRASH_ANALYZER,
    WEB_FORGE,
    IMAGE_STUDIO,
    VIDEO_SUITE,
    DOC_EDITOR,
    GEMINI_ASSISTANT,
    FILE_EXPLORER,
    SETTINGS
}

enum class ThemeAccent(val label: String, val colorHex: Long) {
    EMERALD("Material Emerald", 0xFF10B981),
    LAVENDER("Cyber Lavender", 0xFFA855F7),
    OCEAN("Ocean Blue", 0xFF3B82F6),
    SUNSET("Sunset Amber", 0xFFF59E0B),
    OBSIDIAN("Obsidian Dark", 0xFF6B7280)
}

data class ApkProject(
    val id: String,
    val appName: String,
    val packageName: String,
    val version: String,
    val description: String,
    val category: String,
    val kotlinCode: String,
    val xmlLayout: String,
    val manifest: String,
    val buildStatus: String = "compiled",
    val createdAt: Long = System.currentTimeMillis()
)

data class WebProject(
    val id: String,
    val title: String,
    val prompt: String,
    val htmlCode: String,
    val cssCode: String,
    val jsCode: String,
    val framework: String = "HTML/JS",
    val createdAt: Long = System.currentTimeMillis()
)

data class ImageItem(
    val id: String,
    val title: String,
    val prompt: String,
    val url: String,
    val style: String,
    val aspectRatio: String = "16:9",
    val createdAt: Long = System.currentTimeMillis()
)

data class VideoItem(
    val id: String,
    val title: String,
    val prompt: String,
    val duration: String = "0:07",
    val resolution: String = "1080p",
    val thumbnailUrl: String,
    val status: String = "completed",
    val createdAt: Long = System.currentTimeMillis()
)

data class DocumentItem(
    val id: String,
    val name: String,
    val type: String,
    val content: String,
    val size: String,
    val updatedAt: Long = System.currentTimeMillis()
)

data class ActionCard(
    val type: String,
    val title: String,
    val targetScreen: ScreenId
)

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "gemini"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actionCard: ActionCard? = null
)

data class CrashLog(
    val id: String,
    val ver: String,
    val permission: String,
    val abi: String,
    val model: String,
    val sdk: String,
    val release: String,
    val targetSdk: String,
    val exceptionType: String,
    val exceptionMessage: String,
    val stackTrace: String,
    val timestamp: Long = System.currentTimeMillis()
)
