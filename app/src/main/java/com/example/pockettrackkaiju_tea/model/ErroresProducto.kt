package com.example.pockettrackkaiju_tea.model

data class ErroresProducto(
    val errorCodigo: String? = null,     // null = "está bien"
    val errorNombre: String? = null,
    val errorDescripcion: String? = null,
    val errorCategoria: String? = null,
    val errorTipo: String? = null,
    val errorPrecio: String? = null,
    val errorStockActual: String? = null
)