package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.model.VentaCategoria
import com.example.pockettrackkaiju_tea.ui.components.TarjetaVenta
import com.example.pockettrackkaiju_tea.util.CalculadoraVentas
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaVentas(viewModel: InventarioViewModel) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val ventas = remember(productos, movimientos) {
        CalculadoraVentas.ventasPorProducto(productos, movimientos)
    }
    val porCategoria = remember(ventas) { CalculadoraVentas.ventasPorCategoria(ventas) }

    if (ventas.none { it.unidades > 0 }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Aún no hay ventas registradas.",
                textAlign = TextAlign.Center
            )
        }
    } else {
        val masVendidos = ventas.filter { it.unidades > 0 }.take(5)
        val menosVendidos = ventas.sortedBy { it.unidades }.take(5)
        val maximoCategoria = porCategoria.maxOfOrNull { it.unidades } ?: 1

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Más vendidos", style = MaterialTheme.typography.titleMedium)
            }
            itemsIndexed(masVendidos, key = { _, venta -> "mas-${venta.producto.id}" }) { indice, venta ->
                TarjetaVenta(venta = venta, posicion = indice + 1)
            }

            item {
                Text("Menos vendidos", style = MaterialTheme.typography.titleMedium)
            }
            items(menosVendidos, key = { "menos-${it.producto.id}" }) { venta ->
                TarjetaVenta(venta = venta)
            }

            item {
                Text("Ventas por categoría", style = MaterialTheme.typography.titleMedium)
            }
            items(porCategoria, key = { "cat-${it.categoria.name}" }) { fila ->
                FilaVentaCategoria(venta = fila, maximo = maximoCategoria)
            }
        }
    }
}

@Composable
private fun FilaVentaCategoria(venta: VentaCategoria, maximo: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(venta.categoria.displayName)
            Text("${venta.unidades} unidades")
        }
        LinearProgressIndicator(
            progress = { venta.unidades.toFloat() / maximo },
            modifier = Modifier.fillMaxWidth()
        )
    }
}