package com.example.pockettrackkaiju_tea.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pockettrackkaiju_tea.database.AppDatabase
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.util.SesionGuardada

class AppViewModelFactory(contexto: Context) : ViewModelProvider.Factory {

    private val appContext = contexto.applicationContext

    private val database = AppDatabase.getInstance(appContext)
    private val productoRepository = ProductoRepository(database.productoDao())
    private val movimientoRepository = MovimientoRepository(database.movimientoDao())
    private val usuarioRepository = UsuarioRepository(database.usuarioDao())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProductoViewModel::class.java) ->
            ProductoViewModel(productoRepository)

        modelClass.isAssignableFrom(InventarioViewModel::class.java) ->
            InventarioViewModel(productoRepository, movimientoRepository)

        modelClass.isAssignableFrom(MovimientoViewModel::class.java) ->
            MovimientoViewModel(productoRepository, movimientoRepository)

        modelClass.isAssignableFrom(RegistroUsuarioViewModel::class.java) ->
            RegistroUsuarioViewModel(usuarioRepository)

        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(usuarioRepository)

        modelClass.isAssignableFrom(SesionViewModel::class.java) ->
            SesionViewModel(usuarioRepository, SesionGuardada(appContext))
        modelClass.isAssignableFrom(UsuariosViewModel::class.java) ->
            UsuariosViewModel(usuarioRepository, movimientoRepository)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}