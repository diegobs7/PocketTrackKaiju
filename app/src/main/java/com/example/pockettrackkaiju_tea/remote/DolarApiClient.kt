package com.example.pockettrackkaiju_tea.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DolarApiClient {
    val api: DolarApi by lazy {
        val clienteHttp = OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://mindicador.cl/api/")
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DolarApi::class.java)
    }
}
