package com.aliexpressclone.app.ui.admin.sellers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.PhotoPickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSellerEditScreen(
    viewModel: AdminSellerEditViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.id == 0L) "Nuevo vendedor" else "Editar vendedor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { value -> viewModel.update { it.copy(name = value) } },
                label = { Text("Nombre del vendedor / tienda") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = { value -> viewModel.update { it.copy(description = value) } },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            PhotoPickerField(
                label = "Logo del vendedor",
                imageUri = state.logoUri,
                onImagePicked = { uri -> viewModel.update { it.copy(logoUri = uri) } },
                modifier = Modifier.padding(top = 16.dp)
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.save() },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Guardar vendedor")
            }
        }
    }
}
