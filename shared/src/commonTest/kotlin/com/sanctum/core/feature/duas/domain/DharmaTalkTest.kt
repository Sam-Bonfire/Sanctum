package com.sanctum.core.feature.duas.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class DharmaTalkTest {

    @Test
    fun testParseDharmaTalksHappyPath() {
        val rss = """
            <rss>
                <channel>
                    <item>
                        <title>Mindfulness 101</title>
                        <enclosure url="https://example.com/audio1.mp3" type="audio/mpeg" />
                    </item>
                    <item>
                        <title>Compassion</title>
                        <enclosure url="https://example.com/audio2.mp3" type="audio/mpeg" />
                    </item>
                </channel>
            </rss>
        """.trimIndent()

        val talks = parseDharmaTalks(rss)

        assertEquals(2, talks.size)
        assertEquals("Mindfulness 101", talks[0].title)
        assertEquals("https://example.com/audio1.mp3", talks[0].audioUrl)
        assertEquals("Compassion", talks[1].title)
        assertEquals("https://example.com/audio2.mp3", talks[1].audioUrl)
    }

    @Test
    fun testParseDharmaTalksEdgeCases() {
        val rss = """
            <rss>
                <channel>
                    <item>
                        <title>   Whitespace Title   </title>
                        <enclosure url="https://example.com/audio3.mp3" type="audio/mpeg" />
                    </item>
                    <item>
                        <!-- Missing Title -->
                        <enclosure url="https://example.com/audio4.mp3" type="audio/mpeg" />
                    </item>
                    <item>
                        <title>Missing Audio</title>
                    </item>
                </channel>
            </rss>
        """.trimIndent()

        val talks = parseDharmaTalks(rss)

        assertEquals(2, talks.size)

        assertEquals("Whitespace Title", talks[0].title)
        assertEquals("https://example.com/audio3.mp3", talks[0].audioUrl)

        assertEquals("Missing Audio", talks[1].title)
        assertEquals("", talks[1].audioUrl)
    }

    @Test
    fun testParseDharmaTalksEmpty() {
        val rss = "<rss><channel></channel></rss>"
        val talks = parseDharmaTalks(rss)
        assertEquals(0, talks.size)
    }
}
