package com.example.pockettrackkaiju_tea.model

enum class Rol{
    ADMINISTRADOR,
    VENDEDOR,
    ENCARGADO_INVENTARIO

}

data class Usuario(
    val id:String,
    val nombre:String,
    val rol: Rol
)
