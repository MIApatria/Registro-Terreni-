package com.francesco.crono.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Terracotta = Color(0xFFC1440E)
private val TerracottaLight = Color(0xFFFF7043)
private val Sand = Color(0xFFF6EEE3)

private val LightColors = lightColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    secondary = Color(0xFF1F6FB2),
    background = Sand,
    surface = Color.White,
    surfaceVariant = Color(0xFFF0E6D8)
)

private val DarkColors = darkColorScheme(
    primary = TerracottaLight,
    onPrimary = Color(0xFF3A1200),
    secondary = Color(0xFF7EC0F0),
    background = Color(0xFF14110E),
    surface = Color(0xFF1E1B17),
    surfaceVariant = Color(0xFF2A2621)
)

@Composable
fun CronoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
