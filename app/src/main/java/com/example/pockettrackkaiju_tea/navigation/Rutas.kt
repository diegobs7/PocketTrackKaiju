package com.example.pockettrackkaiju_tea.navigation

sealed class Rutas(val ruta: String) {
    object InicioSesion : Rutas("inicio_sesion")
    object RegistroUsuario : Rutas("registro_usuario")
    object Inicio : Rutas("inicio")
    object Catalogo : Rutas("catalogo")

    object Movimiento : Rutas("movimiento")
    object Alerta : Rutas("alerta")


    object RegistroProducto: Rutas("registroProducto")

    object EditarProducto : Rutas("editarProducto/{productoId}") {
        fun crearRuta(productoId: Int) = "editarProducto/$productoId"
    }

}