package com.aliexpressclone.app.ui.admin.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.OrderStatusBadge
import com.aliexpressclone.app.util.formatDate
import com.aliexpressclone.app.util.formatPrice

@Composable
fun AdminOrdersScreen(
    viewModel: AdminOrdersViewModel,
    onOrderClick: (Long) -> Unit
) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    if (orders.isEmpty()) {
        Text(
            text = "Todavía no hay pedidos.",
            modifier = Modifier.fillMaxSize().padding(24.dp)
        )
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orders, key = { it.order.id }) { row ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                onClick = { onOrderClick(row.order.id) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Pedido #${row.order.orderNumber}", style = MaterialTheme.typography.bodySmall)
                        OrderStatusBadge(status = row.order.status)
                    }
                    Text(text = row.buyerName, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                    Text(text = formatDate(row.order.createdAt), style = MaterialTheme.typography.bodySmall)
                    Text(text = "Total: ${formatPrice(row.order.total)}", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
    }
}
