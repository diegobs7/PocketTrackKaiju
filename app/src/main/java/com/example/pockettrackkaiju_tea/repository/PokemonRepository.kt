package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.PokemonDto
import com.example.pockettrackkaiju_tea.remote.PokeApi
import java.util.Locale

class PokemonRepository(private val api: PokeApi) {

    private val cache = mutableMapOf<String, PokemonDto>()

    suspend fun buscar(entrada: String): PokemonDto {
        val clave = entrada.trim().lowercase(Locale.ROOT)
        cache[clave]?.let { return it }

        val pokemon = api.obtenerPokemon(clave)
        cache[clave] = pokemon
        cache[pokemon.name] = pokemon
        cache[pokemon.id.toString()] = pokemon
        return pokemon
    }
}