package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel

@Composable
fun MensajeErrorConIcono(mensaje: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PantallaInicioSesion(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    onLoginExitoso: (Usuario, Boolean) -> Unit,
    onIrRegistro: () -> Unit = {}
){
    val estado by viewModel.estadoFormulario.collectAsStateWithLifecycle()

    val intentarIngresar: () -> Unit = {
        viewModel.iniciarSesion(onLoginExitoso)
    }

    val errorEmail = estado.errores.errorEmail
    val errorPassword = estado.errores.errorPassword
    val esErrorCredenciales = errorPassword == "Correo o contraseña incorrectos."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        // DISEÑO 1: Banner superior de Error General (Credenciales incorrectas)
        if (esErrorCredenciales) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Correo o contraseña incorrectos. Inténtalo de nuevo.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        OutlinedTextField(
            value = estado.email,
            onValueChange = viewModel::actualizarEmail,
            label = { Text("Correo electrónico") },
            isError = errorEmail != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        // DISEÑO 2: Error de formato de correo con ícono
        if (errorEmail != null) {
            MensajeErrorConIcono(errorEmail)
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.password,
            onValueChange = viewModel::actualizarPassword,
            label = { Text("Contraseña") },
            isError = errorPassword != null,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { intentarIngresar() }),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // DISEÑO 2: Error de formato de contraseña con ícono (Oculto si el banner está activo)
        if (errorPassword != null && !esErrorCredenciales) {
            MensajeErrorConIcono(errorPassword)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = estado.recordarme,
                onCheckedChange = viewModel::actualizarRecordarme
            )
            Text("Recordarme")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = intentarIngresar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Ingresar")
        }
        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onIrRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}