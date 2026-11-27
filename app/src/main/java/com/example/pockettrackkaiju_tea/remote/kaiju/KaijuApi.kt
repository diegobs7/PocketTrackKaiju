package com.example.pockettrackkaiju_tea.remote.kaiju

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KaijuApi {

    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @POST("api/v1/auth/register")
    suspend fun registrar(@Body body: RegistroRequest): UsuarioResponse

    @GET("api/v1/auth/me")
    suspend fun perfil(): UsuarioResponse

    @GET("api/v1/usuarios")
    suspend fun listar(): List<UsuarioResponse>

    @GET("api/v1/usuarios/{id}")
    suspend fun obtener(@Path("id") id: Long): UsuarioResponse

    @POST("api/v1/usuarios")
    suspend fun crear(@Body body: CrearUsuarioRequest): UsuarioResponse

    @PUT("api/v1/usuarios/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body body: ActualizarUsuarioRequest
    ): UsuarioResponse

    @PATCH("api/v1/usuarios/{id}/rol")
    suspend fun cambiarRol(
        @Path("id") id: Long,
        @Body body: CambiarRolRequest
    ): UsuarioResponse

    /** Borrado permanente (DELETE /{id} solo desactiva). */
    @DELETE("api/v1/usuarios/{id}/eliminar")
    suspend fun eliminar(@Path("id") id: Long)
}
