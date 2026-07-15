package com.aliexpressclone.app.ui.buyer.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.PlaceholderImage
import com.aliexpressclone.app.ui.theme.AliDiscountRed
import com.aliexpressclone.app.util.formatPrice
import com.aliexpressclone.app.util.formatSoldCount
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onBack: () -> Unit,
    onStoreClick: (String) -> Unit,
    onBuyNow: () -> Unit
) {
    val product by viewModel.product.collectAsStateWithLifecycle()
    val quantity by viewModel.quantity.collectAsStateWithLifecycle()
    val addedToCart by viewModel.addedToCart.collectAsStateWithLifecycle()

    LaunchedEffect(addedToCart) {
        if (addedToCart) {
            delay(1500)
            viewModel.consumeAddedToCartEvent()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            val current = product
            if (current != null) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.addToCart() },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Añadir a la cesta")
                    }
                    androidx.compose.material3.Button(
                        onClick = { viewModel.buyNow(onBuyNow) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Comprar ahora")
                    }
                }
            }
        },
        snackbarHost = {
            if (addedToCart) {
                Snackbar(modifier = Modifier.padding(12.dp)) {
                    Text("Añadido a la cesta")
                }
            }
        }
    ) { padding ->
        val current = product
        if (current == null) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {}
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            PlaceholderImage(
                emoji = current.placeholderEmoji,
                colorHex = current.placeholderColorHex,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                cornerRadius = 0.dp
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatPrice(current.price),
                        color = AliDiscountRed,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (current.originalPrice > current.price) {
                        Text(
                            text = formatPrice(current.originalPrice),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Text(
                            text = "-${current.discountPercent}%",
                            color = AliDiscountRed,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Text(
                    text = current.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFFFB300))
                    Text(
                        text = " ${current.rating} · ${formatSoldCount(current.soldCount)} · Stock: ${current.stock}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (current.freeShipping) {
                    Text(
                        text = "Envío gratis",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    text = current.storeName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.clickable { onStoreClick(current.storeName) }
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text(text = "Descripción", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = current.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text(text = "Cantidad", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(onClick = { viewModel.decreaseQuantity() }) { Text("-") }
                    Text(text = "$quantity", style = MaterialTheme.typography.titleSmall)
                    OutlinedButton(onClick = { viewModel.increaseQuantity() }) { Text("+") }
                }
            }
        }
    }
}
