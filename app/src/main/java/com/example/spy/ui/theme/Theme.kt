package com.example.spy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CompassColorScheme = darkColorScheme(
    primary = CompassBlueAccent,
    background = CompassDarkBg,
    surface = CompassCardBg,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = CompassBorder
)

@Composable
fun SpyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CompassColorScheme,
        content = content
    )
}