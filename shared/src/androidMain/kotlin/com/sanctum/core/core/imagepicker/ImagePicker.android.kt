package com.sanctum.core.core.imagepicker

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                // Wrap in use to prevent resource leaks
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val options = BitmapFactory.Options().apply {
                        // Downsample to avoid OOM for very large images
                        inJustDecodeBounds = true
                        BitmapFactory.decodeStream(inputStream, null, this)

                        // Target dimensions for background
                        val targetW = 1080
                        val targetH = 1920

                        inSampleSize = 1
                        if (outHeight > targetH || outWidth > targetW) {
                            val halfHeight: Int = outHeight / 2
                            val halfWidth: Int = outWidth / 2
                            while (halfHeight / inSampleSize >= targetH && halfWidth / inSampleSize >= targetW) {
                                inSampleSize *= 2
                            }
                        }
                        inJustDecodeBounds = false
                    }

                    // Decode again with options (requires opening a new stream as the previous one was consumed by decode bounds)
                    context.contentResolver.openInputStream(uri)?.use { finalInputStream ->
                        val bitmap = BitmapFactory.decodeStream(finalInputStream, null, options)
                        onImagePicked(bitmap?.asImageBitmap())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onImagePicked(null)
            }
        } else {
            onImagePicked(null)
        }
    }

    return remember {
        object : ImagePicker {
            override fun launch() {
                launcher.launch("image/*")
            }
        }
    }
}
