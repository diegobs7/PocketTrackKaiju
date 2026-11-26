package com.example.pockettrackkaiju_tea.model

enum class Rol {
    ADMINISTRADOR,
    VENDEDOR,
    ENCARGADO_INVENTARIO
}

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: Rol
)