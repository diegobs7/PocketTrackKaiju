package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.model.puedeVerVentas
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaMovimientos(inventarioViewModel: InventarioViewModel, usuario: Usuario) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    val titulos = if (usuario.rol.puedeVerVentas()) {
        listOf("Registrar", "Historial", "Ventas")
    } else {
        listOf("Registrar", "Historial")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = pestanaSeleccionada) {
            titulos.forEachIndexed { indice, titulo ->
                Tab(
                    selected = pestanaSeleccionada == indice,
                    onClick = { pestanaSeleccionada = indice },
                    text = { Text(titulo) }
                )
            }
        }

        when (pestanaSeleccionada) {
            0 -> PantallaMovimiento(usuario)
            1 -> PantallaHistorialMovimientos(inventarioViewModel)
            else -> PantallaVentas(inventarioViewModel)
        }
    }
}