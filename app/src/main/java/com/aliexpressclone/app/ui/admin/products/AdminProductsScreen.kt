package com.aliexpressclone.app.ui.admin.products

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
import com.aliexpressclone.app.ui.components.PlaceholderImage
import com.aliexpressclone.app.util.formatPrice

@Composable
fun AdminProductsScreen(
    viewModel: AdminProductsViewModel,
    onAddProduct: () -> Unit,
    onEditProduct: (Long) -> Unit
) {
    val products by viewModel.products.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir producto")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(products, key = { it.id }) { product ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlaceholderImage(
                        emoji = product.placeholderEmoji,
                        colorHex = product.placeholderColorHex,
                        modifier = Modifier.size(56.dp)
                    )
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                        Text(text = product.name, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${formatPrice(product.price)} · Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { onEditProduct(product.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { viewModel.deleteProduct(product) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
                Divider()
            }
        }
    }
}
