package com.aliexpressclone.app.ui.admin.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.buyer.account.AccountViewModel
import com.aliexpressclone.app.ui.theme.AliBrown

@Composable
fun AdminAccountScreen(
    viewModel: AccountViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().background(AliBrown).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = user?.name?.take(1)?.uppercase() ?: "?", color = Color.White, style = MaterialTheme.typography.titleLarge)
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = user?.name ?: "", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(text = "${user?.email ?: ""} · Administrador", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Text(
            text = "Desde aquí administras el catálogo y el estado de los pedidos de todos los compradores.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}
