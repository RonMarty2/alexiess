package com.aliexpressclone.app.ui.buyer.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.ui.buyer.home.HomeViewModel

@Composable
fun CategoriesScreen(
    viewModel: HomeViewModel,
    onCategoryClick: (Category) -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    LazyColumn {
        items(categories) { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick(category) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(runCatching { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(category.colorHex)) }.getOrDefault(MaterialTheme.colorScheme.surfaceVariant)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category.emoji)
                }
                Text(
                    text = category.name,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
            }
            Divider()
        }
    }
}
