package com.example.pockettrackkaiju_tea.model

data class FormularioEditarUsuario(
    val nombre: String = "",
    val email: String = "",
    val rol: Rol = Rol.VENDEDOR,
    val cargando: Boolean = false,
    val errores: ErroresEditarUsuario = ErroresEditarUsuario()
)