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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.TipoProducto
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.ProductoViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroProducto(
    viewModel: ProductoViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    onRegistroExitoso: () -> Unit = {}
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()

    var menuCategoriaExpandido by remember { mutableStateOf(false) }
    var menuTipoExpandido by remember { mutableStateOf(false) }

    var mostrarMensajeExito by remember { mutableStateOf(false) }

    val intentarRegistrar: () -> Unit = {
        if (viewModel.crearProducto()) {
            mostrarMensajeExito = true
        }
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
                text = "Registrar producto",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.codigo,
                onValueChange = viewModel::actualizarCodigo,
                label = { Text("Código") },
                isError = estado.errores.errorCodigo != null,
                supportingText = { estado.errores.errorCodigo?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre") },
                isError = estado.errores.errorNombre != null,
                supportingText = { estado.errores.errorNombre?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = { Text("Descripción") },
                isError = estado.errores.errorDescripcion != null,
                supportingText = { estado.errores.errorDescripcion?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = menuCategoriaExpandido,
                onExpandedChange = { menuCategoriaExpandido = !menuCategoriaExpandido },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría (Colección)") },
                    isError = estado.errores.errorCategoria != null,
                    supportingText = { estado.errores.errorCategoria?.let { Text(it) } },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoriaExpandido)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuCategoriaExpandido,
                    onDismissRequest = { menuCategoriaExpandido = false }
                ) {
                    CategoriaProducto.entries.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.displayName) },
                            onClick = {
                                viewModel.actualizarCategoria(categoria.displayName)
                                menuCategoriaExpandido = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = menuTipoExpandido,
                onExpandedChange = { menuTipoExpandido = !menuTipoExpandido },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = estado.tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Prenda") },
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
                    TipoProducto.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.displayName) },
                            onClick = {
                                viewModel.actualizarTipo(tipo.displayName)
                                menuTipoExpandido = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::actualizarPrecio,
                label = { Text("Precio") },
                prefix = { Text("$") },
                isError = estado.errores.errorPrecio != null,
                supportingText = { estado.errores.errorPrecio?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.stockActual,
                onValueChange = viewModel::actualizarStockActual,
                label = { Text("Stock actual") },
                isError = estado.errores.errorStockActual != null,
                supportingText = { estado.errores.errorStockActual?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.stockMinimo,
                onValueChange = viewModel::actualizarStockMinimo,
                label = { Text("Stock mínimo") },
                isError = estado.errores.errorStockMinimo != null,
                supportingText = {
                    Text(
                        estado.errores.errorStockMinimo
                            ?: "Se mostrará una alerta cuando el stock llegue a este número."
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.detalle,
                onValueChange = viewModel::actualizarDetalle,
                label = { Text("Detalle (opcional)") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { intentarRegistrar() }),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = intentarRegistrar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrar producto")
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
                    text = "¡Producto registrado con éxito!",
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
            delay(2.seconds) // 2 segundos
            mostrarMensajeExito = false
            onRegistroExitoso()
        }
    }
}