package com.example.pockettrackkaiju_tea.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.viewmodel.RegistroUsuarioViewModel
import androidx.compose.ui.text.input.ImeAction
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory


@Composable
fun PantallaRegistroUsuario(
    viewModel: RegistroUsuarioViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    onRegistroExitoso: () -> Unit = {}
) {
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()
    val intentarRegistrar: () -> Unit = {
        if (viewModel.registrarUsuario()) onRegistroExitoso()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::actualizarNombre,
            label = { Text("Nombre") },
            isError = estado.errores.errorNombre != null,
            supportingText = { estado.errores.errorNombre?.let { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.email,
            onValueChange = viewModel::actualizarEmail,
            label = { Text("Correo electrónico") },
            isError = estado.errores.errorEmail != null,
            supportingText = { estado.errores.errorEmail?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.password,
            onValueChange = viewModel::actualizarPassword,
            label = { Text("Contraseña") },
            isError = estado.errores.errorPassword != null,
            supportingText = { estado.errores.errorPassword?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.confirmarPassword,
            onValueChange = viewModel::actualizarConfirmarPassword,
            label = { Text("Confirmar contraseña") },
            isError = estado.errores.errorConfirmarPassword != null,
            supportingText = { estado.errores.errorConfirmarPassword?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { intentarRegistrar() }),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = intentarRegistrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Registrarme")
        }
    }
}