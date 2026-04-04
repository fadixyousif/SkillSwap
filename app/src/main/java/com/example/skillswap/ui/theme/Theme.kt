package com.example.skillswap.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SkillSwapColorScheme = darkColorScheme(
    primary = SkillSwapPrimary,
    onPrimary = SkillSwapTextPrimary,

    secondary = SkillSwapSecondary,
    onSecondary = SkillSwapBackground,

    background = SkillSwapBackground,
    onBackground = SkillSwapTextPrimary,

    surface = SkillSwapSurface,
    onSurface = SkillSwapTextPrimary,

    surfaceVariant = SkillSwapSurfaceAlt,
    onSurfaceVariant = SkillSwapTextSecondary,

    outline = SkillSwapBorder
)

@Composable
fun SkillSwapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SkillSwapColorScheme,
        typography = Typography,
        content = content
    )
}