package com.aliexpressclone.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliexpressclone.app.data.local.entity.OrderStatus

private fun colorFor(status: OrderStatus): Color = when (status) {
    OrderStatus.A_PAGAR -> Color(0xFFFF9800)
    OrderStatus.PROCESANDO -> Color(0xFF1976D2)
    OrderStatus.ENVIADO -> Color(0xFF7B1FA2)
    OrderStatus.EN_TRANSITO -> Color(0xFF0097A7)
    OrderStatus.FINALIZADO -> Color(0xFF2E7D32)
    OrderStatus.CANCELADO -> Color(0xFF9E9E9E)
}

@Composable
fun OrderStatusBadge(status: OrderStatus, modifier: Modifier = Modifier) {
    val color = colorFor(status)
    Text(
        text = status.label,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
