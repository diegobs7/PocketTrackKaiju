package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.model.tiposDeMovimientoPermitidos
import com.example.pockettrackkaiju_tea.ui.components.CamaraNativa
import com.example.pockettrackkaiju_tea.ui.components.ModoCamara
import com.example.pockettrackkaiju_tea.ui.components.etiquetaRol
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.MovimientoViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    usuario: Usuario,
    viewModel: MovimientoViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()
    val productos by viewModel.productos.collectAsStateWithLifecycle()

    var menuProductoExpandido by remember { mutableStateOf(false) }
    var menuTipoExpandido by remember { mutableStateOf(false) }

    var mostrarMensajeExito by remember { mutableStateOf(false) }

    var mostrarCamara by remember { mutableStateOf(false) }
    var errorEscaneo by remember { mutableStateOf<String?>(null) }

    val intentarRegistrar: () -> Unit = {
        if (viewModel.registrarMovimiento(usuario)) {
            mostrarMensajeExito = true
        }
    }

    val stockActual = estado.producto?.let { elegido ->
        productos.find { it.id == elegido.id }?.stockActual
    }

    if (mostrarCamara) {
        CamaraNativa(
            modo = ModoCamara.ESCANEAR_CODIGO,
            onCerrar = { mostrarCamara = false },
            onCodigoEscaneado = { codigo ->
                mostrarCamara = false
                val encontrado = productos.find { it.codigo.equals(codigo.trim(), ignoreCase = true) }
                if (encontrado != null) {
                    viewModel.seleccionarProducto(encontrado)
                    errorEscaneo = null
                } else {
                    errorEscaneo = "No existe un producto con el código \"${codigo.trim()}\"."
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Registrar movimiento",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))


            ExposedDropdownMenuBox(
                expanded = menuProductoExpandido,
                onExpandedChange = { menuProductoExpandido = !menuProductoExpandido },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.producto?.let { "${it.codigo} - ${it.nombre}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Producto") },
                    isError = estado.errores.errorProducto != null,
                    supportingText = {
                        val mensaje = estado.errores.errorProducto
                            ?: stockActual?.let { "Stock actual: $it" }
                        mensaje?.let { Text(it) }
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuProductoExpandido)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuProductoExpandido,
                    onDismissRequest = { menuProductoExpandido = false }
                ) {
                    productos.forEach { producto ->
                        DropdownMenuItem(
                            text = { Text("${producto.codigo} - ${producto.nombre}") },
                            onClick = {
                                viewModel.seleccionarProducto(producto)
                                menuProductoExpandido = false
                            }
                        )
                    }
                }
            }


            OutlinedButton(
                onClick = { errorEscaneo = null; mostrarCamara = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Escanear código del producto")
            }
            errorEscaneo?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            ExposedDropdownMenuBox(
                expanded = menuTipoExpandido,
                onExpandedChange = { menuTipoExpandido = !menuTipoExpandido },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.tipo?.let { etiquetaTipo(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de movimiento") },
                    isError = estado.errores.errorTipo != null,
                    supportingText = { estado.errores.errorTipo?.let { Text(it) } },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuTipoExpandido)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuTipoExpandido,
                    onDismissRequest = { menuTipoExpandido = false }
                ) {
                    usuario.rol.tiposDeMovimientoPermitidos().forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(etiquetaTipo(tipo)) },
                            onClick = {
                                viewModel.seleccionarTipo(tipo)
                                menuTipoExpandido = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estado.cantidad,
                onValueChange = viewModel::actualizarCantidad,
                label = { Text("Cantidad") },
                isError = estado.errores.errorCantidad != null,
                supportingText = { estado.errores.errorCantidad?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.motivo,
                onValueChange = viewModel::actualizarMotivo,
                label = { Text("Motivo") },
                isError = estado.errores.errorMotivo != null,
                supportingText = { estado.errores.errorMotivo?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "${usuario.nombre} (${etiquetaRol(usuario.rol)})",
                onValueChange = {},
                readOnly = true,
                label = { Text("Responsable") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = intentarRegistrar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrar movimiento")
            }
        }

        AnimatedVisibility(
            visible = mostrarMensajeExito,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¡Movimiento registrado con éxito!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (mostrarMensajeExito) {
        LaunchedEffect(Unit) {
            delay(2.seconds)
            mostrarMensajeExito = false
        }
    }
}

fun etiquetaTipo(tipo: TipoMovimiento): String = when (tipo) {
    TipoMovimiento.ENTRADA -> "Entrada"
    TipoMovimiento.SALIDA_VENTA -> "Salida por venta"
    TipoMovimiento.SALIDA_CONSUMO -> "Salida por consumo"
    TipoMovimiento.AJUSTE -> "Ajuste"
}