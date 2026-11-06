package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pockettrackkaiju_tea.model.PokemonDto
import com.example.pockettrackkaiju_tea.viewmodel.EasterEggUiState

@Composable
fun DialogoPokemon(
    estado: EasterEggUiState,
    onConsultaChange: (String) -> Unit,
    onBuscar: () -> Unit,
    onOtroAleatorio: () -> Unit,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("¡Pokémon sorpresa!") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = estado.consulta,
                    onValueChange = onConsultaChange,
                    label = { Text("Nombre o número") },
                    singleLine = true,
                    enabled = !estado.cargando,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onBuscar, enabled = !estado.cargando) { Text("Buscar") }
                    OutlinedButton(onClick = onOtroAleatorio, enabled = !estado.cargando) {
                        Text("Al azar")
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when {
                        estado.cargando -> {
                            CircularProgressIndicator()
                            Text("Consultando PokéAPI…")
                        }
                        estado.error != null -> {
                            Text(estado.error, color = MaterialTheme.colorScheme.error)
                            TextButton(onClick = onBuscar) { Text("Reintentar") }
                        }
                        estado.pokemon != null -> FichaPokemon(estado.pokemon)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onCerrar) { Text("Cerrar") } }
    )
}

@Composable
private fun FichaPokemon(pokemon: PokemonDto) {
    Text(
        "#${pokemon.id} · ${pokemon.name.replaceFirstChar { it.uppercase() }}",
        style = MaterialTheme.typography.titleLarge
    )
    if (pokemon.sprites.frontDefault == null) {
        Text("Imagen no disponible")
    } else {
        AsyncImage(
            model = pokemon.sprites.frontDefault,
            contentDescription = "Imagen de ${pokemon.name}",
            error = painterResource(android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(160.dp)
        )
    }
    Text("Tipos: " + pokemon.types.sortedBy { it.slot }.joinToString { it.type.name })
    Text("Altura: ${pokemon.height / 10.0} m")
    Text("Peso: ${pokemon.weight / 10.0} kg")
}