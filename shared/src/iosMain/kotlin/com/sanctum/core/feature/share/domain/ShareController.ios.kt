package com.sanctum.core.feature.share.domain

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

actual class ShareController actual constructor() {
    @OptIn(ExperimentalForeignApi::class)
    actual fun shareImage(image: ImageBitmap) {
        val skiaBitmap = image.asSkiaBitmap()
        val skiaImage = Image.makeFromBitmap(skiaBitmap)
        val bytes = skiaImage.encodeToData()?.bytes ?: return

        val nsData = NSData.dataWithBytes(bytes.refTo(0), bytes.size.toULong())
        val uiImage = UIImage(data = nsData)

        val activityViewController = UIActivityViewController(
            activityItems = listOf(uiImage),
            applicationActivities = null,
        )

        // Find the root view controller to present the share sheet
        val window = UIApplication.sharedApplication.keyWindow
        val rootViewController = window?.rootViewController

        // Setup for iPad popover if needed
        activityViewController.popoverPresentationController?.sourceView = rootViewController?.view

        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }

    actual fun isShareSupported(): Boolean = true
}
