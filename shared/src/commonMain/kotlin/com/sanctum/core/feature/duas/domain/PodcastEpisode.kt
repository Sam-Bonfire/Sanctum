package com.sanctum.core.feature.duas.domain

import kotlinx.serialization.Serializable

@Serializable
data class PodcastEpisode(
    val title: String,
    val audioUrl: String,
    val publishedAt: String = "",
    val durationSecs: Long = 0,
)

fun parseRssEpisodes(rss: String): List<PodcastEpisode> {
    val items = Regex("<item>(.*?)</item>", RegexOption.DOT_MATCHES_ALL).findAll(rss)
    return items.mapNotNull { match ->
        val block = match.groupValues[1]
        val title = Regex("<title>(.*?)</title>", RegexOption.DOT_MATCHES_ALL)
            .find(block)?.groupValues?.get(1)?.trim() ?: return@mapNotNull null
        val url = Regex("<enclosure[^>]*url=\"([^\"]+)\"").find(block)?.groupValues?.get(1) ?: ""
        PodcastEpisode(title = title, audioUrl = url)
    }.toList()
}
