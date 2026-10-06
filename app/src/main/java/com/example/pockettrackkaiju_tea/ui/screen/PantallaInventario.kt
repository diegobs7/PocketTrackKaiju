package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.ui.components.TarjetaProducto

private val productosEjemplo = listOf(
    Producto(nombre = "Polera Ben 10", categoria = "Ropa"),
    Producto(nombre = "Pantalon Dragon Ball Z", categoria = "Ropa"),
    Producto(nombre = "Chaqueta One Piece", categoria = "Ropa")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInventario() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PocketTrack Kaiju")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(productosEjemplo) {producto ->
                TarjetaProducto(
                    nombre = producto.nombre,
                    categoria = producto.categoria
                )
            }
        }
    }

}