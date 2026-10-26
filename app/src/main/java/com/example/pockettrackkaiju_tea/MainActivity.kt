package com.example.pockettrackkaiju_tea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pockettrackkaiju_tea.navigation.MiNavHost
import com.example.pockettrackkaiju_tea.util.NotificacionesStock
import com.example.pockettrackkaiju_tea.ui.theme.PocketTrackKaiju_TEATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        NotificacionesStock.crearCanal(this)
        setContent {
            PocketTrackKaiju_TEATheme {
                MiNavHost()
            }
        }
    }
}
