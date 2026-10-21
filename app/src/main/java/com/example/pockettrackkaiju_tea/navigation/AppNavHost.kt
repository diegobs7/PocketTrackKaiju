package com.example.pockettrackkaiju_tea.navigation


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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInicioSesion
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroUsuario
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.pockettrackkaiju_tea.ui.screen.PantallaCatalogo
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.platform.LocalContext
import com.example.pockettrackkaiju_tea.ui.screen.PantallaAlertas
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInicio
import com.example.pockettrackkaiju_tea.ui.screen.PantallaMovimientos
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroProducto
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiNavHost(
    loginViewModel: LoginViewModel = viewModel(),
    inventarioViewModel: InventarioViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val rutasSinBarra = listOf(Rutas.InicioSesion.ruta, Rutas.RegistroUsuario.ruta, Rutas.RegistroProducto.ruta)
    val rutasConBotonAtras = listOf(Rutas.RegistroUsuario.ruta, Rutas.RegistroProducto.ruta)
    val mostrarBotonAtras = rutaActual in rutasConBotonAtras
    val mostrarBarraInferior = rutaActual != null && rutaActual !in rutasSinBarra
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
                        label = {
                            Text(
                                text = "Inicio",
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catalogo.ruta,
                        onClick = { navegarAPestana(Rutas.Catalogo.ruta) },
                        icon = { Icon(Icons.Filled.Store, contentDescription = "Productos") },
                        label = {
                            Text(
                                text = "Catalogo",
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Movimiento.ruta,
                        onClick = {navegarAPestana(Rutas.Movimiento.ruta)},
                        icon = {Icon(Icons.Filled.Moving, contentDescription = "Movimiento")},
                        label = {
                            Text(
                                text = "Movimiento",
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Alerta.ruta,
                        onClick = {navegarAPestana(Rutas.Alerta.ruta)},
                        icon = {Icon(Icons.Filled.AddAlert, contentDescription = "Alerta")},
                        label = {
                            Text(
                                text = "Alertas",
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }                    )
                }
            }
        },
        floatingActionButton = {
            if (rutaActual == Rutas.Catalogo.ruta) {
                FloatingActionButton(
                    onClick = {navController.navigate(Rutas.RegistroProducto.ruta)}
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Producto")
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
                PantallaInicio(inventarioViewModel)
            }
            composable(Rutas.Catalogo.ruta) {
                PantallaCatalogo(inventarioViewModel)
            }
            composable(Rutas.Movimiento.ruta) {
                PantallaMovimientos(inventarioViewModel)
            }
            composable(Rutas.Alerta.ruta) {
                PantallaAlertas(inventarioViewModel)
            }
            composable(Rutas.RegistroProducto.ruta) {
                PantallaRegistroProducto(
                    onRegistroExitoso = { navController.popBackStack() }
                )
            }
        }
    }
}