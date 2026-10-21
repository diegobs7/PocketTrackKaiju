package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.Producto

@Composable
fun TarjetaAlerta(
    producto: Producto,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Código: ${producto.codigo}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = if (producto.stockActual == 0) "Agotado" else "Stock actual: ${producto.stockActual}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Stock mínimo: ${producto.stockMinimo}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}