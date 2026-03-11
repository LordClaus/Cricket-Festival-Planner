package com.client.cricketfestivalplanner.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}