package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaMovimientos(inventarioViewModel: InventarioViewModel) {
    var pestanaSeleccionada by remember { mutableStateOf(0) }
    val titulos = listOf("Registrar", "Historial")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestanaSeleccionada) {
            titulos.forEachIndexed { indice, titulo ->
                Tab(
                    selected = pestanaSeleccionada == indice,
                    onClick = { pestanaSeleccionada = indice },
                    text = { Text(titulo) }
                )
            }
        }

        when (pestanaSeleccionada) {
            0 -> PantallaMovimiento()
            else -> PantallaHistorialMovimientos(inventarioViewModel)
        }
    }
}