package com.sanctum.core.feature.prayer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FloatingPlayerTest {

    private val state = FloatingPlayerState("Dua", durationMs = 1000, isVisible = true)

    @Test
    fun `progress reflects position`() {
        assertEquals(0f, state.progress())
        assertEquals(0.5f, state.seekTo(500).progress())
    }

    @Test
    fun `toggle flips playback`() {
        assertTrue(state.toggle().isPlaying)
        assertFalse(state.toggle().toggle().isPlaying)
    }

    @Test
    fun `seek clamps to duration`() {
        assertEquals(1000, state.seekTo(5000).positionMs)
        assertEquals(0, state.seekTo(-10).positionMs)
    }
}
