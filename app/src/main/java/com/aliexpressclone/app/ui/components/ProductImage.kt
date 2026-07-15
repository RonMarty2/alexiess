package com.aliexpressclone.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProductImage(
    imageUri: String?,
    emoji: String,
    colorHex: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp
) {
    if (imageUri.isNullOrBlank()) {
        PlaceholderImage(
            emoji = emoji,
            colorHex = colorHex,
            modifier = modifier,
            cornerRadius = cornerRadius
        )
    } else {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(cornerRadius))
        )
    }
}
