package com.example.pockettrackkaiju_tea.remote

import com.example.pockettrackkaiju_tea.model.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(@Path("nombre") nombre: String): PokemonDto
}