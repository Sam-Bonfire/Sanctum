package com.sanctum.core.feature.share.domain

import androidx.compose.ui.graphics.ImageBitmap

expect class ShareController() {
    fun shareImage(image: ImageBitmap)
    fun isShareSupported(): Boolean
}
