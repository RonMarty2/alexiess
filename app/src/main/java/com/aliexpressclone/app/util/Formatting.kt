package com.aliexpressclone.app.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatPrice(value: Double): String = "US $" + String.format(Locale.US, "%.2f", value)

private val dateFormatter = SimpleDateFormat("d MMM, yyyy", Locale("es", "ES"))

fun formatDate(timestampMillis: Long): String = dateFormatter.format(java.util.Date(timestampMillis))

fun formatSoldCount(count: Int): String = when {
    count >= 1000 -> "${count / 1000}K+ vendidos"
    else -> "$count vendidos"
}
