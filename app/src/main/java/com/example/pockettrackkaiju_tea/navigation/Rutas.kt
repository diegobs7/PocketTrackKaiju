package com.example.pockettrackkaiju_tea.navigation

sealed class Rutas(val ruta: String) {
    object InicioSesion : Rutas("inicio_sesion")
    object Inicio : Rutas("inicio")
    object Perfil : Rutas("perfil")
    object Producto : Rutas("producto")
}