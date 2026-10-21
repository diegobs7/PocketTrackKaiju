package com.example.pockettrackkaiju_tea.model

data class FormularioProducto(
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val tipo: String = "",
    val precio: String = "",
    val stockActual: String = "",
    val stockMinimo: String = "",
    val detalle: String = "",
    val errores: ErroresProducto = ErroresProducto()

)