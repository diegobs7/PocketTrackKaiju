package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.Usuario

@Composable
fun TarjetaUsuario(
    usuario: Usuario,
    esElMismo: Boolean,
    onCambiarRol: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (esElMismo) "${usuario.nombre} (tú)" else usuario.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = usuario.correo,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rol: ${etiquetaRol(usuario.rol)}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (!esElMismo) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCambiarRol) {
                        Text("Cambiar rol")
                    }
                    TextButton(onClick = onEliminar) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}