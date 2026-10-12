package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pockettrackkaiju_tea.navigation.Rutas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PocketTrack Kaiju")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rutaActual == Rutas.Inicio.ruta,
                    onClick = {
                        navController.navigate(Rutas.Inicio.ruta) {launchSingleTop = true}
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = {Text("Inicio")}
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Login.ruta,
                    onClick = {
                        navController.navigate(Rutas.Login.ruta) { launchSingleTop = true}
                    },
                    icon = {Icon(Icons.Filled.Person, contentDescription = "Perfil")},
                    label = {Text("Perfil")}
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        ) {
            // Contenido principal de la pantalla Inicio
        }
    }

}