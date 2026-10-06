package com.example.pockettrackkaiju_tea.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // 1. El Scaffold ahora es el contenedor principal (global)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PocketTrack Kaiju") },
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
                    onClick = { navController.navigate(Rutas.Inicio.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Producto.ruta,
                    onClick = {navController.navigate(Rutas.Producto.ruta) {launchSingleTop = true} },
                    icon = {Icon(Icons.Filled.Store, contentDescription = "Productos")},
                    label = {Text("Productos")}
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Perfil.ruta,
                    onClick = { navController.navigate(Rutas.Perfil.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        // 2. El NavHost va adentro, ocupando solo el espacio libre del Scaffold
        NavHost(
            navController = navController,
            startDestination = Rutas.Inicio.ruta,
            modifier = Modifier.padding(padding) // Esto evita que las barras tapen el contenido
        ) {

            composable(Rutas.Inicio.ruta) {
                // Aquí ya no necesitas llamar a un Scaffold, solo el contenido directo de Inicio
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Inicio")
                }
            }

            composable(Rutas.Perfil.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Perfil en desarrollo")
                }
            }
            composable(Rutas.Producto.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Productos en desarrollo")
                }
            }
        }
    }
}