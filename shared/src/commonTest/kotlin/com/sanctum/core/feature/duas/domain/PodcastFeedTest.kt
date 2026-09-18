package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PodcastFeedTest {

    private val rss = """
        <rss><channel>
        <item><title>Episode One</title><enclosure url="https://x.test/e1.mp3" /></item>
        <item><title>Broken</title></item>
        </channel></rss>
    """.trimIndent()

    @Test
    fun `parses episodes with audio`() {
        val episodes = parseRssEpisodes(rss)
        assertEquals(2, episodes.size)
        assertEquals("https://x.test/e1.mp3", episodes[0].audioUrl)
        assertEquals("", episodes[1].audioUrl)
    }

    @Test
    fun `returns empty without items`() {
        assertTrue(parseRssEpisodes("<rss></rss>").isEmpty())
    }
}
