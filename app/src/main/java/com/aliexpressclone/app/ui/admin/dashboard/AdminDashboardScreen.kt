package com.aliexpressclone.app.ui.admin.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.util.formatPrice

@Composable
fun AdminDashboardScreen(viewModel: AdminDashboardViewModel) {
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val buyerCount by viewModel.buyerCount.collectAsStateWithLifecycle()
    val pendingOrders by viewModel.pendingOrders.collectAsStateWithLifecycle()
    val totalOrders by viewModel.totalOrders.collectAsStateWithLifecycle()
    val totalSales by viewModel.totalSales.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Panel de administrador", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Resumen general de la tienda (datos simulados)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(title = "Productos", value = productCount.toString(), modifier = Modifier.weight(1f))
            StatCard(title = "Compradores", value = buyerCount.toString(), modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(title = "Pedidos totales", value = totalOrders.toString(), modifier = Modifier.weight(1f))
            StatCard(title = "Pendientes de pago", value = pendingOrders.toString(), modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            StatCard(title = "Ventas totales (simuladas)", value = formatPrice(totalSales), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}
