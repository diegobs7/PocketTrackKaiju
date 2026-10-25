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
    primary = RosadoClaroKaiju,
    onPrimary = TextoSobreRosadoKaiju,
    primaryContainer = RosadoContenedorKaiju,
    onPrimaryContainer = RosadoPaliKaiju,

    secondary = LilaKaiju,
    onSecondary = TextoSobreLilaKaiju,
    secondaryContainer = LilaContenedorKaiju,
    onSecondaryContainer = LilaPaliKaiju,

    tertiary = AmarilloKaiju,
    onTertiary = Color.Black,
    tertiaryContainer = AmbarKaiju,
    onTertiaryContainer = Color.Black,

    background = FondoOscuroKaiju,
    onBackground = TextoClaroKaiju,

    surface = SuperficieOscuraKaiju,
    onSurface = TextoClaroKaiju,
    surfaceVariant = TarjetaOscuraKaiju,
    onSurfaceVariant = TextoSecundarioClaroKaiju,

    surfaceContainerLowest = Color(0xFF0A070D),
    surfaceContainerLow = Color(0xFF150F1D),
    surfaceContainer = SuperficieOscuraKaiju,
    surfaceContainerHigh = Color(0xFF221A2D),
    surfaceContainerHighest = TarjetaOscuraKaiju,

    outline = Color(0xFF8A7A9B),
    outlineVariant = Color(0xFF3A2D4A)
)

private val LightColorScheme = lightColorScheme(
    primary = MoradoKaiju,
    onPrimary = TextoBlanco,
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = MoradoOscuroKaiju,

    secondary = RosadoProfundoKaiju,
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
    onSurfaceVariant = MoradoOscuroKaiju,

    surfaceContainerLowest = TextoBlanco,
    surfaceContainerLow = Color(0xFFF9F3FC),
    surfaceContainer = SuperficieClaraKaiju,
    surfaceContainerHigh = Color(0xFFEFE6F5),
    surfaceContainerHighest = Color(0xFFE7DFEC),

    outline = Color(0xFF7C6B8C),
    outlineVariant = Color(0xFFD3C6DF)
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