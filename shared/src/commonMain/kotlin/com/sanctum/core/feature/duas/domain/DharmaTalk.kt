package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
data class DharmaTalk(
    val title: String,
    val audioUrl: String,
    val publishedAt: String = "",
    val durationSecs: Long = 0,
)

fun parseDharmaTalks(rss: String): List<DharmaTalk> {
    val items = Regex("<item>(.*?)</item>", RegexOption.DOT_MATCHES_ALL).findAll(rss)
    return items.mapNotNull { match ->
        val block = match.groupValues[1]
        val title = Regex("<title>(.*?)</title>", RegexOption.DOT_MATCHES_ALL)
            .find(block)?.groupValues?.get(1)?.trim() ?: return@mapNotNull null
        val url = Regex("<enclosure[^>]*url=\"([^\"]+)\"").find(block)?.groupValues?.get(1) ?: ""
        DharmaTalk(title = title, audioUrl = url)
    }.toList()
}
