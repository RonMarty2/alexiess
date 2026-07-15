package com.aliexpressclone.app.ui.buyer.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.ui.components.ProductCard
import com.aliexpressclone.app.ui.theme.AliBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel,
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val averageRating = if (products.isEmpty()) 0.0 else products.map { it.rating }.average()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.storeName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(padding)
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AliBrown)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Store, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(text = viewModel.storeName, color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.titleSmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFFFC107))
                            Text(
                                text = " %.1f · ${products.size} productos".format(averageRating),
                                color = androidx.compose.ui.graphics.Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            items(products) { product ->
                ProductCard(product = product, onClick = { onProductClick(product.id) })
            }
        }
    }
}
