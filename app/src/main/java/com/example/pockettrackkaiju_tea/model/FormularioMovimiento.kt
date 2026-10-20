package com.example.pockettrackkaiju_tea.model

data class FormularioMovimiento(
    val producto: Producto? = null,
    val tipo: TipoMovimiento? = null,
    val cantidad: String = "",
    val motivo: String = "",
    val responsable: Usuario? = null,
    val errores: ErroresMovimiento = ErroresMovimiento()
)