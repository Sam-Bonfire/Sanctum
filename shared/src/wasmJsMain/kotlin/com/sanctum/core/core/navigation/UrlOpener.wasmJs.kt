package com.sanctum.core.core.navigation

import kotlinx.browser.window

actual class UrlOpener actual constructor() {
    actual fun openUrl(url: String): Boolean {
        if (url.isBlank()) return false
        return try {
            window.open(url, "_blank")
            true
        } catch (_: Exception) {
            false
        }
    }
}

actual fun getUrlOpener(): UrlOpener = UrlOpener()
