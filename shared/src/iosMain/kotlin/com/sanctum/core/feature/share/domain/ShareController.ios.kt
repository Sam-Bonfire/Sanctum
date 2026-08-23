package com.sanctum.core.feature.share.domain

import androidx.compose.ui.graphics.ImageBitmap

actual class ShareController actual constructor() {
    actual fun shareImage(image: ImageBitmap) {
        // Implementation omitted for now, we'll disable share on iOS/Wasm for this scope if we can't fully implement it
    }

    actual fun isShareSupported(): Boolean = false
}
