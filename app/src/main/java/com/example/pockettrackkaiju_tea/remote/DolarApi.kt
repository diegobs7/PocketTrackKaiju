package com.example.pockettrackkaiju_tea.remote

import com.example.pockettrackkaiju_tea.model.DolarDto
import retrofit2.http.GET

interface DolarApi {
    @GET("dolar")
    suspend fun obtenerDolar(): DolarDto
}
