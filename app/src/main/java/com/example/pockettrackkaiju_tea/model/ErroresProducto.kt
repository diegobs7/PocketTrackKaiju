package com.example.pockettrackkaiju_tea.model

data class ErroresProducto(
    val errorCodigo: String? = null,
    val errorNombre: String? = null,
    val errorDescripcion: String? = null,
    val errorCategoria: String? = null,
    val errorTipo: String? = null,
    val errorPrecio: String? = null,
    val errorStockActual: String? = null,
    val errorStockMinimo: String? = null,
)