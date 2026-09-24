package com.sanctum.core.feature.share.presentation

import androidx.compose.ui.graphics.ImageBitmap

// Platform share bridge. Lives in presentation (not domain) because its contract
// is inherently a UI type: callers share a rendered ImageBitmap.
expect class ShareController() {
    fun shareImage(image: ImageBitmap)
    fun isShareSupported(): Boolean
}
