package com.tapsave.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Grey10,
    primaryContainer = Green40,
    onPrimaryContainer = Grey99,

    secondary = Orange80,
    onSecondary = Grey10,
    secondaryContainer = Orange40,
    onSecondaryContainer = Grey99,

    tertiary = Success,
    error = Error,

    background = Grey10,
    onBackground = Grey95,
    surface = Grey20,
    onSurface = Grey95
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Grey99,
    primaryContainer = Green80,
    onPrimaryContainer = Grey10,

    secondary = Orange40,
    onSecondary = Grey99,
    secondaryContainer = Orange80,
    onSecondaryContainer = Grey10,

    tertiary = Success,
    error = Error,

    background = Grey99,
    onBackground = Grey10,
    surface = Grey95,
    onSurface = Grey10
)

@Composable
fun TapSaveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}