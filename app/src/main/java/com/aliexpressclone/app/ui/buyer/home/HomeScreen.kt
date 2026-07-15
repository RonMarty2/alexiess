package com.aliexpressclone.app.ui.buyer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.ui.components.ProductCard
import com.aliexpressclone.app.ui.theme.AliOrange
import com.aliexpressclone.app.ui.theme.AliYellow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onProductClick: (Long) -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                SearchBarPlaceholder(onClick = onSearchClick)
                PromoBanner()
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categories) { category ->
                        CategoryChip(category, onClick = { onCategoryClick(category) })
                    }
                }
                Text(
                    text = "Recomendado para ti",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }
        }
        items(products) { product ->
            ProductCard(product = product, onClick = { onProductClick(product.id) })
        }
    }
}

@Composable
private fun SearchBarPlaceholder(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Text(
            text = "Buscar productos...",
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun PromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AliYellow)
            .padding(16.dp)
    ) {
        Column {
            Text(text = "Venta de Vacaciones", fontWeight = FontWeight.Bold, color = AliOrange, style = MaterialTheme.typography.titleSmall)
            Text(text = "Hasta -60% · Datos de demostración", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun CategoryChip(category: Category, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(runCatching { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(category.colorHex)) }.getOrDefault(MaterialTheme.colorScheme.surfaceVariant)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = category.emoji, style = MaterialTheme.typography.titleMedium)
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
