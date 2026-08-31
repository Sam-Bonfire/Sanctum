package com.sanctum.core.core.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker {
    return remember {
        object : ImagePicker {
            override fun launch() {
                // To do: proper iOS image picker implementation
                // For now just pass null to unblock build
                onImagePicked(null)
            }
        }
    }
}
