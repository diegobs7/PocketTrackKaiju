package com.example.pockettrackkaiju_tea.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInicioSesion
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiNavHost(
    loginViewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val rutasSinBarra = listOf(Rutas.InicioSesion.ruta, Rutas.RegistroUsuario.ruta)
    val mostrarBarraInferior = rutaActual != null && rutaActual !in rutasSinBarra
    val mostrarBotonAtras = rutaActual == Rutas.RegistroUsuario.ruta
    val navegarAPestana: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.Inicio.ruta) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("PocketTrack Kaiju")},
                navigationIcon = {
                    if (mostrarBotonAtras) {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (mostrarBarraInferior) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Inicio.ruta,
                        onClick = { navegarAPestana(Rutas.Inicio.ruta) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio")}
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catalogo.ruta,
                        onClick = { navegarAPestana(Rutas.Catalogo.ruta) },
                        icon = { Icon(Icons.Filled.Store, contentDescription = "Productos") },
                        label = { Text("Catalogo")}
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = {navegarAPestana(Rutas.Movimiento.ruta)},
                        icon = {Icon(Icons.Filled.Moving, contentDescription = "Movimiento")},
                        label = {Text("Movimiento")}
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Alerta.ruta,
                        onClick = {navegarAPestana(Rutas.Alerta.ruta)},
                        icon = {Icon(Icons.Filled.AddAlert, contentDescription = "Alerta")},
                        label = { Text("Alertas")}
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Rutas.InicioSesion.ruta,
            modifier = Modifier.padding(padding),
        ) {

            composable(
                route = Rutas.InicioSesion.ruta
            ) {
                PantallaInicioSesion(
                    viewModel = loginViewModel,
                    onLoginExitoso = {
                        navController.navigate(Rutas.Inicio.ruta) {
                            popUpTo(Rutas.InicioSesion.ruta) { inclusive = true }
                        }
                    },
                    onIrRegistro = { navController.navigate(Rutas.RegistroUsuario.ruta) }
                )
            }
            composable(Rutas.RegistroUsuario.ruta) {
                PantallaRegistroUsuario(
                    onRegistroExitoso = {navController.popBackStack()}
                )
            }
            composable(Rutas.Inicio.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pantalla de Inicio")
                }
            }

            composable(Rutas.Catalogo.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Catalogo en desarrollo")
                }
            }
            composable(Rutas.Movimiento.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Movimiento en desarrollo")
                }
            }
            composable(Rutas.Alerta.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Alertas en desarrollo")
                }
            }
        }
    }
}