package com.example.pockettrackkaiju_tea.model

data class FormularioRegistroUsuario(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val rol: Rol = Rol.VENDEDOR,
    val cargando: Boolean = false,
    val errores: ErroresRegistroUsuario = ErroresRegistroUsuario()
)