package com.aliexpressclone.app.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * Copies a picked gallery image into the app's own internal storage so it keeps
 * working even if the original gallery photo is later deleted or moved.
 */
object ImageStorage {
    fun copyToInternalStorage(context: Context, source: Uri): String? {
        return try {
            val imagesDir = File(context.filesDir, "images").apply { if (!exists()) mkdirs() }
            val destFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(source)?.use { input ->
                destFile.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            Uri.fromFile(destFile).toString()
        } catch (e: Exception) {
            null
        }
    }
}
