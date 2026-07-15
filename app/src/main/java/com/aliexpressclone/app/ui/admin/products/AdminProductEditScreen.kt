package com.aliexpressclone.app.ui.admin.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.PhotoPickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductEditScreen(
    viewModel: AdminProductEditViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val sellers by viewModel.sellers.collectAsStateWithLifecycle()
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var sellerMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.id == 0L) "Nuevo producto" else "Editar producto") },
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
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = { value -> viewModel.update { it.copy(description = value) } },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = state.price,
                    onValueChange = { value -> viewModel.update { it.copy(price = value) } },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = state.originalPrice,
                    onValueChange = { value -> viewModel.update { it.copy(originalPrice = value) } },
                    label = { Text("Precio original") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = state.stock,
                    onValueChange = { value -> viewModel.update { it.copy(stock = value) } },
                    label = { Text("Stock") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = state.soldCount,
                    onValueChange = { value -> viewModel.update { it.copy(soldCount = value) } },
                    label = { Text("Vendidos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = state.rating,
                    onValueChange = { value -> viewModel.update { it.copy(rating = value) } },
                    label = { Text("Calificación (0-5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = state.storeName,
                    onValueChange = { value -> viewModel.update { it.copy(storeName = value) } },
                    label = { Text("Tienda") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = state.placeholderEmoji,
                    onValueChange = { value -> viewModel.update { it.copy(placeholderEmoji = value) } },
                    label = { Text("Ícono (emoji)") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = state.placeholderColorHex,
                    onValueChange = { value -> viewModel.update { it.copy(placeholderColorHex = value) } },
                    label = { Text("Color (#RRGGBB)") },
                    modifier = Modifier.weight(1f)
                )
            }

            val selectedCategoryName = categories.firstOrNull { it.id == state.categoryId }?.name ?: "Selecciona categoría"
            ExposedDropdownMenuBox(
                expanded = categoryMenuExpanded,
                onExpandedChange = { categoryMenuExpanded = it },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = { categoryMenuExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text("${category.emoji} ${category.name}") },
                            onClick = {
                                viewModel.update { it.copy(categoryId = category.id) }
                                categoryMenuExpanded = false
                            }
                        )
                    }
                }
            }

            val selectedSellerName = sellers.firstOrNull { it.id == state.sellerId }?.name ?: "Sin vendedor asignado"
            ExposedDropdownMenuBox(
                expanded = sellerMenuExpanded,
                onExpandedChange = { sellerMenuExpanded = it },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedSellerName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Vendedor") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sellerMenuExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = sellerMenuExpanded,
                    onDismissRequest = { sellerMenuExpanded = false }
                ) {
                    if (sellers.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Todavía no hay vendedores creados") },
                            onClick = { sellerMenuExpanded = false }
                        )
                    }
                    sellers.forEach { seller ->
                        DropdownMenuItem(
                            text = { Text(seller.name) },
                            onClick = {
                                viewModel.selectSeller(seller)
                                sellerMenuExpanded = false
                            }
                        )
                    }
                }
            }

            PhotoPickerField(
                label = "Foto real del producto",
                imageUri = state.imageUri,
                onImagePicked = { uri -> viewModel.update { it.copy(imageUri = uri) } },
                modifier = Modifier.padding(top = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.freeShipping,
                    onCheckedChange = { value -> viewModel.update { it.copy(freeShipping = value) } }
                )
                Text("Envío gratis")
            }

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
                Text("Guardar producto")
            }
        }
    }
}
