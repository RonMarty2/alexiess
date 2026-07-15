package com.aliexpressclone.app.ui.admin.dashboard

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.util.formatPrice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdminDashboardScreen(viewModel: AdminDashboardViewModel) {
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val buyerCount by viewModel.buyerCount.collectAsStateWithLifecycle()
    val pendingOrders by viewModel.pendingOrders.collectAsStateWithLifecycle()
    val totalOrders by viewModel.totalOrders.collectAsStateWithLifecycle()
    val totalSales by viewModel.totalSales.collectAsStateWithLifecycle()
    val resetInProgress by viewModel.resetInProgress.collectAsStateWithLifecycle()
    val exportInProgress by viewModel.exportInProgress.collectAsStateWithLifecycle()
    var showResetConfirm by remember { mutableStateOf(false) }
    var pendingExportJson by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        val json = pendingExportJson
        if (uri != null && json != null) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                }
                Toast.makeText(context, "Catálogo exportado correctamente", Toast.LENGTH_SHORT).show()
            }
        }
        pendingExportJson = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
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

        Divider(modifier = Modifier.padding(vertical = 20.dp))

        Text(text = "Mantenimiento", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "Borra únicamente pedidos y carritos de prueba. Nunca toca productos, vendedores ni fotos.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )
        OutlinedButton(
            onClick = { showResetConfirm = true },
            enabled = !resetInProgress,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (resetInProgress) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp))
            } else {
                Text("Reiniciar pedidos y carritos de prueba")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 20.dp))

        Text(text = "Respaldo del catálogo", style = MaterialTheme.typography.titleSmall)
        Text(
            text = "Genera un archivo con tus vendedores, productos, fotos y descripciones para guardarlo como respaldo permanente.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )
        Button(
            onClick = {
                viewModel.exportCatalog { json ->
                    pendingExportJson = json
                    exportLauncher.launch("aliclone_catalogo_backup.json")
                }
            },
            enabled = !exportInProgress,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (exportInProgress) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Exportar catálogo")
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("¿Reiniciar pedidos y carritos?") },
            text = { Text("Se borrarán todos los pedidos, su seguimiento y los carritos de compra. Los productos, vendedores y fotos NO se ven afectados.") },
            confirmButton = {
                Button(onClick = {
                    showResetConfirm = false
                    viewModel.resetTransactionalData()
                }) {
                    Text("Sí, reiniciar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
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
