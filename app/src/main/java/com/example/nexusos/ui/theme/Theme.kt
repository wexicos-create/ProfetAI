package com.example.nexusos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.nexusos.model.ThemeAccent

@Composable
fun NexusOSTheme(
    accent: ThemeAccent = ThemeAccent.EMERALD,
    content: @Composable () -> Unit
) {
    val primaryColor = Color(accent.colorHex)

    val colorScheme = darkColorScheme(
        primary = primaryColor,
        onPrimary = Color.Black,
        primaryContainer = primaryColor.copy(alpha = 0.2f),
        onPrimaryContainer = primaryColor,
        background = BackgroundDark,
        onBackground = TextPrimary,
        surface = SurfaceDark,
        onSurface = TextPrimary,
        surfaceVariant = SurfaceCard,
        onSurfaceVariant = TextSecondary,
        outline = BorderColor
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
