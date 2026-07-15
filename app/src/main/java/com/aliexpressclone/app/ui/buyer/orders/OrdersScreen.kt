package com.aliexpressclone.app.ui.buyer.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.ui.components.OrderStatusBadge
import com.aliexpressclone.app.util.formatDate
import com.aliexpressclone.app.util.formatPrice

private val tabStatuses: List<OrderStatus?> = listOf(
    null,
    OrderStatus.A_PAGAR,
    OrderStatus.PROCESANDO,
    OrderStatus.ENVIADO,
    OrderStatus.EN_TRANSITO,
    OrderStatus.FINALIZADO
)

private fun tabLabel(status: OrderStatus?) = status?.label ?: "Todos"

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    onOrderClick: (Long) -> Unit
) {
    val selected by viewModel.selectedStatus.collectAsStateWithLifecycle()
    val orders by viewModel.filteredOrders.collectAsStateWithLifecycle()
    val selectedIndex = tabStatuses.indexOf(selected).coerceAtLeast(0)

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedIndex) {
            tabStatuses.forEachIndexed { index, status ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { viewModel.selectStatus(status) },
                    text = { Text(tabLabel(status)) }
                )
            }
        }

        if (orders.isEmpty()) {
            Text(
                text = "No tienes pedidos en esta categoría",
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(orders, key = { it.id }) { order ->
                    OrderRow(order = order, onClick = { onOrderClick(order.id) })
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun OrderRow(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pedido #${order.orderNumber}", style = MaterialTheme.typography.bodySmall)
                OrderStatusBadge(status = order.status)
            }
            Text(text = formatDate(order.createdAt), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
            Text(text = "Total: ${formatPrice(order.total)}", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 6.dp))
        }
    }
}
