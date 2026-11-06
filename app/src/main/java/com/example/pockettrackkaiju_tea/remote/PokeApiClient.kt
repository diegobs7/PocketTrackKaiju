package com.example.pockettrackkaiju_tea.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PokeApiClient {
    val api: PokeApi by lazy {
        val clienteHttp = OkHttpClient.Builder()
            .callTimeout(20, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // La barra final es necesaria.
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }
}