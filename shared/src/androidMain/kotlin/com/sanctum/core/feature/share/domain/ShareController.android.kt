package com.sanctum.core.feature.share.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class ShareController actual constructor() : KoinComponent {
    private val context: Context by inject()

    actual fun shareImage(image: ImageBitmap) {
        val bitmap = image.asAndroidBitmap()

        // Write to MediaStore to get a URI that other apps can read without requiring a custom FileProvider
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Shared Verse", null)
        val uri = Uri.parse(path)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Share Verse").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    actual fun isShareSupported(): Boolean = true
}
