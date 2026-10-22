package com.example.pockettrackkaiju_tea.model

data class VentaProducto(
    val producto: Producto,
    val unidades: Int
)

data class VentaCategoria(
    val categoria: CategoriaProducto,
    val unidades: Int
)