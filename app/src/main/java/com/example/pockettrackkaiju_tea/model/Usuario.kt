package com.example.pockettrackkaiju_tea.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

enum class Rol{
    ADMINISTRADOR,
    VENDEDOR,
    ENCARGADO_INVENTARIO

}

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contra: String,
    val rol: Rol
)