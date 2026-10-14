package com.example.pockettrackkaiju_tea.model

data class Producto (
    val codigo: String,
    val nombre : String,
    val descripcion:String,
    val categoria: String,
    val tipo: String,
    val precio: Int,
    val stockActual: Int,
    val stockMinimo: Int = 0,
    val detalle: String?=null

)