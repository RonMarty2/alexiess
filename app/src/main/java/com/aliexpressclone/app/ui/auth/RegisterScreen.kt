package com.aliexpressclone.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.Role

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(Role.BUYER) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Crear cuenta", style = MaterialTheme.typography.titleLarge)

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; viewModel.clearError() },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; viewModel.clearError() },
            label = { Text("Correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; viewModel.clearError() },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(text = "Tipo de cuenta", style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(selected = role == Role.BUYER, onClick = { role = Role.BUYER }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = role == Role.BUYER, onClick = { role = Role.BUYER })
            Text("Comprador")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(selected = role == Role.ADMIN, onClick = { role = Role.ADMIN }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = role == Role.ADMIN, onClick = { role = Role.ADMIN })
            Text("Administrador")
        }

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))

        Button(
            onClick = { viewModel.register(name, email, password, role, onRegisterSuccess) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Registrarme")
            }
        }

        TextButton(onClick = onBackToLogin) {
            Text("Ya tengo cuenta, iniciar sesión")
        }
    }
}
