package com.aliexpressclone.app.ui.buyer.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.ui.theme.AliBrown

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onOrdersClick: (OrderStatus?) -> Unit,
    onLogout: () -> Unit
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AliBrown)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = user?.name?.take(1)?.uppercase() ?: "?", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.titleLarge)
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = user?.name ?: "", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.titleMedium)
                Text(text = user?.email ?: "", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Text("Mis pedidos", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickLink("A pagar") { onOrdersClick(OrderStatus.A_PAGAR) }
            QuickLink("Procesando") { onOrdersClick(OrderStatus.PROCESANDO) }
            QuickLink("Enviado") { onOrdersClick(OrderStatus.ENVIADO) }
            QuickLink("Finalizado") { onOrdersClick(OrderStatus.FINALIZADO) }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        AccountRow(label = "Historial de pedidos") { onOrdersClick(null) }
        Divider()
        AccountRow(label = "Lista de deseos (próximamente)") { }
        Divider()
        AccountRow(label = "Cupones (próximamente)") { }
        Divider()

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 24.dp))
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun QuickLink(label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun AccountRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
