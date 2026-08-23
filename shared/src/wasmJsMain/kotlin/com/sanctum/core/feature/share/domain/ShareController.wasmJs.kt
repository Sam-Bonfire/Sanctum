package com.sanctum.core.feature.share.domain

import androidx.compose.ui.graphics.ImageBitmap

actual class ShareController actual constructor() {
    actual fun shareImage(image: ImageBitmap) {
    }

    actual fun isShareSupported(): Boolean = false
}
