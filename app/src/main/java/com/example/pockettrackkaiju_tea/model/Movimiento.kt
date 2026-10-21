package com.example.pockettrackkaiju_tea.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoMovimiento {
    ENTRADA,
    SALIDA_VENTA,
    SALIDA_CONSUMO,
    AJUSTE
}

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigoProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String,
    val fecha: String,
    val responsable: String,
    val responsableId: Int
)


