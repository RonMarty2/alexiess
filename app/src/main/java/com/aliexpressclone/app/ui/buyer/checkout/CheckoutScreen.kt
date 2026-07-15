package com.aliexpressclone.app.ui.buyer.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.util.formatPrice

private val paymentOptions = listOf(
    "Tarjeta de crédito (simulado)",
    "Tarjeta de débito (simulado)",
    "Pago contra entrega (simulado)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit,
    onOrderPlaced: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val total by viewModel.total.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.placedOrderId) {
        uiState.placedOrderId?.let { onOrderPlaced(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar compra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total a pagar", style = MaterialTheme.typography.bodySmall)
                    Text(formatPrice(total), style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = { viewModel.confirmPurchase() },
                    enabled = !uiState.isPlacingOrder
                ) {
                    if (uiState.isPlacingOrder) {
                        CircularProgressIndicator(modifier = Modifier.padding(2.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Confirmar compra")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Dirección de envío", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(
                value = uiState.shippingName,
                onValueChange = viewModel::updateName,
                label = { Text("Nombre del destinatario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = uiState.shippingPhone,
                onValueChange = viewModel::updatePhone,
                label = { Text("Teléfono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = uiState.shippingAddress,
                onValueChange = viewModel::updateAddress,
                label = { Text("Dirección completa") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Método de pago (simulado, no se realiza ningún cobro real)", style = MaterialTheme.typography.titleSmall)
            paymentOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = uiState.paymentMethod == option,
                            onClick = { viewModel.updatePaymentMethod(option) }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = uiState.paymentMethod == option, onClick = { viewModel.updatePaymentMethod(option) })
                    Text(option)
                }
            }
        }
    }
}
