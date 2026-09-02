package com.sanctum.core.core.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberImagePicker(onImagePicked: (ImageBitmap?) -> Unit): ImagePicker

interface ImagePicker {
    fun launch()
}
