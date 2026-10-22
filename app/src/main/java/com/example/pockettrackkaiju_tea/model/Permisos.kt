package com.example.pockettrackkaiju_tea.model

fun Rol.puedeAgregarProductos(): Boolean = this == Rol.ADMINISTRADOR

fun Rol.puedeVerAlertas(): Boolean = this != Rol.VENDEDOR

fun Rol.tiposDeMovimientoPermitidos(): List<TipoMovimiento> = when (this) {
    Rol.ADMINISTRADOR -> TipoMovimiento.entries
    Rol.VENDEDOR -> listOf(TipoMovimiento.SALIDA_VENTA)
    Rol.ENCARGADO_INVENTARIO -> listOf(
        TipoMovimiento.ENTRADA,
        TipoMovimiento.SALIDA_CONSUMO,
        TipoMovimiento.AJUSTE
    )
}
fun Rol.puedeGestionarProductos(): Boolean = this == Rol.ADMINISTRADOR

fun Rol.puedeVerVentas(): Boolean = this == Rol.ADMINISTRADOR