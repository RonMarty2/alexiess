package com.aliexpressclone.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.ui.theme.AliDiscountRed
import com.aliexpressclone.app.util.formatPrice
import com.aliexpressclone.app.util.formatSoldCount

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                ProductImage(
                    imageUri = product.imageUri,
                    emoji = product.placeholderEmoji,
                    colorHex = product.placeholderColorHex,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
                if (product.discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .background(AliDiscountRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discountPercent}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                Text(
                    text = formatPrice(product.price),
                    color = AliDiscountRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (product.originalPrice > product.price) {
                    Text(
                        text = formatPrice(product.originalPrice),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        fontSize = 11.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color(0xFFFFB300),
                        modifier = Modifier.padding(end = 2.dp).size(12.dp)
                    )
                    Text(
                        text = "${product.rating} · ${formatSoldCount(product.soldCount)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
