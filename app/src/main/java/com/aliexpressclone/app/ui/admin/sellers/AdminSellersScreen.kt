package com.aliexpressclone.app.ui.admin.sellers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.ProductImage

@Composable
fun AdminSellersScreen(
    viewModel: AdminSellersViewModel,
    onAddSeller: () -> Unit,
    onEditSeller: (Long) -> Unit
) {
    val sellers by viewModel.sellers.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSeller) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir vendedor")
            }
        }
    ) { padding ->
        if (sellers.isEmpty()) {
            Text(
                text = "Todavía no hay vendedores. Crea el primero con el botón +.",
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)
            )
            return@Scaffold
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(sellers, key = { it.id }) { seller ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductImage(
                        imageUri = seller.logoUri,
                        emoji = "🏪",
                        colorHex = "#D1C4E9",
                        modifier = Modifier.size(48.dp)
                    )
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(text = seller.name, style = MaterialTheme.typography.bodyMedium)
                        if (seller.description.isNotBlank()) {
                            Text(text = seller.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                        }
                    }
                    IconButton(onClick = { onEditSeller(seller.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { viewModel.deleteSeller(seller) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
                Divider()
            }
        }
    }
}
