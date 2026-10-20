package com.example.pockettrackkaiju_tea.model

data class ErroresMovimiento(
    val errorProducto: String? = null,
    val errorTipo: String? = null,
    val errorCantidad: String? = null,
    val errorMotivo: String? = null,
    val errorResponsable: String? = null
)