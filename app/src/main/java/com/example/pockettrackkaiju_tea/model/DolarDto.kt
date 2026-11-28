package com.example.pockettrackkaiju_tea.model


data class DolarDto(
    val codigo: String,
    val nombre: String,
    val serie: List<ValorDolarDto>
)

data class ValorDolarDto(
    val fecha: String,
    val valor: Double
)
