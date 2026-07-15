package com.aliexpressclone.app.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoPickerField(
    label: String,
    imageUri: String?,
    onImagePicked: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> onImagePicked(uri?.toString()) }

    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                imageUri = imageUri,
                emoji = "🖼️",
                colorHex = "#E0E0E0",
                modifier = Modifier.size(72.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                OutlinedButton(
                    onClick = {
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Text(if (imageUri.isNullOrBlank()) "Elegir foto" else "Cambiar foto")
                }
                if (!imageUri.isNullOrBlank()) {
                    TextButton(onClick = { onImagePicked(null) }) {
                        Text("Quitar foto")
                    }
                }
            }
        }
    }
}
