package com.example.pockettrackkaiju_tea.viewmodel

import com.example.pockettrackkaiju_tea.model.PokemonDto

data class EasterEggUiState(
    val visible: Boolean = false,
    val consulta: String = "",
    val cargando: Boolean = false,
    val pokemon: PokemonDto? = null,
    val error: String? = null
)