package com.example.bitcoinstore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BitcoinOrange,
    onPrimary = Black,
    secondary = BitcoinOrange,
    onSecondary = Black,
    tertiary = SuccessGreen,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

private val DarkColors = darkColorScheme(
    primary = BitcoinOrange,
    onPrimary = White,
    secondary = BitcoinOrange,
    onSecondary = White,
    tertiary = SuccessGreen,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White,
)

@Composable
fun BitcoinStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )

}
