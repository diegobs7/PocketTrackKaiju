package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.ui.components.TarjetaProducto
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaCatalogo(viewModel: InventarioViewModel) {
    // La pantalla solo observa: si la lista cambia en el ViewModel, se redibuja sola
    val productos by viewModel.productos.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(productos, key = { it.codigo }) { producto ->
            TarjetaProducto(
                producto = producto,
                enAlerta = viewModel.estaEnAlerta(producto)
            )
        }
    }
}