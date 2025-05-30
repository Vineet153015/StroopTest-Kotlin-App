package com.example.strooptest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFF06B6D4),
    tertiary = Color(0xFF8B5CF6),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFF06B6D4),
    tertiary = Color(0xFF8B5CF6),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun StroopTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Custom colors for the app
object StroopColors {
    val GameBackground = Color(0xFF0F172A)
    val CardBackground = Color(0xFF1E293B)
    val AccentBlue = Color(0xFF3B82F6)
    val AccentGreen = Color(0xFF10B981)
    val AccentRed = Color(0xFFEF4444)
    val AccentYellow = Color(0xFFF59E0B)
    val GradientStart = Color(0xFF6366F1)
    val GradientEnd = Color(0xFF8B5CF6)
}