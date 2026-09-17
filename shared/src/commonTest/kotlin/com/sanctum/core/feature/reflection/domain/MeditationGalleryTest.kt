package com.sanctum.core.feature.reflection.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MeditationGalleryTest {

    private val gallery = MeditationGallery(
        listOf(
            MeditationTrack("t1", "Morning Calm", 300_000, "guided"),
            MeditationTrack("t2", "Deep Sleep", 900_000, "sleep"),
        ),
    )

    @Test
    fun `filters by category`() {
        assertEquals(listOf("t1"), gallery.byCategory("GUIDED").map { it.id })
    }

    @Test
    fun `filters by duration and finds by id`() {
        assertEquals(listOf("t1"), gallery.shorterThan(600_000).map { it.id })
        assertEquals("Deep Sleep", gallery.find("t2")?.title)
        assertNull(gallery.find("missing"))
    }
}
