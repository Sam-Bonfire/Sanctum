package com.sanctum.core.core.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class UrlOpener actual constructor() : KoinComponent {
    private val context: Context by inject()

    actual fun openUrl(url: String): Boolean {
        if (url.isBlank()) return false
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}

actual fun getUrlOpener(): UrlOpener = UrlOpener()
