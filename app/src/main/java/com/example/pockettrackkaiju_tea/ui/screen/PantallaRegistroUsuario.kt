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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.ui.components.MensajeExito
import com.example.pockettrackkaiju_tea.ui.components.OpcionMenu
import com.example.pockettrackkaiju_tea.ui.components.TextoApoyoCampo
import com.example.pockettrackkaiju_tea.ui.components.etiquetaRol
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.RegistroUsuarioViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroUsuario(
    viewModel: RegistroUsuarioViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    conSelectorDeRol: Boolean = false,
    onRegistroExitoso: () -> Unit = {}
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()
    var menuRolExpandido by remember { mutableStateOf(false) }
    var mostrarMensajeExito by remember { mutableStateOf(false) }

    val intentarRegistrar: () -> Unit = {
        viewModel.registrarUsuario(
            conRol = conSelectorDeRol,
            onExito = { mostrarMensajeExito = true }
        )
    }

    LaunchedEffect(mostrarMensajeExito) {
        if (mostrarMensajeExito) {
            delay(1.5.seconds)
            onRegistroExitoso()
        }
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
                        text = if (conSelectorDeRol) "Crear usuario" else "Crear cuenta",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (conSelectorDeRol) "Elige el rol de la nueva cuenta" else "Únete a PocketTrack Kaiju",
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    OutlinedTextField(
                        value = estado.password,
                        onValueChange = viewModel::actualizarPassword,
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        isError = estado.errores.errorPassword != null,
                        supportingText = { TextoApoyoCampo(estado.errores.errorPassword) },
                        shape = formaCampo,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    OutlinedTextField(
                        value = estado.confirmarPassword,
                        onValueChange = viewModel::actualizarConfirmarPassword,
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        isError = estado.errores.errorConfirmarPassword != null,
                        supportingText = { TextoApoyoCampo(estado.errores.errorConfirmarPassword) },
                        shape = formaCampo,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = if (conSelectorDeRol) ImeAction.Next else ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { intentarRegistrar() }),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (conSelectorDeRol) {
                        Spacer(Modifier.height(4.dp))

                        ExposedDropdownMenuBox(
                            expanded = menuRolExpandido,
                            onExpandedChange = { menuRolExpandido = !menuRolExpandido },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = etiquetaRol(estado.rol),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Rol") },
                                shape = formaCampo,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuRolExpandido)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = menuRolExpandido,
                                onDismissRequest = { menuRolExpandido = false }
                            ) {
                                Rol.entries.forEach { rol ->
                                    OpcionMenu(
                                        texto = etiquetaRol(rol),
                                        seleccionada = estado.rol == rol,
                                        onClick = {
                                            viewModel.actualizarRol(rol)
                                            menuRolExpandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = intentarRegistrar,
                        enabled = !estado.cargando && !mostrarMensajeExito,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            when {
                                estado.cargando -> "Guardando..."
                                conSelectorDeRol -> "Crear usuario"
                                else -> "Registrarme"
                            }
                        )
                    }
                }
            }
        }

        MensajeExito(
            visible = mostrarMensajeExito,
            texto = if (conSelectorDeRol) "¡Usuario creado con éxito!" else "¡Cuenta creada con éxito!",
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}