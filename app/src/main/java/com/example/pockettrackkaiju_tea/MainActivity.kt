package com.example.pockettrackkaiju_tea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInventario
import com.example.pockettrackkaiju_tea.ui.theme.PocketTrackKaiju_TEATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketTrackKaiju_TEATheme {
                PantallaInventario()
            }
        }
    }
}
