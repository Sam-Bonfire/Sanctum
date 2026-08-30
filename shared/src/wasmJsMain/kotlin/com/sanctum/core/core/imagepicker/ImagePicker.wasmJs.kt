package com.sanctum.core.core.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker {
    return remember {
        object : ImagePicker {
            override fun launch() {
                // Not supported on wasm currently
                onImagePicked(null)
            }
        }
    }
}
