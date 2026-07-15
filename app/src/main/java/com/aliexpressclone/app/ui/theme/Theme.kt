package com.aliexpressclone.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AliColorScheme = lightColorScheme(
    primary = AliOrange,
    onPrimary = Color.White,
    secondary = AliBrown,
    onSecondary = Color.White,
    background = AliBackground,
    surface = AliSurface,
    onBackground = AliTextPrimary,
    onSurface = AliTextPrimary,
    error = AliDiscountRed,
    onError = Color.White
)

@Composable
fun AliExpressCloneTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AliColorScheme,
        typography = AliTypography,
        content = content
    )
}
