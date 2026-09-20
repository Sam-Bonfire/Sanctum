package com.sanctum.core.feature.compass.domain

import com.sanctum.core.core.navigation.UrlOpener
import kotlinx.serialization.Serializable

@Serializable
class StreamLauncher(
    val title: String,
    val url: String,
    val startsAtMs: Long,
) {
    fun launch(urlOpener: UrlOpener): Boolean {
        if (url.isBlank()) return false
        return urlOpener.openUrl(url)
    }
}
