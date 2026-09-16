package com.sanctum.core.core.navigation

expect class UrlOpener() {
    fun openUrl(url: String): Boolean
}

expect fun getUrlOpener(): UrlOpener
