package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.ui.components.BannerError
import com.example.pockettrackkaiju_tea.ui.components.MensajeExito
import com.example.pockettrackkaiju_tea.ui.components.TextoApoyoCampo
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.EditarUsuarioViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import com.example.pockettrackkaiju_tea.ui.components.SelectorRol


@Composable
fun PantallaEditarUsuario(
    usuarioId: Int,
    viewModel: EditarUsuarioViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    onGuardadoExitoso: () -> Unit = {}
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()
    var mostrarMensajeExito by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioId) {
        viewModel.cargarUsuario(usuarioId)
    }

    LaunchedEffect(mostrarMensajeExito) {
        if (mostrarMensajeExito) {
            delay(1.5.seconds)
            onGuardadoExitoso()
        }
    }

    val intentarGuardar: () -> Unit = {
        viewModel.guardarCambios(onExito = { mostrarMensajeExito = true })
    }

    val formaCampo = RoundedCornerShape(12.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 88.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            estado.errores.errorGeneral?.let { mensaje ->
                BannerError(
                    mensaje = mensaje,
                    onCerrar = viewModel::limpiarErrorGeneral,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Editar usuario",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Cambia el nombre o el correo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = estado.nombre,
                        onValueChange = viewModel::actualizarNombre,
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        isError = estado.errores.errorNombre != null,
                        supportingText = { TextoApoyoCampo(estado.errores.errorNombre) },
                        shape = formaCampo,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(Modifier.height(4.dp))

                    OutlinedTextField(
                        value = estado.email,
                        onValueChange = viewModel::actualizarEmail,
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        isError = estado.errores.errorEmail != null,
                        supportingText = { TextoApoyoCampo(estado.errores.errorEmail) },
                        shape = formaCampo,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { intentarGuardar() }),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))

                    SelectorRol(
                        rol = estado.rol,
                        onRolSeleccionado = viewModel::actualizarRol
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = intentarGuardar,
                        enabled = !estado.cargando && !mostrarMensajeExito,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(if (estado.cargando) "Guardando..." else "Guardar cambios")
                    }
                }
            }
        }

        MensajeExito(
            visible = mostrarMensajeExito,
            texto = "¡Usuario actualizado con éxito!",
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}