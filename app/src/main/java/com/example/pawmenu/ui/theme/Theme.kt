package com.example.pawmenu.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeMode { SYSTEM, LIGHT, DARK }

private val LightColors = lightColorScheme(
    primary = Color(0xFF6B8F6B), onPrimary = Color.White,
    background = Color(0xFFF6EFE7), onBackground = Color(0xFF2E3A2F),
    surface = Color(0xFFFFFBF6), onSurface = Color(0xFF2E3A2F),
    surfaceVariant = Color(0xFFF3EADF), onSurfaceVariant = Color(0xFF8B7D72),
    secondaryContainer = Color(0xFFF7D7D9), onSecondaryContainer = Color(0xFF2E3A2F),
    tertiary = Color(0xFFC9B8E4), outline = Color(0xFFE6DACD), error = Color(0xFFE8A0A6)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA78BFA), onPrimary = Color(0xFF1E1E2E),
    background = Color(0xFF1E1E2E), onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF2C2F3D), onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF252836), onSurfaceVariant = Color(0xFFB4B4C4),
    secondaryContainer = Color(0xFF3A3350), onSecondaryContainer = Color(0xFFEDEDED),
    tertiary = Color(0xFF8FD3A6), outline = Color(0xFF3B3F52), error = Color(0xFFFFB8A7)
)

@Composable
fun PawMenuTheme(mode: ThemeMode, content: @Composable () -> Unit) {
    val dark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
