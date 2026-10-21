package com.example.pockettrackkaiju_tea.ui.components

import com.example.pockettrackkaiju_tea.model.TipoMovimiento

fun etiquetaTipo(tipo: TipoMovimiento): String = when (tipo) {
    TipoMovimiento.ENTRADA -> "Entrada"
    TipoMovimiento.SALIDA_VENTA -> "Salida por venta"
    TipoMovimiento.SALIDA_CONSUMO -> "Salida por consumo"
    TipoMovimiento.AJUSTE -> "Ajuste"
}