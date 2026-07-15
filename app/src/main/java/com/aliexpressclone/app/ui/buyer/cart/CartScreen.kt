package com.aliexpressclone.app.ui.buyer.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.dao.CartItemWithProduct
import com.aliexpressclone.app.ui.components.ProductImage
import com.aliexpressclone.app.util.formatPrice

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onCheckout: () -> Unit
) {
    val items by viewModel.cartItems.collectAsStateWithLifecycle()
    val total by viewModel.total.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (items.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Total", style = MaterialTheme.typography.bodySmall)
                        Text(text = formatPrice(total), style = MaterialTheme.typography.titleMedium)
                    }
                    Button(onClick = onCheckout) {
                        Text("Ir a pagar (${items.size})")
                    }
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tu cesta está vacía", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(items, key = { it.cartItemId }) { item ->
                    CartRow(
                        item = item,
                        onIncrease = { viewModel.increase(item) },
                        onDecrease = { viewModel.decrease(item) },
                        onRemove = { viewModel.remove(item) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItemWithProduct,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductImage(
            imageUri = item.imageUri,
            emoji = item.placeholderEmoji,
            colorHex = item.placeholderColorHex,
            modifier = Modifier.size(72.dp)
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(text = item.name, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
            Text(text = item.storeName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(text = formatPrice(item.price), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 4.dp))
            Row(modifier = Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onDecrease) { Text("-") }
                Text(text = " ${item.quantity} ", modifier = Modifier.padding(horizontal = 8.dp))
                OutlinedButton(onClick = onIncrease) { Text("+") }
            }
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
        }
    }
}
