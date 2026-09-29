package com.miapatria.registroterreni.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrandGreen = Color(0xFF2E5E1E)
val BrandGreenDark = Color(0xFF1B3D10)
val LeafLight = Color(0xFF8CC152)
val Sand = Color(0xFFF6F4EC)
val EntrataGreen = Color(0xFF2E7D32)
val UscitaRed = Color(0xFFC62828)

private val LightColors = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDEBBB),
    onPrimaryContainer = BrandGreenDark,
    secondary = Color(0xFF6D8B5A),
    tertiary = Color(0xFFB08A3E),
    background = Sand,
    surface = Color.White,
    surfaceVariant = Color(0xFFECEFE6)
)

private val DarkColors = darkColorScheme(
    primary = LeafLight,
    onPrimary = BrandGreenDark,
    primaryContainer = BrandGreenDark,
    onPrimaryContainer = Color(0xFFCDEBBB),
    secondary = Color(0xFF9FBE86),
    background = Color(0xFF12160F),
    surface = Color(0xFF1B211A)
)

@Composable
fun RegistroTerreniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
