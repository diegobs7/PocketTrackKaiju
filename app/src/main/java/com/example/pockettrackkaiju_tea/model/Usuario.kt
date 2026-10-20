package com.example.pockettrackkaiju_tea.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Rol{
    ADMINISTRADOR,
    VENDEDOR,
    ENCARGADO_INVENTARIO

}

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val rol: Rol
)