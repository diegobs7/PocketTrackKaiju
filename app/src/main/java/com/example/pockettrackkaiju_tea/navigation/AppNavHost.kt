package com.example.pockettrackkaiju_tea.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import com.example.pockettrackkaiju_tea.ui.screen.PantallaPerfilUsuario
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroUsuario
import androidx.navigation.NavGraph.Companion.findStartDestination


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
    val mostrarBotonAtras = rutaActual in rutasSinBarra
    val navegarAPestana: (String) -> Unit = {ruta -> navController.navigate(ruta) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    } }

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
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Producto.ruta,
                        onClick = { navegarAPestana(Rutas.Producto.ruta) },
                        icon = { Icon(Icons.Filled.Store, contentDescription = "Productos") },
                        label = { Text("Productos") }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Perfil.ruta,
                        onClick = { navegarAPestana(Rutas.Perfil.ruta) },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Rutas.Inicio.ruta,
            modifier = Modifier.padding(padding)
        ) {

            composable(Rutas.InicioSesion.ruta) {
                PantallaInicioSesion(
                    viewModel = loginViewModel,
                    onLoginExitoso = {navController.popBackStack() },
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

            composable(Rutas.Perfil.ruta) {
                PantallaPerfilUsuario(
                    onIrInicioSesion = {navController.navigate(Rutas.InicioSesion.ruta)}
                )
            }
            composable(Rutas.Producto.ruta) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Pantalla de Productos en desarrollo")
                }
            }
        }
    }
}