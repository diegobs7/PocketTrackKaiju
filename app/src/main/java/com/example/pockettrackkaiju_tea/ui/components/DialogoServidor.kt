package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

/** Cuadro del engranaje del login: cambia la dirección del backend sin recompilar la app. */
@Composable
fun DialogoServidor(
    url: String,
    error: String?,
    onCambiarUrl: (String) -> Unit,
    onGuardar: () -> Unit,
    onRestablecer: () -> Unit,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Servidor") },
        text = {
            Column {
                Text("Dirección del backend, por ejemplo 54.12.34.56:8080.")
                OutlinedTextField(
                    value = url,
                    onValueChange = onCambiarUrl,
                    label = { Text("Dirección") },
                    leadingIcon = { Icon(Icons.Filled.Dns, contentDescription = null) },
                    isError = error != null,
                    supportingText = if (error != null) {
                        { Text(error) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    singleLine = true
                )
            }
        },
        confirmButton = { TextButton(onClick = onGuardar) { Text("Guardar") } },
        dismissButton = {
            Column {
                TextButton(onClick = onRestablecer) { Text("Restablecer") }
                TextButton(onClick = onCerrar) { Text("Cancelar") }
            }
        }
    )
}
