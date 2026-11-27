package com.example.pockettrackkaiju_tea.remote.kaiju

import com.example.pockettrackkaiju_tea.util.ConfigServidor
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit del backend. Hay una sola instancia de Retrofit y la dirección del
 * servidor se reemplaza en cada petición con la que guarda [ConfigServidor], de modo que
 * cambiar la URL en el login tiene efecto de inmediato, sin reconstruir nada.
 *
 * @param token entrega el token JWT vigente, o null si no hay sesión (login y registro).
 */
class KaijuClient(
    private val config: ConfigServidor,
    private val token: () -> String?
) {

    val api: KaijuApi by lazy {
        val clienteHttp = OkHttpClient.Builder()
            .addInterceptor(interceptorDeServidor())
            .addInterceptor(interceptorDeToken())
            .connectTimeout(10, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("http://localhost/") // Marcador: interceptorDeServidor() pone la URL real.
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KaijuApi::class.java)
    }

    private fun interceptorDeServidor() = Interceptor { cadena ->
        val original = cadena.request()
        val destino = config.urlBase().toHttpUrlOrNull()
        val peticion = if (destino == null) original else {
            val nueva = original.url.newBuilder()
                .scheme(destino.scheme)
                .host(destino.host)
                .port(destino.port)
                .build()
            original.newBuilder().url(nueva).build()
        }
        cadena.proceed(peticion)
    }

    private fun interceptorDeToken() = Interceptor { cadena ->
        val jwt = token()
        val peticion = if (jwt == null) cadena.request() else {
            cadena.request().newBuilder()
                .header("Authorization", "Bearer $jwt")
                .build()
        }
        cadena.proceed(peticion)
    }
}
