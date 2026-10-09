package com.example.pockettrackkaiju_tea.model

data class Producto (
    val codigo: String,
    val nombre : String,
    val descripcion:String,
    val categoria: String,
    val tipo: String,
    val precio: Double,
    val stockActual: Int,
    val stockMinimo: Int,
    val detalle: String?=null

)