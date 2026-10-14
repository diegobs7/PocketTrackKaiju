package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.viewmodel.ProductoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.model.Producto

@Composable
fun PantallaRegistroProducto(
    viewModel: ProductoViewModel = viewModel(),
    codigosExistentes: List<String>,
    onGuardar: (Producto) -> Unit,
    onRegistroExitoso: () -> Unit = {}
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()

    val intentarRegistrar: () -> Unit = {
        viewModel.crearProducto(codigosExistentes)?.let { producto ->
            onGuardar(producto)
            onRegistroExitoso()
        }
    }

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

        OutlinedTextField(
            value = estado.categoria,
            onValueChange = viewModel::actualizarCategoria,
            label = { Text("Categoría") },
            isError = estado.errores.errorCategoria != null,
            supportingText = { estado.errores.errorCategoria?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.tipo,
            onValueChange = viewModel::actualizarTipo,
            label = { Text("Tipo") },
            isError = estado.errores.errorTipo != null,
            supportingText = { estado.errores.errorTipo?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

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
}