package com.example.pockettrackkaiju_tea.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pockettrackkaiju_tea.ui.screen.PantallaAlertas
import com.example.pockettrackkaiju_tea.ui.screen.PantallaCatalogo
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInicio
import com.example.pockettrackkaiju_tea.ui.screen.PantallaInicioSesion
import com.example.pockettrackkaiju_tea.ui.screen.PantallaMovimientos
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroProducto
import com.example.pockettrackkaiju_tea.ui.screen.PantallaRegistroUsuario
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel
import com.example.pockettrackkaiju_tea.viewmodel.SesionViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.model.puedeAgregarProductos
import com.example.pockettrackkaiju_tea.model.puedeGestionarProductos
import com.example.pockettrackkaiju_tea.model.puedeVerAlertas
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material.icons.filled.Group
import com.example.pockettrackkaiju_tea.model.puedeGestionarUsuarios
import com.example.pockettrackkaiju_tea.ui.screen.PantallaUsuarios

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiNavHost(
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    sesionViewModel: SesionViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    inventarioViewModel: InventarioViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val cargando by sesionViewModel.cargando.collectAsStateWithLifecycle()
    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val usuario by sesionViewModel.usuarioActual.collectAsStateWithLifecycle()
    val rol = usuario?.rol
    val destinoInicial = remember {
        if (sesionViewModel.usuarioActual.value != null) Rutas.Inicio.ruta else Rutas.InicioSesion.ruta
    }

    val rutasSinBarra = listOf(
        Rutas.InicioSesion.ruta, Rutas.RegistroUsuario.ruta,
        Rutas.RegistroProducto.ruta, Rutas.EditarProducto.ruta
    )
    val rutasConBotonAtras = listOf(
        Rutas.RegistroUsuario.ruta, Rutas.RegistroProducto.ruta, Rutas.EditarProducto.ruta
    )
    val mostrarBotonAtras = rutaActual in rutasConBotonAtras
    val mostrarBarraInferior = rutaActual != null && rutaActual !in rutasSinBarra


    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    val navegarAPestana: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.Inicio.ruta) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }


    if (mostrarDialogoSalir) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoSalir = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoSalir = false
                        sesionViewModel.cerrarSesion()
                        navController.navigate(Rutas.InicioSesion.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Sí, salir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoSalir = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PocketTrack Kaiju") },
                navigationIcon = {
                    if (mostrarBotonAtras) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    } else if (mostrarBarraInferior) {

                        IconButton(onClick = { mostrarDialogoSalir = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar sesión"
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
                        onClick = { navegarAPestana(Rutas.Movimiento.ruta) },
                        icon = { Icon(Icons.Filled.Moving, contentDescription = "Movimiento") },
                        label = {
                            Text(
                                text = "Movimiento",
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    if(rol?.puedeVerAlertas() == true) {
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Alerta.ruta,
                            onClick = { navegarAPestana(Rutas.Alerta.ruta) },
                            icon = { Icon(Icons.Filled.AddAlert, contentDescription = "Alerta") },
                            label = {
                                Text(
                                    text = "Alertas",
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                    if (rol?.puedeGestionarUsuarios() == true) {
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Usuarios.ruta,
                            onClick = { navegarAPestana(Rutas.Usuarios.ruta) },
                            icon = { Icon(Icons.Filled.Group, contentDescription = "Usuarios") },
                            label = {
                                Text(
                                    text = "Usuarios",
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (rutaActual == Rutas.Catalogo.ruta && rol?.puedeAgregarProductos() == true) {
                FloatingActionButton(
                    onClick = { navController.navigate(Rutas.RegistroProducto.ruta) }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Producto")
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = destinoInicial,
            modifier = Modifier.padding(padding),
        ) {
            composable(route = Rutas.InicioSesion.ruta) {
                PantallaInicioSesion(
                    viewModel = loginViewModel,
                    onLoginExitoso = { usuarioLogueado, recordar ->
                        sesionViewModel.iniciarSesion(usuarioLogueado, recordar)
                        navController.navigate(Rutas.Inicio.ruta) {
                            popUpTo(Rutas.InicioSesion.ruta) { inclusive = true }
                        }
                    },
                    onIrRegistro = { navController.navigate(Rutas.RegistroUsuario.ruta) }
                )
            }
            composable(Rutas.RegistroUsuario.ruta) {
                PantallaRegistroUsuario(
                    onRegistroExitoso = { navController.popBackStack() }
                )
            }
            composable(
                route = Rutas.EditarProducto.ruta,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { entrada ->
                PantallaRegistroProducto(
                    productoId = entrada.arguments?.getInt("productoId"),
                    onRegistroExitoso = { navController.popBackStack() }
                )
            }
            composable(Rutas.Inicio.ruta) {
                usuario?.let { PantallaInicio(inventarioViewModel, it) }
            }
            composable(Rutas.Catalogo.ruta) {
                PantallaCatalogo(
                    viewModel = inventarioViewModel,
                    esAdministrador = rol?.puedeGestionarProductos() == true,
                    onEditar = { producto ->
                        navController.navigate(Rutas.EditarProducto.crearRuta(producto.id))
                    }
                )
            }
            composable(Rutas.Movimiento.ruta) {
                usuario?.let { PantallaMovimientos(inventarioViewModel, it) }
            }
            composable(Rutas.Alerta.ruta) {
                PantallaAlertas(inventarioViewModel)
            }
            composable(Rutas.RegistroProducto.ruta) {
                PantallaRegistroProducto(
                    onRegistroExitoso = { navController.popBackStack() }
                )
            }
            composable(Rutas.Usuarios.ruta) {
                usuario?.let { PantallaUsuarios(it) }
            }
        }
    }
}