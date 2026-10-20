package com.example.pockettrackkaiju_tea.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pockettrackkaiju_tea.database.AppDatabase
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository

class AppViewModelFactory(contexto: Context) : ViewModelProvider.Factory {

    private val database = AppDatabase.getInstance(contexto)
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
            MovimientoViewModel(productoRepository, movimientoRepository, usuarioRepository)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}