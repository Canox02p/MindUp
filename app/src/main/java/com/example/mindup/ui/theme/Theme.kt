package com.example.mindup.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MindUpPrimaryBlueDark,
    onPrimary = Color.White,
    secondary = MindUpNavyDark,
    onSecondary = Color(0xFF0D1117),
    background = MindUpPageBgDark,
    onBackground = MindUpNavyDark,
    surface = MindUpCardBgDark,
    onSurface = MindUpNavyDark,
    surfaceVariant = MindUpSoftBorderDark,
    outline = MindUpSoftBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = MindUpPrimaryBlue,
    onPrimary = Color.White,
    secondary = MindUpNavy,
    onSecondary = Color.White,
    background = MindUpPageBg,
    onBackground = MindUpNavy,
    surface = MindUpCardBg,
    onSurface = MindUpNavy,
    surfaceVariant = MindUpSoftBorder,
    outline = MindUpSoftBorder
)

@Composable
fun MindUpTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,

content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
