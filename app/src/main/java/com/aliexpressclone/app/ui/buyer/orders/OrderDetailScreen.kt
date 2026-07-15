package com.aliexpressclone.app.ui.buyer.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.ui.components.OrderStatusBadge
import com.aliexpressclone.app.ui.components.ProductImage
import com.aliexpressclone.app.util.formatDate
import com.aliexpressclone.app.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel,
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    val order by viewModel.order.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val tracking by viewModel.tracking.collectAsStateWithLifecycle()
    val myReviews by viewModel.myReviews.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedido #${order?.orderNumber.orEmpty()}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        val currentOrder = order ?: return@Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OrderStatusBadge(status = currentOrder.status)
            if (currentOrder.estimatedDeliveryDate != null) {
                Text(
                    text = "Entrega estimada: ${formatDate(currentOrder.estimatedDeliveryDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            if (!currentOrder.trackingNumber.isNullOrBlank()) {
                Text(
                    text = "Número de rastreo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = currentOrder.trackingNumber,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Seguimiento del pedido", style = MaterialTheme.typography.titleSmall)
            tracking.forEach { event ->
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = event.status.label, style = MaterialTheme.typography.bodyLarge)
                    Text(text = formatDate(event.date), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    if (event.note.isNotBlank()) {
                        Text(text = event.note, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Dirección de envío", style = MaterialTheme.typography.titleSmall)
            Text(text = currentOrder.shippingName, modifier = Modifier.padding(top = 6.dp))
            Text(text = currentOrder.shippingPhone, style = MaterialTheme.typography.bodySmall)
            Text(text = currentOrder.shippingAddress, style = MaterialTheme.typography.bodySmall)

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Artículos", style = MaterialTheme.typography.titleSmall)
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable { onProductClick(item.productId) }
                ) {
                    ProductImage(
                        imageUri = item.imageUri,
                        emoji = item.placeholderEmoji,
                        colorHex = item.placeholderColorHex,
                        modifier = Modifier.size(56.dp)
                    )
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = item.storeName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text(text = item.productName, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                        Text(text = "${formatPrice(item.unitPrice)} x${item.quantity}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 2.dp))
                        if (!item.realDescription.isNullOrBlank()) {
                            Text(
                                text = item.realDescription,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                if (currentOrder.status == OrderStatus.FINALIZADO) {
                    val existingReview = myReviews.firstOrNull { it.productId == item.productId }
                    ReviewEditor(
                        item = item,
                        existingRating = existingReview?.rating,
                        existingComment = existingReview?.comment,
                        onSubmit = { rating, comment -> viewModel.submitReview(item.productId, rating, comment) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(text = formatPrice(currentOrder.total), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun ReviewEditor(
    item: OrderItem,
    existingRating: Float?,
    existingComment: String?,
    onSubmit: (Float, String) -> Unit
) {
    var rating by remember(item.productId) { mutableStateOf(existingRating ?: 5f) }
    var comment by remember(item.productId) { mutableStateOf(existingComment.orEmpty()) }

    Column(modifier = Modifier.fillMaxWidth().padding(start = 68.dp, top = 8.dp)) {
        Text(
            text = if (existingRating != null) "Tu reseña" else "Calificar este producto",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.padding(top = 4.dp)) {
            (1..5).forEach { star ->
                IconButton(onClick = { rating = star.toFloat() }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (star <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFFFFB300)
                    )
                }
            }
        }
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario (opcional)") },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )
        Button(
            onClick = { onSubmit(rating, comment) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (existingRating != null) "Actualizar reseña" else "Guardar reseña")
        }
    }
}
