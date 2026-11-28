package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.viewmodel.DolarUiState
import java.util.Locale

@Composable
fun BannerDolar(
    estado: DolarUiState,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when {
                estado.cargando -> {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp), strokeWidth = 2.dp)
                    Text("Consultando el dólar...", style = MaterialTheme.typography.bodyMedium)
                }
                estado.dolar != null -> Text(
                    text = "Dólar hoy: \$${formatearPesos(estado.dolar.pesos)} CLP · ${formatearFecha(estado.dolar.fecha)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color= MaterialTheme.colorScheme.onPrimaryContainer
                )
                else -> {
                    Text(
                        text = estado.error ?: "Dólar no disponible.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onReintentar) { Text("Reintentar") }
                }
            }
        }
    }
}

private fun formatearPesos(valor: Double): String =
    String.format(Locale.forLanguageTag("es-CL"), "%,.2f", valor)

fun formatearUsd(precioClp: Int, dolarPesos: Double): String =
    "≈ US$ " + String.format(Locale.forLanguageTag("es-CL"), "%,.2f", precioClp / dolarPesos)

private fun formatearFecha(iso: String): String =
    iso.split("-").reversed().joinToString("-")