package com.sanctum.core.core.navigation

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class UrlOpener actual constructor() {
    actual fun openUrl(url: String): Boolean {
        if (url.isBlank()) return false
        val nsUrl = NSURL.URLWithString(url) ?: return false
        return try {
            UIApplication.sharedApplication.openURL(nsUrl)
        } catch (_: Exception) {
            false
        }
    }
}

actual fun getUrlOpener(): UrlOpener = UrlOpener()
