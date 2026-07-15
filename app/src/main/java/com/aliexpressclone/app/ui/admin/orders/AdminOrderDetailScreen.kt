package com.aliexpressclone.app.ui.admin.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.ui.components.OrderStatusBadge
import com.aliexpressclone.app.ui.components.PlaceholderImage
import com.aliexpressclone.app.util.formatDate
import com.aliexpressclone.app.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdminOrderDetailScreen(
    viewModel: AdminOrderDetailViewModel,
    onBack: () -> Unit
) {
    val order by viewModel.order.collectAsStateWithLifecycle()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val tracking by viewModel.tracking.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()
    val note by viewModel.note.collectAsStateWithLifecycle()
    val estimatedDays by viewModel.estimatedDays.collectAsStateWithLifecycle()

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
        val effectiveStatus = selectedStatus ?: currentOrder.status

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OrderStatusBadge(status = currentOrder.status)
            Text(
                text = "Total: ${formatPrice(currentOrder.total)}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "Comprador: ${currentOrder.shippingName}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            Text(text = currentOrder.shippingPhone, style = MaterialTheme.typography.bodySmall)
            Text(text = currentOrder.shippingAddress, style = MaterialTheme.typography.bodySmall)

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Artículos", style = MaterialTheme.typography.titleSmall)
            items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    PlaceholderImage(
                        emoji = item.placeholderEmoji,
                        colorHex = item.placeholderColorHex,
                        modifier = Modifier.size(48.dp)
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(text = item.productName, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                        Text(text = "${formatPrice(item.unitPrice)} x${item.quantity}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Actualizar seguimiento", style = MaterialTheme.typography.titleSmall)
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatus.entries.forEach { status ->
                    FilterChip(
                        selected = effectiveStatus == status,
                        onClick = { viewModel.selectStatus(status) },
                        label = { Text(status.label) }
                    )
                }
            }

            OutlinedTextField(
                value = note,
                onValueChange = viewModel::updateNote,
                label = { Text("Nota de seguimiento (opcional)") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )

            if (effectiveStatus != OrderStatus.FINALIZADO && effectiveStatus != OrderStatus.CANCELADO) {
                OutlinedTextField(
                    value = estimatedDays,
                    onValueChange = viewModel::updateEstimatedDays,
                    label = { Text("Días estimados de entrega desde hoy") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                )
            }

            if (currentOrder.estimatedDeliveryDate != null) {
                Text(
                    text = "Entrega estimada actual: ${formatDate(currentOrder.estimatedDeliveryDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.applyUpdate() },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Guardar actualización")
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Historial de seguimiento", style = MaterialTheme.typography.titleSmall)
            tracking.sortedByDescending { it.date }.forEach { event ->
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = event.status.label, style = MaterialTheme.typography.bodyLarge)
                    Text(text = formatDate(event.date), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    if (event.note.isNotBlank()) {
                        Text(text = event.note, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
