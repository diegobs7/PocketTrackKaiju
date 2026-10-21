package com.example.pockettrackkaiju_tea.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RosadoKaiju,
    onPrimary = TextoBlanco,
    primaryContainer = MoradoOscuroKaiju,
    onPrimaryContainer = TextoBlanco,

    secondary = MoradoKaiju,
    onSecondary = TextoBlanco,
    secondaryContainer = TarjetaOscuraKaiju,
    onSecondaryContainer = TextoBlanco,

    tertiary = AmarilloKaiju,
    onTertiary = Color.Black,
    tertiaryContainer = AmbarKaiju,
    onTertiaryContainer = Color.Black,

    background = FondoOscuroKaiju,
    onBackground = TextoBlanco,

    surface = SuperficieOscuraKaiju,
    onSurface = TextoBlanco,
    surfaceVariant = TarjetaOscuraKaiju,
    onSurfaceVariant = TextoBlanco
)

private val LightColorScheme = lightColorScheme(
    primary = MoradoKaiju,
    onPrimary = TextoBlanco,
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = MoradoOscuroKaiju,

    secondary = RosadoKaiju,
    onSecondary = TextoBlanco,
    secondaryContainer = Color(0xFFFFD9E4),
    onSecondaryContainer = Color(0xFF8C0B48),

    tertiary = AmarilloKaiju,
    onTertiary = Color.Black,
    tertiaryContainer = AmbarKaiju,
    onTertiaryContainer = Color.Black,

    background = TextoBlanco,
    onBackground = Color(0xFF141018),

    surface = SuperficieClaraKaiju,
    onSurface = Color(0xFF141018),
    surfaceVariant = Color(0xFFE7DFEC),
    onSurfaceVariant = MoradoOscuroKaiju
)

@Composable
fun PocketTrackKaiju_TEATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}