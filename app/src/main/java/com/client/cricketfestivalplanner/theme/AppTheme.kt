package com.client.cricketfestivalplanner.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ColorTokens.PrimaryAccent,
    secondary = ColorTokens.SecondaryAccent,
    background = ColorTokens.Background,
    surface = ColorTokens.Card,
    onPrimary = ColorTokens.Card,
    onSecondary = ColorTokens.Card,
    onBackground = ColorTokens.TextPrimary,
    onSurface = ColorTokens.TextPrimary,
    error = ColorTokens.Error,
    outline = ColorTokens.Divider
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorTokens.PrimaryAccent,
    secondary = ColorTokens.SecondaryAccent,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = ColorTokens.Error,
    outline = Color(0xFF3A3A3A)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}