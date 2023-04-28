package com.example.ofiu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = WhiteGris,
    primaryVariant = Blue,
    secondary = WhiteBlue,
    secondaryVariant = DarkGris,
    background = DarkBlue,
    surface = ReWhiteGris,
    onSurface = WhiteSGris,
    onPrimary = White,
    onSecondary = Black,
    onBackground = MediumGris
)

private val LightColorPalette = lightColors(
    primary = WhiteGris,
    primaryVariant = Blue,
    secondary = WhiteBlue,
    secondaryVariant = DarkGris,
    background = DarkBlue,
    surface = ReWhiteGris,
    onSurface = WhiteSGris,
    onPrimary = White,
    onSecondary = Black,
    onBackground = MediumGris
)

@Composable
fun OfiuTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}