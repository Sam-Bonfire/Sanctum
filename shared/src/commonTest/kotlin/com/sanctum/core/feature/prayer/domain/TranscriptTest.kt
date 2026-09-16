package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TranscriptTest {

    private val transcript = Transcript(
        "a1",
        listOf(
            TranscriptSegment(0, 1000, "In the beginning"),
            TranscriptSegment(1000, 2000, "there was light"),
        ),
    )

    @Test
    fun `finds active segment`() {
        assertEquals("In the beginning", transcript.activeSegmentAt(500)?.text)
        assertNull(transcript.activeSegmentAt(2000))
    }

    @Test
    fun `searches case insensitively`() {
        assertEquals(1, transcript.search("LIGHT").size)
        assertEquals(0, transcript.search("  ").size)
    }
}
