package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario

@Composable
fun DialogoCambiarRol(
    usuario: Usuario,
    onGuardar: (Rol) -> Unit,
    onCerrar: () -> Unit
) {
    var rolElegido by remember { mutableStateOf(usuario.rol) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Cambiar rol") },
        text = {
            Column {
                Text("Usuario: ${usuario.nombre}")
                Spacer(Modifier.height(8.dp))
                Rol.entries.forEach { rol ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .selectable(
                                selected = rolElegido == rol,
                                onClick = { rolElegido = rol },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = rolElegido == rol, onClick = null)
                        Spacer(Modifier.width(8.dp))
                        Text(etiquetaRol(rol))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(rolElegido) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )
}